package com.algo.visual.ui;

import com.algo.visual.algos.Algos;
import com.algo.visual.drawing.DrawPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.stream.Collectors;

final class AlgoRun implements ActionListener {
    private final Visualizer visualizer;

    public AlgoRun(Visualizer visualizer) {
        this.visualizer = visualizer;
    }

    public void actionPerformed(ActionEvent e) {
        if (visualizer.isRunning() && !visualizer.isToBePaused()) {
            visualizer.setToBePaused(true);
            visualizer.run.setText("Resume");
            visualizer.setRunning(false);
            Visualizer.LOGGER.log(Level.INFO, Thread.currentThread().getName() + " is paused");
            return;
        } else if (!visualizer.isRunning() && visualizer.isToBePaused()) {
            Visualizer.LOGGER.log(Level.INFO, Thread.currentThread().getName() + " is resumed");
            visualizer.setToBePaused(false);
        }
        visualizer.chckbxRandomData.setEnabled(false);
        if (visualizer.algos != null) {
            DrawPanel[] _lines = visualizer.getLines().toArray(new DrawPanel[visualizer.getLines().size()]);
            doWork(_lines, visualizer.algos);
        }
    }

    private void doWork(final DrawPanel[] _lines, Algos algo) {
        new Thread(new Runnable() {
            public void run() {
                visualizer.inputData.setEditable(false);
                visualizer.algosAvailable.setEnabled(false);
                if (!visualizer.isRunning()) {
                    visualizer.run.setText("Pause");
                    visualizer.setToBePaused(false);
                    Visualizer.LOGGER.log(Level.INFO, Thread.currentThread().getName() + " started an slgorithm");
                    visualizer.setRunning(true);
                    defineSortingAlgoAndMaintainUiLines();
                    Visualizer.LOGGER.log(Level.INFO, Thread.currentThread().getName() + " stopped an algorithm");
                    visualizer.inputData.setEditable(true);
                    visualizer.algosAvailable.setEnabled(true);
                    visualizer.getFrmAlgo().setResizable(true);
                    visualizer.run.setText("Run");
                    visualizer.run.setEnabled(false);
                    visualizer.setRunning(false);

                }

            }

            private void defineSortingAlgoAndMaintainUiLines() {
                if (algo == Algos.BUBBLE) {
                    visualizer.algoHelper.bubbleSort(visualizer.getNumOfOperations(), _lines);
                    followUp(_lines);
                } else if (algo == Algos.INSERTION) {
                    visualizer.algoHelper.innsertionSort(_lines);
                    followUp(_lines);
                } else if (algo == Algos.SHELL) {
                    visualizer.algoHelper.shell(_lines);
                    followUp(_lines);
                } else if (algo == Algos.MERGE) {
                    AtomicInteger[] _sortable = linesToYaxisData();
                    visualizer.algoHelper.mergeSort(_sortable);
                    followUp(_lines);
                } else if (algo == Algos.QUICK) {
                    visualizer.algoHelper.quickSort(_lines);
                    followUp(_lines);
                }
            }

            AtomicInteger[] linesToYaxisData() {
                List<AtomicInteger> sortable = visualizer.getLines().parallelStream().map(DrawPanel::getAtomicYReference)
                        .collect(Collectors.toList());
                return sortable.toArray(new AtomicInteger[sortable.size()]);
            }

            private void followUp(final DrawPanel[] _lines) {
                visualizer.getFrmAlgo().repaint();
                visualizer.chckbxRandomData.setEnabled(true);
            }

        }).start();
    }
}
