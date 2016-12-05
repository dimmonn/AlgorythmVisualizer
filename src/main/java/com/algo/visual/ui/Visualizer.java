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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import java.awt.Toolkit;
import javax.swing.border.EtchedBorder;

import com.algo.visual.algos.Algos;
import com.algo.visual.drawing.DrawPanel;

import java.awt.SystemColor;
import java.awt.BorderLayout;
import java.awt.Label;

public class Visualizer {

	private JFrame frmAlgo;
	private static final Logger LOGGER = Logger.getLogger(Visualizer.class.getName());

	private JTextField inputData;
	private JTextArea numOfIfs;
	private final List<DrawPanel> lines = new ArrayList<>();
	private final JCheckBox chckbxRandomData = new JCheckBox("Random/Clear");
	private final JButton run = new JButton("Run");
	private final JPanel mainPannel = new JPanel();
	private final JSpinner speedDelay = new JSpinner();
	private final JList<String> algosAvailable = new JList<>();
	private AtomicInteger[] helper;
	private AtomicInteger[] _sortable;
	private int swaps = 0;
	private int ops = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Visualizer window = new Visualizer();
				window.frmAlgo.setVisible(true);
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
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
		JTextArea numOfOperations = setUpUi(panel);

		inputData = new JTextField();
		inputData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAlgo.setResizable(false);
				lines.clear();
				mainPannel.removeAll();
				frmAlgo.repaint();
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
					lines.add(drawPanel);
					mainPannel.add(lines.get(i));
					mainPannel.validate();
					mainPannel.repaint();
					int x = i * (frmAlgo.getWidth() - 50) / _data.size() + 50;
					int _y = frmAlgo.getHeight() - 300 - ((frmAlgo.getHeight() - 300) * _data.get(i)) / max + 20;
					lines.get(i).setXX(x);
					lines.get(i).setYY(_y);
				}
				helper = new AtomicInteger[lines.size()];
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
			lines.clear();
			frmAlgo.repaint();
			if (chckbxRandomData.isSelected()) {
				inputData.setEditable(false);
				frmAlgo.setResizable(false);
				for (int i = 0; i < frmAlgo.getWidth() - 130; i++) {
					DrawPanel drawPanel = new DrawPanel();
					lines.add(drawPanel);
					mainPannel.add(lines.get(i), BorderLayout.CENTER);
					mainPannel.validate();
				}
				helper = new AtomicInteger[lines.size()];
				fillInRandomData();
				run.setEnabled(true);
			} else if (!chckbxRandomData.isSelected()) {
				lines.clear();
				mainPannel.removeAll();
				run.setEnabled(false);
				inputData.setEditable(true);
			}
		});
		chckbxRandomData.setBounds(6, 121, 112, 35);
		panel.add(chckbxRandomData);
		run.setEnabled(false);
		run.addActionListener(new ActionListener() {
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
					DrawPanel[] _lines = lines.toArray(new DrawPanel[lines.size()]);
					doWork(_lines, algos);
				}
			}

			private void doWork(final DrawPanel[] _lines, Algos algo) {
				new Thread(new Runnable() {
					public void run() {
						run.setEnabled(false);
						if (algo == Algos.BUBBLE) {
							bubbleSort(numOfOperations, _lines, swaps);
							followUp(_lines);
						}
						if (algo == Algos.MERGE) {
							List<AtomicInteger> sortable = lines.parallelStream().map(DrawPanel::getAtomicYReference)
									.collect(Collectors.toList());
							_sortable = sortable.toArray(new AtomicInteger[sortable.size()]);
							mergeSort(_sortable);
							swaps = 0;
							ops = 0;
							List<DrawPanel> list = Arrays.asList(_lines);
							list.forEach(a -> a.setColor(Color.GREEN));
							followUp(_lines);
						}
						frmAlgo.setResizable(true);
					}

					private void followUp(final DrawPanel[] _lines) {
						Arrays.stream(_lines).forEach(a -> a.setColor(Color.GREEN));
						frmAlgo.repaint();
						run.setEnabled(true);
						chckbxRandomData.setEnabled(true);
					}

					private void mergeSort(AtomicInteger[] _sortable) {

						_mergesort(0, _sortable.length - 1);
					}

					private void _mergesort(int low, int high) {

						if (low < high) {
							int middle = low + (high - low) / 2;
							_mergesort(low, middle);
							_mergesort(middle + 1, high);
							merge(low, middle, high);
						}
					}

					private void merge(int low, int middle, int high) {
						for (int i = low; i <= high; i++) {
							helper[i] = new AtomicInteger(_sortable[i].get());
							numOfOperations.setText(String.valueOf(++ops));
						}
						int i = low;
						int j = middle + 1;
						int k = low;
						while (i <= middle && j <= high) {
							numOfOperations.setText(String.valueOf(++ops));
							if (helper[i].get() >= helper[j].get()) {
								slowDown();
								_sortable[k].set(helper[i].get());
								numOfIfs.setText(String.valueOf(++swaps));
								_lines[k].setColor(Color.BLACK);
								i++;
								frmAlgo.repaint();
							} else {
								slowDown();
								_sortable[k].set(helper[j].get());
								numOfIfs.setText(String.valueOf(++swaps));
								_lines[k].setColor(Color.BLACK);
								j++;
								frmAlgo.repaint();
							}
							k++;
						}
						while (i <= middle) {
							numOfOperations.setText(String.valueOf(++ops));
							slowDown();
							_sortable[k].set(helper[i].get());
							_lines[k].setColor(Color.BLACK);
							k++;
							i++;
							frmAlgo.repaint();
						}
					}

					private void slowDown() {
						if ((Integer) speedDelay.getValue() >= 1) {
							sleep((Integer) speedDelay.getValue());
						}
					}

					private void bubbleSort(JTextArea numOfOperations, final DrawPanel[] _lines, int swaps) {
						for (int i = 0; i < _lines.length - 1; i++) {
							for (int j = 0; j < _lines.length - 1; j++) {
								numOfOperations.setText(String.valueOf(++ops));
								if (_lines[j].getHHeight() > _lines[j + 1].getHHeight()) {
									swap(_lines, j, j + 1, swaps++);
								}
							}
							_lines[_lines.length - 1 - i].setColor(Color.BLACK);
						}
					}

					private void swap(final DrawPanel[] _lines, int i, int j, int swaps) {
						DrawPanel tmp = _lines[j];
						_lines[j] = _lines[i];
						_lines[i] = tmp;
						numOfIfs.setText(String.valueOf(swaps));
						int deltaJ = _lines[j].getXX();
						int deltaI = _lines[i].getXX();
						_lines[i].setColor(Color.BLACK);
						_lines[j].setColor(Color.BLACK);
						moveSwap(_lines[j].getAtomicXReference(), _lines[i].getAtomicXReference(), deltaJ, deltaI);
						_lines[i].setColor(Color.YELLOW);
						_lines[j].setColor(Color.YELLOW);
					}

					private void moveSwap(AtomicInteger atomicInteger, AtomicInteger atomicInteger2, int deltaJ,
							int deltaI) {
						for (int k = deltaJ; k <= deltaI; k++) {
							atomicInteger.set(k);
							atomicInteger2.set(deltaI - (k - deltaJ));
							frmAlgo.repaint();
							slowDown();
						}
					}

					private void sleep(int ms) {
						try {
							Thread.sleep(ms);
						} catch (InterruptedException e1) {
							LOGGER.log(Level.SEVERE, e1.getMessage());
						}
					}
				}).start();
			}
		});
	}

	private void setUpJframe() {
		frmAlgo = new JFrame();
		frmAlgo.getContentPane().setForeground(new Color(128, 0, 128));
		frmAlgo.getContentPane().setBackground(Color.RED);
		frmAlgo.setAlwaysOnTop(true);
		frmAlgo.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("algo.png")));
		frmAlgo.setTitle("Algo");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frmAlgo.setSize(screenSize.width * 7 / 8, screenSize.height * 7 / 8);
		frmAlgo.setMinimumSize(new Dimension(900, 500));
		frmAlgo.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frmAlgo.getContentPane().setLayout(null);

		mainPannel.setBounds(12, 178, 1870, 765);
		frmAlgo.getContentPane().add(mainPannel);
		mainPannel.setBackground(Color.RED);
		mainPannel.setLayout(new BorderLayout(0, 0));
	}

	private JTextArea setUpUi(JPanel panel) {
		panel.setBounds(12, 13, 1870, 163);
		frmAlgo.getContentPane().add(panel);
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

		JTextArea numOfOperations = new JTextArea();
		numOfOperations.setBounds(338, 16, 92, 44);
		panel.add(numOfOperations);
		numOfOperations.setEditable(false);
		numOfOperations.setRows(1);

		numOfIfs = new JTextArea();
		numOfIfs.setBounds(126, 16, 82, 44);
		panel.add(numOfIfs);
		numOfIfs.setEditable(false);
		numOfIfs.setRows(1);

		run.setBounds(126, 122, 519, 34);
		panel.add(run);

		speedDelay.setModel(new SpinnerNumberModel(0, 0, 5000, 1));
		speedDelay.setBounds(743, 19, 44, 33);
		panel.add(speedDelay);

		Label lblDelay = new Label("Speed Delay");
		lblDelay.setBounds(651, 16, 88, 24);
		panel.add(lblDelay);
		listOfAlgos(panel);

		JLabel label_1 = new JLabel("Num Of Operations");
		label_1.setBackground(SystemColor.textHighlight);
		label_1.setBounds(220, 8, 118, 44);
		return numOfOperations;
	}

	private void fillInRandomData() {
		for (int i = 0; i < lines.size(); i++) {
			int x = 50 + i;
			int y = frmAlgo.getHeight() - 280
					- ((frmAlgo.getHeight() - 280) * new Random().nextInt(frmAlgo.getHeight() - 280))
							/ (frmAlgo.getHeight() - 280)
					+ 20;
			lines.get(i).setXX(x);
			lines.get(i).setYY(y);
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
}