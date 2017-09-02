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
	private int ops, ifs;
	private DrawPanel[] qs;

	public AlgoHelper(Visualizer visualizer) {
		this.visualizer = visualizer;
	}

	public void mergeSort(AtomicInteger[] _sortable) {
		startCounter();
		lazyLoadWhatToSort(_sortable);
		_mergesort(0, _sortable.length - 1);
		helper = null;
	}

	public void innsertionSort(final DrawPanel[] input) {
		startCounter();
		for (int i = 1; i < input.length; i++) {
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
			input[i].setColor(new Color(0, 102, 153));
			for (int j = i; j > 0; j--) {
				visualizer.getNumOfOperations().setText(String.valueOf(++ops));
				if (input[j].getYY() > input[j - 1].getYY()) {
					swap(input, j - 1, j, ifs++);
				}
				input[j].setColor(new Color(0, 102, 153));
			}
		}
	}

	public void bubbleSort(JTextArea numOfOperations, final DrawPanel[] _lines) {
		startCounter();
		for (int i = 0; i < _lines.length - 1; i++) {
			numOfOperations.setText(String.valueOf(++ops));
			for (int j = 0; j < _lines.length - 1; j++) {
				numOfOperations.setText(String.valueOf(++ops));
				if (_lines[j].getYY() < _lines[j + 1].getYY()) {

					swap(_lines, j, j + 1, ifs++);
				}
			}
			_lines[_lines.length - 1 - i].setColor(new Color(0, 102, 153));
		}
		_lines[0].setColor(new Color(0, 102, 153));
	}

	public void pauseThreadIfNeeded() {
		while (visualizer.isToBePaused()) {
		}
		visualizer.setRunning(true);
		visualizer.getRun().setText("Pause");
	}

	public void shell(final DrawPanel[] input) {
		startCounter();
		int increment = input.length / 2;
		increment = shellTillIncPositive(input, increment);
		finallyColor(input);
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
			visualizer.getNumOfIfs().setText(String.valueOf(++ifs));
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
			slowDownAndPauseIfNeeded();
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
			if (helper[i].get() >= helper[j].get()) {
				visualizer.getNumOfIfs().setText(String.valueOf(++ifs));
				i = injectValue(i, k);
			} else {
				visualizer.getNumOfIfs().setText(String.valueOf(++ifs));
				j = injectValue(j, k);
			}
			k++;
		}
		while (i <= middle) {
			slowDownAndPauseIfNeeded();
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
			_sortable[k].set(helper[i].get());
			getKline(k).setColor(new Color(0, 102, 153));
			k++;
			i++;
			visualizer.getFrmAlgo().repaint();
		}
	}

	public void slowDownAndPauseIfNeeded() {
		slowDown();
		pauseThreadIfNeeded();
	}

	private void prepareHelper(int low, int high) {
		for (int i = low; i <= high; i++) {
			helper[i] = new AtomicInteger(_sortable[i].get());
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
		}
	}

	private int injectValue(int j, int k) {
		_sortable[k].set(helper[j].get());
		visualizer.getNumOfIfs().setText(String.valueOf(++ifs));
		j++;
		getKline(k).setColor(new Color(0, 102, 153));
		visualizer.getFrmAlgo().repaint();
		return j;
	}

	private DrawPanel getKline(int k) {
		return visualizer.getLines().toArray(new DrawPanel[visualizer.getLines().size()])[k];
	}

	private void swap(final DrawPanel[] _lines, int i, int j, int swaps) {
		DrawPanel tmp = _lines[j];
		_lines[j] = _lines[i];
		_lines[i] = tmp;
		int deltaJ = _lines[j].getXX();
		int deltaI = _lines[i].getXX();
		moveSwap(_lines[j].getAtomicXReference(), _lines[i].getAtomicXReference(), deltaJ, deltaI);
		visualizer.getNumOfIfs().setText(String.valueOf(swaps));
	}

	private void moveSwap(AtomicInteger atomicInteger1, AtomicInteger atomicInteger2, int deltaJ, int deltaI) {

		for (int k = deltaJ; k <= deltaI; k = (visualizer.getChckbxRandomData().isSelected() && k + 40 < deltaI)
				? k + 40 : k + 1) {
			slowDownAndPauseIfNeeded();
			atomicInteger1.set(k);
			atomicInteger2.set(deltaI - (k - deltaJ));
			visualizer.getFrmAlgo().repaint();
		}
	}

	public int shellTillIncPositive(final DrawPanel[] input, int increment) {
		while (increment > 0) {
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
			for (int i = increment; i < input.length; i++) {
				visualizer.getNumOfOperations().setText(String.valueOf(++ops));
				loopAndIncChange(input, increment, i);
			}
			increment = maintainInc(increment);
		}
		return increment;
	}

	public int maintainInc(int increment) {
		if (increment == 2) {
			visualizer.getNumOfIfs().setText(String.valueOf(++ifs));
			increment = 1;
		} else {
			visualizer.getNumOfIfs().setText(String.valueOf(++ifs));
			increment *= (5.0 / 11);
		}
		return increment;
	}

	public void loopAndIncChange(final DrawPanel[] input, int increment, int i) {
		int j = i;
		DrawPanel temp = new DrawPanel(input[i]);
		while (j >= increment && input[j - increment].getYY() < temp.getYY()) {
			pauseThreadIfNeeded();
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
			j = incCompareAndChange(input, increment, j);

		}
		input[j].setYY(temp.getYY());
		visualizer.getFrmAlgo().repaint();
	}

	public int incCompareAndChange(final DrawPanel[] input, int increment, int j) {
		int tmpFrom = input[j - increment].getXX();
		int tmpTo = input[j].getXX();
		moveLineShell(input, increment, j, tmpFrom, tmpTo);
		input[j].setYY(input[j - increment].getYY());
		input[j - increment].setXX(tmpFrom);
		j = j - increment;
		visualizer.getFrmAlgo().repaint();
		return j;
	}

	public void moveLineShell(final DrawPanel[] input, int increment, int j, int tmpFrom, int tmpTo) {
		input[j - increment].setColor(new Color(255, 254, 89));
		input[j].setColor(new Color(255, 254, 89));
		for (int k = tmpFrom; k <= tmpTo; k = (!visualizer.getChckbxRandomData().isSelected()) ? k + 1 : k + 10) {
			slowDownAndPauseIfNeeded();
			input[j - increment].setXX(k);
			visualizer.getFrmAlgo().repaint();
		}
		input[j].setColor(new Color(249, 13, 23));
		input[j - increment].setColor(new Color(3, 153, 16));
	}

	public void startCounter() {
		ops = 0;
		ifs = 0;
		visualizer.getNumOfIfs().setText(String.valueOf(0));
		visualizer.getNumOfOperations().setText(String.valueOf(0));
	}

	public void quickSort(DrawPanel[] _lines) {
		startCounter();
		this.qs = _lines;
		int length = _lines.length;
		_quickSort(0, length - 1, _lines);
		finallyColor(_lines);
	}

	private void finallyColor(DrawPanel[] _lines) {
		for (int i = 0; i < _lines.length; i++) {
			_lines[i].setColor(new Color(0, 102, 153));
			pauseThreadIfNeeded();
		}
	}

	private void _quickSort(int lowerIndex, int higherIndex, DrawPanel[] _lines) {
		int i = lowerIndex;
		int j = higherIndex;
		int pivot = qs[lowerIndex + (higherIndex - lowerIndex) / 2].getYY();
		qs[lowerIndex + (higherIndex - lowerIndex) / 2].setColor(Color.GREEN);
		while (i <= j) {
			visualizer.getNumOfOperations().setText(String.valueOf(++ops));
			while (qs[i].getYY() > pivot) {
				visualizer.getNumOfOperations().setText(String.valueOf(++ops));
				i++;
			}
			while (qs[j].getYY() < pivot) {
				visualizer.getNumOfOperations().setText(String.valueOf(++ops));
				j--;
			}
			if (i <= j) {
				swap(_lines, i, j, ifs++);
				qs[i].setColor(new Color(0, 102, 153));
				qs[j].setColor(new Color(0, 102, 153));
				i++;
				j--;
			}
		}
		if (lowerIndex < j) {
			visualizer.getNumOfIfs().setText(String.valueOf(++ifs));
			_quickSort(lowerIndex, j, _lines);
		}
		if (i < higherIndex) {
			visualizer.getNumOfIfs().setText(String.valueOf(++ifs));
			_quickSort(i, higherIndex, _lines);
		}
	}
}