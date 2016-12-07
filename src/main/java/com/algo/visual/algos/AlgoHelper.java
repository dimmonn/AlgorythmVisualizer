package com.algo.visual.algos;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

import com.algo.visual.drawing.DrawPanel;
import com.algo.visual.ui.Visualizer;

public class AlgoHelper {

	private final Visualizer visualizer;
	private AtomicInteger[] _sortable;
	private AtomicInteger[] helper;
	private static final Logger LOGGER = Logger.getLogger(AlgoHelper.class.getName());
	private int ops, swaps;

	public AlgoHelper(Visualizer visualizer) {
		this.visualizer = visualizer;
	}

	public void mergeSort(AtomicInteger[] _sortable) {
		swaps = 0;
		ops = 0;
		lazyLoadWhatToSort(_sortable);
		_mergesort(0, _sortable.length - 1);
		helper = null;
	}

	private void lazyLoadWhatToSort(AtomicInteger[] _sortable) {
		if (helper == null) {
			this._sortable = _sortable;
			helper = new AtomicInteger[_sortable.length];
		}
	}

	private void slowDown() {
		if ((Integer) visualizer.getSpeedDelay().getValue() >= 1) {
			sleep((Integer) visualizer.getSpeedDelay().getValue());
		}
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e1) {
			LOGGER.log(Level.SEVERE, e1.getMessage());
		}
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
		prepareHelper(low, high);
		int i = low;
		int j = middle + 1;
		int k = low;
		while (i <= middle && j <= high) {
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
			if (helper[i].get() >= helper[j].get()) {
				i = injectValue(i, k);
			} else {
				j = injectValue(j, k);
			}
			k++;
		}
		while (i <= middle) {
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
			slowDown();
			_sortable[k].set(helper[i].get());
			getKline(k).setColor(Color.BLACK);
			k++;
			i++;
			visualizer.getFrmAlgo().repaint();
		}
	}

	private void prepareHelper(int low, int high) {
		for (int i = low; i <= high; i++) {
			helper[i] = new AtomicInteger(_sortable[i].get());
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
		}
	}

	public void innsertionSort(JTextArea numOfOperations, final DrawPanel[] input) {

		for (int i = 1; i < input.length; i++) {
			for (int j = i; j > 0; j--) {
				numOfOperations.setText(String.valueOf(++ops));
				if (input[j].getYY() > input[j - 1].getYY()) {
					swap(input, j - 1, j, swaps++);
				}
			}
		}
	}

	private int injectValue(int j, int k) {
		slowDown();
		_sortable[k].set(helper[j].get());
		visualizer.getNumOfIfs().setText(String.valueOf(++swaps));
		j++;
		getKline(k).setColor(Color.BLACK);
		visualizer.getFrmAlgo().repaint();
		return j;
	}

	private DrawPanel getKline(int k) {
		return visualizer.getLines().toArray(new DrawPanel[visualizer.getLines().size()])[k];
	}

	public void bubbleSort(JTextArea numOfOperations, final DrawPanel[] _lines) {
		ops = 0;
		swaps = 0;
		for (int i = 0; i < _lines.length - 1; i++) {
			for (int j = 0; j < _lines.length - 1; j++) {
				numOfOperations.setText(String.valueOf(++ops));
				if (_lines[j].getYY() < _lines[j + 1].getYY()) {
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
		visualizer.getNumOfIfs().setText(String.valueOf(swaps));
		int deltaJ = _lines[j].getXX();
		int deltaI = _lines[i].getXX();
		_lines[i].setColor(Color.BLACK);
		_lines[j].setColor(Color.BLACK);
		moveSwap(_lines[j].getAtomicXReference(), _lines[i].getAtomicXReference(), deltaJ, deltaI);
		_lines[i].setColor(Color.YELLOW);
		_lines[j].setColor(Color.YELLOW);
	}

	private void moveSwap(AtomicInteger atomicInteger, AtomicInteger atomicInteger2, int deltaJ, int deltaI) {
		for (int k = deltaJ; k <= deltaI; k++) {
			slowDown();
			atomicInteger.set(k);
			atomicInteger2.set(deltaI - (k - deltaJ));
			visualizer.getFrmAlgo().repaint();
		}
	}

	public void shell(final DrawPanel[] input) {
		int increment = input.length / 2;
		while (increment > 0) {
			for (int i = increment; i < input.length; i++) {
				int j = i;
				DrawPanel temp = new DrawPanel(input[i]);
				while (j >= increment && input[j - increment].getYY() < temp.getYY()) {
					slowDown();
					input[j].setColor(Color.BLACK);
					int tmpX = input[j - increment].getXX();
					int tmpXTo = input[j].getXX();
					for (int k = tmpX; k <= tmpXTo; k++) {
						input[j - increment].setColor(Color.BLACK);
						input[j - increment].setXX(k);
						visualizer.getFrmAlgo().repaint();
					}
					slowDown();
					input[j - increment].setXX(tmpX);
					input[j].setYY(input[j - increment].getYY());
					input[j].setColor(Color.YELLOW);
					j = j - increment;
					visualizer.getFrmAlgo().repaint();
				}
				slowDown();
				input[j].setYY(temp.getYY());
				visualizer.getFrmAlgo().repaint();
			}
			if (increment == 2) {
				increment = 1;
			} else {
				increment *= (5.0 / 11);
			}
		}
	}
}