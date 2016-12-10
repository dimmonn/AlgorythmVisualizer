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
import com.algo.visual.algos.AlgoHelper;
import com.algo.visual.algos.Algos;
import com.algo.visual.drawing.DrawPanel;

import java.awt.SystemColor;
import java.awt.BorderLayout;
import java.awt.Label;
import javax.swing.border.TitledBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
	private JPanel configPanel;
	private JPanel configBorder;
	private JPanel mainBorder;

	public JCheckBox getChckbxRandomData() {
		return chckbxRandomData;
	}

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

		configBorder = new JPanel();
		frmAlgo.getContentPane().add(configBorder);
		configBorder.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sort Config",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		configBorder.setLayout(null);
		chckbxRandomData.setBounds(6, 121, 112, 35);

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
		run.setBounds(126, 122, 519, 34);
		run.setEnabled(false);
		run.addActionListener(new AlgoRun());
		configPanel = new JPanel();
		configBorder.add(configPanel);
		setUpUi(configPanel);

		inputData = new JTextField();
		inputData.setBounds(126, 65, 419, 44);
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
					int x = i * (getFrmAlgo().getWidth() - 50) / _data.size() + 20;
					int _y = getFrmAlgo().getHeight() - 360 - ((getFrmAlgo().getHeight() - 360) * _data.get(i)) / max
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
							dialog.setAlwaysOnTop(false);
							dialog.setVisible(true);
							isAllowed.set(false);
						}
					}
				});
			}
		});
		configPanel.add(inputData);
		inputData.setColumns(10);
		configPanel.add(chckbxRandomData);

		JLabel lblNumOfSwaps = new JLabel("Num Of Ifs");
		lblNumOfSwaps.setBounds(6, 8, 71, 44);
		configPanel.add(lblNumOfSwaps);
		lblNumOfSwaps.setBackground(new Color(51, 153, 255));

		JLabel lblAlgoType = new JLabel("Algorythm Type");
		lblAlgoType.setBounds(442, 8, 99, 44);
		lblAlgoType.setBackground(SystemColor.textHighlight);
		configPanel.add(lblAlgoType);

		JLabel lblInData = new JLabel("Input Array");
		lblInData.setBounds(6, 65, 90, 44);
		configPanel.add(lblInData);

		setNumOfOperations(new JTextArea());
		configPanel.add(getNumOfOperations());
		getNumOfOperations().setEditable(false);
		getNumOfOperations().setRows(1);

		Label lblDelay = new Label("Speed Delay");
		lblDelay.setBounds(651, 16, 88, 24);
		configPanel.add(lblDelay);

		JLabel lblNumOfOpps = new JLabel("Num Of Operations");
		lblNumOfOpps.setBounds(220, 8, 118, 44);
		lblNumOfOpps.setBackground(SystemColor.textHighlight);
		configPanel.add(lblNumOfOpps);

		setNumOfIfs(new JTextArea());
		getNumOfIfs().setEditable(false);
		getNumOfIfs().setRows(1);
	}

	private void setUpJframe() {
		JFrame frame = new JFrame();
		frame.getContentPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				configBorder.setBounds(12, 25, getFrmAlgo().getWidth() - 50, 205);
				configPanel.setBounds(12, 25, getFrmAlgo().getWidth() - 75, 165);
				mainBorder.setBounds(12, 245, getFrmAlgo().getWidth() - 50, (int) (getFrmAlgo().getHeight() - 300));
				mainPannel.setBounds(12, 25, getFrmAlgo().getWidth() - 75, (int) (getFrmAlgo().getHeight() - 340));
			}
		});
		frame.getContentPane().setLayout(null);
		setFrmAlgo(frame);
		getFrmAlgo().getContentPane().setBackground(new Color(240, 232, 208));
		getFrmAlgo().setAlwaysOnTop(false);
		getFrmAlgo().setIconImage(
				Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("algo.png")));
		getFrmAlgo().setTitle("Algo");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		getFrmAlgo().setSize(screenSize.width * 5 / 6, screenSize.height * 5 / 6);
		getFrmAlgo().setMinimumSize(new Dimension(900, 500));
		getFrmAlgo().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainBorder = new JPanel();
		mainBorder.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sort Visualization",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frmAlgo.getContentPane().add(mainBorder);
		mainBorder.setLayout(null);
		mainBorder.add(mainPannel);
		mainPannel.setBackground(Color.WHITE);
		mainPannel.setLayout(new BorderLayout(0, 0));
	}

	private void setUpUi(JPanel panel) {
		panel.setForeground(new Color(128, 0, 0));
		panel.setBackground(Color.WHITE);
		configPanel.setLayout(null);
		panel.add(run);
		speedDelay.setBounds(743, 19, 44, 33);
		getSpeedDelay().setModel(new SpinnerNumberModel(0, 0, 5000, 1));
		panel.add(getSpeedDelay());
		listOfAlgos(panel);
	}

	private void fillInRandomData() {
		for (int i = 0; i < getLines().size(); i++) {
			int x = 20 + i;
			int y = getFrmAlgo().getHeight() - 360
					- ((getFrmAlgo().getHeight() - 360) * new Random().nextInt(getFrmAlgo().getHeight() - 360))
							/ (getFrmAlgo().getHeight() - 360)
					+ 20;
			getLines().get(i).setXX(x);
			getLines().get(i).setYY(y);
		}
	}

	private void listOfAlgos(JPanel panel) {
		algosAvailable.setBounds(553, 21, 92, 88);

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
		numOfOperations.setBounds(338, 8, 92, 44);
	}

	public JTextArea getNumOfIfs() {
		return numOfIfs;
	}

	private void setNumOfIfs(JTextArea numOfIfs) {
		this.numOfIfs = numOfIfs;
		numOfIfs.setBounds(93, 8, 82, 44);
		configPanel.add(numOfIfs);
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
					} else if (algo == Algos.INSERTION) {
						algoHelper.innsertionSort(numOfOperations, _lines);
						followUp(_lines);
					} else if (algo == Algos.SHELL) {
						algoHelper.shell(_lines);
						followUp(_lines);
					} else if (algo == Algos.MERGE) {
						List<AtomicInteger> sortable = getLines().parallelStream().map(DrawPanel::getAtomicYReference)
								.collect(Collectors.toList());
						AtomicInteger[] _sortable = sortable.toArray(new AtomicInteger[sortable.size()]);
						algoHelper.mergeSort(_sortable);
						followUp(_lines);
					}
					getFrmAlgo().setResizable(true);
				}

				private void followUp(final DrawPanel[] _lines) {
					getFrmAlgo().repaint();
					run.setEnabled(true);
					chckbxRandomData.setEnabled(true);
				}

			}).start();
		}
	}
}