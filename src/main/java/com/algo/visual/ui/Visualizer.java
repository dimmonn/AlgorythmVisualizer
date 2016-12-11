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
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Font;

public class Visualizer {

	private JFrame frmAlgo;
	private JTextField inputData;
	private JTextArea numOfIfsArea;
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
	private JPanel numOfOpsBorder;
	private JTable table = new JTable();;
	private Algos algos = null;
	private JLabel lblAlgName = new JLabel("Bubble Sort");

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

		configBorder.setBounds(12, 13, 1550, 223);

		mainBorder.setBounds(12, 255, getFrmAlgo().getWidth() - 50, (int) (getFrmAlgo().getHeight() - 300));
		mainPannel.setBounds(105, 27, getFrmAlgo().getWidth() - 75, (int) (getFrmAlgo().getHeight() - 340));

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
		configPanel.setBounds(12, 16, 1525, 190);
		setUpUi(configPanel);

		inputData = new JTextField();
		inputData.setBounds(126, 76, 419, 33);
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
		lblNumOfSwaps.setBounds(14, 28, 71, 24);
		configPanel.add(lblNumOfSwaps);
		lblNumOfSwaps.setBackground(new Color(51, 153, 255));

		JLabel lblAlgoType = new JLabel("Algorythm Type");
		lblAlgoType.setBounds(446, 23, 99, 34);
		lblAlgoType.setBackground(SystemColor.textHighlight);
		configPanel.add(lblAlgoType);

		JLabel lblInData = new JLabel("Input Array");
		lblInData.setBounds(12, 80, 79, 24);
		configPanel.add(lblInData);

		numOfOpsBorder = new JPanel();
		numOfOpsBorder.setBorder(UIManager.getBorder("TextArea.border"));
		numOfOpsBorder.setBounds(294, 19, 71, 44);
		configPanel.add(numOfOpsBorder);
		numOfOpsBorder.setLayout(null);

		setNumOfOperations(new JTextArea());
		getNumOfOperations().setEditable(false);
		getNumOfOperations().setRows(1);

		Label lblDelay = new Label("Speed Delay");
		lblDelay.setBounds(673, 16, 88, 24);
		configPanel.add(lblDelay);

		JLabel lblNumOfOpps = new JLabel("Num Of Operations");
		lblNumOfOpps.setBounds(176, 29, 118, 34);
		lblNumOfOpps.setBackground(SystemColor.textHighlight);
		configPanel.add(lblNumOfOpps);

		JPanel numOfIfsBorder = new JPanel();
		numOfIfsBorder.setLayout(null);
		numOfIfsBorder.setBorder(UIManager.getBorder("TextArea.border"));
		numOfIfsBorder.setBounds(93, 19, 71, 44);
		configPanel.add(numOfIfsBorder);

		numOfIfsArea = new JTextArea();
		numOfIfsArea.setRows(1);
		numOfIfsArea.setEditable(false);
		numOfIfsArea.setBounds(12, 13, 48, 18);
		numOfIfsBorder.add(numOfIfsArea);
		setUpTable();
	}

	public void setUpTable() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Algorithm Complexity",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(831, 13, 584, 143);
		configPanel.add(panel);
		panel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 67, 569, 63);
		panel.add(scrollPane);
		table.setFont(new Font("Tahoma", Font.PLAIN, 28));
		table.setRowHeight(table.getRowHeight() + 24);
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		table.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "Best Case", "Average Case", "Worst Case" }));
		lblAlgName.setBounds(182, 18, 222, 36);
		panel.add(lblAlgName);
		lblAlgName.setFont(new Font("Tahoma", Font.PLAIN, 32));
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(104);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(172);
		table.getColumnModel().getColumn(1).setMinWidth(40);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(146);
		table.getColumnModel().getColumn(2).setMinWidth(40);

		insertRow(new Object[] { "Ω(n)", "Θ(n^2)", "O(n^2)" });
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
		speedDelay.setBounds(765, 19, 44, 33);
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

		JPanel algoTypeBorder = new JPanel();
		algoTypeBorder.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		algoTypeBorder.setBounds(557, 13, 112, 96);
		configPanel.add(algoTypeBorder);
		algoTypeBorder.setLayout(null);
		algosAvailable.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (algosAvailable.getSelectedValue().equals("Bubble Sort")) {
					algos = Algos.BUBBLE;
					lblAlgName.setText("Bubble Sort");
					insertRow(new Object[] { "Ω(n)", "Θ(n^2)", "O(n^2)" });
				} else if (algosAvailable.getSelectedValue().equals("Merge Sort")) {
					algos = Algos.MERGE;
					lblAlgName.setText("Merge Sort");
					insertRow(new Object[] { "Ω(n log(n))", "Θ(n log(n))", "O(n log(n))" });
				} else if (algosAvailable.getSelectedValue().equals("Insertion Sort")) {
					algos = Algos.INSERTION;
					lblAlgName.setText("Insertion Sort");
					insertRow(new Object[] { "Ω(n)", "Θ(n^2)", "O(n^2)" });
				} else if (algosAvailable.getSelectedValue().equals("Shell Sort")) {
					algos = Algos.SHELL;
					lblAlgName.setText("Shell Sort");
					insertRow(new Object[] { "Ω(n log(n))", "Θ(n(log(n))^2)", "O(n(log(n))^2)" });
				}
			}

		});
		algosAvailable.setBounds(12, 13, 90, 70);
		algoTypeBorder.add(algosAvailable);

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
	}

	public void insertRow(Object[] row) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		if (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		model.insertRow(0, row);
	}

	public JSpinner getSpeedDelay() {
		return speedDelay;
	}

	public JTextArea getNumOfOperations() {
		return numOfOperations;
	}

	private void setNumOfOperations(JTextArea numOfOperations) {
		this.numOfOperations = numOfOperations;
		numOfOperations.setBounds(12, 13, 48, 18);
		numOfOpsBorder.add(numOfOperations);
	}

	public JTextArea getNumOfIfs() {
		return numOfIfsArea;
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
						AtomicInteger[] _sortable = linesToYaxisData();
						algoHelper.mergeSort(_sortable);
						followUp(_lines);
					}
					getFrmAlgo().setResizable(true);
					chckbxRandomData.setSelected(false);
					run.setEnabled(false);
				}

				public AtomicInteger[] linesToYaxisData() {
					List<AtomicInteger> sortable = getLines().parallelStream().map(DrawPanel::getAtomicYReference)
							.collect(Collectors.toList());
					AtomicInteger[] _sortable = sortable.toArray(new AtomicInteger[sortable.size()]);
					return _sortable;
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