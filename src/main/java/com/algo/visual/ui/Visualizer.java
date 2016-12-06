package com.algo.visual.ui;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.awt.Toolkit;
import javax.swing.border.EtchedBorder;

import com.algo.visual.algos.AlgoHelper;
import com.algo.visual.algos.Algos;
import com.algo.visual.drawing.DrawPanel;

import java.awt.SystemColor;
import java.awt.BorderLayout;
import java.awt.Label;

public class Visualizer {

	private JFrame frmAlgo;
	private JTextField inputData;
	private JTextArea numOfIfs;
	private final List<DrawPanel> lines = new ArrayList<>();
	private final JCheckBox chckbxRandomData = new JCheckBox("Random/Clear");
	private final JButton run = new JButton("Run");
	private final JPanel mainPannel = new JPanel();
	private final JSpinner speedDelay = new JSpinner();
	private final JList<String> algosAvailable = new JList<>();
	private JTextArea numOfOperations;
	private final AlgoHelper algoHelper = new AlgoHelper(Visualizer.this);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Visualizer window = new Visualizer();
				window.getFrmAlgo().setVisible(true);
			} catch (Exception e) {
				// LOGGER.log(Level.SEVERE, e.getMessage());
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	private Visualizer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setUpJframe();
		JPanel panel = new JPanel();
		setUpUi(panel);

		inputData = new JTextField();
		inputData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getFrmAlgo().setResizable(false);
				getLines().clear();
				mainPannel.removeAll();
				getFrmAlgo().repaint();
				List<String> data = Arrays.asList(inputData.getText().split(","));
				final AtomicBoolean isAllowed = new AtomicBoolean();
				isAllowed.set(true);
				validateDataInput(data, isAllowed);
				if (isAllowed.get()) {
					chckbxRandomData.setEnabled(false);
					List<Integer> _data = data.stream().map(Integer::valueOf).collect(Collectors.toList());
					int max = Collections.max(_data);
					fillInData(_data, max);
					inputData.setText("");
					run.setEnabled(true);
				}
			}

			private void fillInData(List<Integer> _data, int max) {
				for (int i = 0; i < _data.size(); i++) {
					DrawPanel drawPanel = new DrawPanel();
					drawPanel.setVisible(true);
					getLines().add(drawPanel);
					mainPannel.add(getLines().get(i));
					mainPannel.validate();
					mainPannel.repaint();
					int x = i * (getFrmAlgo().getWidth() - 50) / _data.size() + 50;
					int _y = getFrmAlgo().getHeight() - 300 - ((getFrmAlgo().getHeight() - 300) * _data.get(i)) / max
							+ 20;
					getLines().get(i).setXX(x);
					getLines().get(i).setYY(_y);
				}
			}

			private void validateDataInput(List<String> data, final AtomicBoolean isAllowed) {
				data.forEach(d -> {
					for (int i = 0; i < d.length(); i++) {
						if (!Character.isDigit(d.charAt(i))) {
							JOptionPane optionPane = new JOptionPane();
							JDialog dialog = optionPane.createDialog("Warning");
							dialog.setAlwaysOnTop(true);
							dialog.setVisible(true);
							isAllowed.set(false);
						}
					}
				});
			}
		});
		inputData.setBounds(126, 65, 419, 44);
		panel.add(inputData);
		inputData.setColumns(10);

		chckbxRandomData.addActionListener(e -> {
			mainPannel.removeAll();
			getLines().clear();
			getFrmAlgo().repaint();
			if (chckbxRandomData.isSelected()) {
				inputData.setEditable(false);
				getFrmAlgo().setResizable(false);
				for (int i = 0; i < getFrmAlgo().getWidth() - 130; i++) {
					DrawPanel drawPanel = new DrawPanel();
					getLines().add(drawPanel);
					mainPannel.add(getLines().get(i), BorderLayout.CENTER);
					mainPannel.validate();
				}
				fillInRandomData();
				run.setEnabled(true);
			} else if (!chckbxRandomData.isSelected()) {
				getLines().clear();
				mainPannel.removeAll();
				run.setEnabled(false);
				inputData.setEditable(true);
			}
		});
		chckbxRandomData.setBounds(6, 121, 112, 35);
		panel.add(chckbxRandomData);
		run.setEnabled(false);
		run.addActionListener(new AlgoRun());
	}

	private void setUpJframe() {
		setFrmAlgo(new JFrame());
		getFrmAlgo().getContentPane().setForeground(new Color(128, 0, 128));
		getFrmAlgo().getContentPane().setBackground(Color.RED);
		getFrmAlgo().setAlwaysOnTop(true);
		getFrmAlgo().setIconImage(
				Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("algo.png")));
		getFrmAlgo().setTitle("Algo");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		getFrmAlgo().setSize(screenSize.width * 7 / 8, screenSize.height * 7 / 8);
		getFrmAlgo().setMinimumSize(new Dimension(900, 500));
		getFrmAlgo().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getFrmAlgo().getContentPane().setLayout(null);

		mainPannel.setBounds(12, 178, 1870, 765);
		getFrmAlgo().getContentPane().add(mainPannel);
		mainPannel.setBackground(Color.RED);
		mainPannel.setLayout(new BorderLayout(0, 0));
	}

	private void setUpUi(JPanel panel) {
		panel.setBounds(12, 13, 1870, 163);
		getFrmAlgo().getContentPane().add(panel);
		panel.setForeground(new Color(128, 0, 0));
		panel.setBackground(new Color(189, 183, 107));
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(0, 0, 0), new Color(75, 0, 130)));
		panel.setLayout(null);

		JLabel lblNumOfSwaps = new JLabel("Num Of Ifs");
		lblNumOfSwaps.setBounds(12, 8, 92, 44);
		panel.add(lblNumOfSwaps);
		lblNumOfSwaps.setBackground(new Color(51, 153, 255));

		JLabel label = new JLabel("Algorythm Type");
		label.setBackground(SystemColor.textHighlight);
		label.setBounds(442, 8, 99, 44);
		panel.add(label);

		JLabel lblNewLabel = new JLabel("Input Array");
		lblNewLabel.setBounds(22, 65, 74, 44);
		panel.add(lblNewLabel);

		setNumOfOperations(new JTextArea());
		getNumOfOperations().setBounds(338, 16, 92, 44);
		panel.add(getNumOfOperations());
		getNumOfOperations().setEditable(false);
		getNumOfOperations().setRows(1);

		setNumOfIfs(new JTextArea());
		getNumOfIfs().setBounds(126, 16, 82, 44);
		panel.add(getNumOfIfs());
		getNumOfIfs().setEditable(false);
		getNumOfIfs().setRows(1);

		run.setBounds(126, 122, 519, 34);
		panel.add(run);

		getSpeedDelay().setModel(new SpinnerNumberModel(0, 0, 5000, 1));
		getSpeedDelay().setBounds(743, 19, 44, 33);
		panel.add(getSpeedDelay());

		Label lblDelay = new Label("Speed Delay");
		lblDelay.setBounds(651, 16, 88, 24);
		panel.add(lblDelay);
		listOfAlgos(panel);

		JLabel label_1 = new JLabel("Num Of Operations");
		label_1.setBackground(SystemColor.textHighlight);
		label_1.setBounds(220, 8, 118, 44);
		panel.add(label_1);
	}

	private void fillInRandomData() {
		for (int i = 0; i < getLines().size(); i++) {
			int x = 50 + i;
			int y = getFrmAlgo().getHeight() - 280
					- ((getFrmAlgo().getHeight() - 280) * new Random().nextInt(getFrmAlgo().getHeight() - 280))
							/ (getFrmAlgo().getHeight() - 280)
					+ 20;
			getLines().get(i).setXX(x);
			getLines().get(i).setYY(y);
		}
	}

	private void listOfAlgos(JPanel panel) {

		algosAvailable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		algosAvailable.setModel(new AbstractListModel<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			final String[] values = new String[] { "Bubble Sort", "Merge Sort", "Insertion Sort", "Shell Sort" };

			public int getSize() {
				return values.length;
			}

			public String getElementAt(int index) {
				return values[index];
			}
		});
		algosAvailable.setSelectedIndex(0);
		algosAvailable.setBounds(553, 21, 92, 88);
		panel.add(algosAvailable);
	}

	public JSpinner getSpeedDelay() {
		return speedDelay;
	}

	public JTextArea getNumOfOperations() {
		return numOfOperations;
	}

	private void setNumOfOperations(JTextArea numOfOperations) {
		this.numOfOperations = numOfOperations;
	}

	public JTextArea getNumOfIfs() {
		return numOfIfs;
	}

	private void setNumOfIfs(JTextArea numOfIfs) {
		this.numOfIfs = numOfIfs;
	}

	public JFrame getFrmAlgo() {
		return frmAlgo;
	}

	private void setFrmAlgo(JFrame frmAlgo) {
		this.frmAlgo = frmAlgo;
	}

	public List<DrawPanel> getLines() {
		return lines;
	}

	public final class AlgoRun implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			chckbxRandomData.setEnabled(false);
			Algos algos = null;
			if (algosAvailable.getSelectedValue().equals("Bubble Sort")) {
				algos = Algos.BUBBLE;
			} else if (algosAvailable.getSelectedValue().equals("Merge Sort")) {
				algos = Algos.MERGE;
			} else if (algosAvailable.getSelectedValue().equals("Insertion Sort")) {
				algos = Algos.INSERTION;
			} else if (algosAvailable.getSelectedValue().equals("Shell Sort")) {
				algos = Algos.SHELL;
			}
			if (algos != null) {
				DrawPanel[] _lines = getLines().toArray(new DrawPanel[getLines().size()]);
				doWork(_lines, algos);
			}
		}

		public void doWork(final DrawPanel[] _lines, Algos algo) {
			new Thread(new Runnable() {
				public void run() {
					run.setEnabled(false);
					if (algo == Algos.BUBBLE) {
						algoHelper.bubbleSort(getNumOfOperations(), _lines);
						followUp(_lines);
					}
					if (algo == Algos.MERGE) {
						List<AtomicInteger> sortable = getLines().parallelStream().map(DrawPanel::getAtomicYReference)
								.collect(Collectors.toList());
						AtomicInteger[] _sortable = sortable.toArray(new AtomicInteger[sortable.size()]);
						algoHelper.mergeSort(_sortable);
						followUp(_lines);
					}
					getFrmAlgo().setResizable(true);
				}

				private void followUp(final DrawPanel[] _lines) {
					Arrays.stream(_lines).forEach(a -> a.setColor(Color.GREEN));
					getFrmAlgo().repaint();
					run.setEnabled(true);
					chckbxRandomData.setEnabled(true);
				}

			}).start();
		}
	}
}