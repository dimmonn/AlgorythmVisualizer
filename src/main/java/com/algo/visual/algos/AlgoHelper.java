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
        if (helper == null) {
            this._sortable = _sortable;
            helper = new AtomicInteger[_sortable.length];
        }
        _mergesort(0, _sortable.length - 1);
        helper = null;
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
        for (int i = low; i <= high; i++) {
            helper[i] = new AtomicInteger(_sortable[i].get());
            visualizer.getNumOfOperations().setText(String.valueOf(++ops));
        }
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

    private int injectValue(int j, int k) {
        slowDown();
        _sortable[k].set(helper[j].get());
        visualizer.getNumOfIfs().setText(String.valueOf(++swaps));
        getKline(k).setColor(Color.BLACK);
        j++;
        visualizer.getFrmAlgo().repaint();
        return j;
    }

    private DrawPanel getKline(int k) {
        return visualizer.getLines().toArray(new DrawPanel[visualizer.getLines().size()])[k];
    }

    public void bubbleSort(JTextArea numOfOperations, final DrawPanel[] _lines) {
        for (int i = 0; i < _lines.length - 1; i++) {
            for (int j = 0; j < _lines.length - 1; j++) {
                numOfOperations.setText(String.valueOf(++ops));
                if (_lines[j].getHHeight() > _lines[j + 1].getHHeight()) {
                    swap(_lines, j, j + 1, swaps++);
                }
            }
            _lines[_lines.length - 1 - i].setColor(Color.BLACK);
        }
        ops = 0;
        swaps = 0;
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
            atomicInteger.set(k);
            atomicInteger2.set(deltaI - (k - deltaJ));
            visualizer.getFrmAlgo().repaint();
            slowDown();
        }
    }

}
