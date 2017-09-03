package com.algo.visual.algos;

import com.algo.visual.ui.Visualizer;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

class AlgoHelperTest {

    private AtomicInteger[] _sortable;
    private AlgoHelper algoHelper;

    @BeforeEach
    void setUp() {
        _sortable = new AtomicInteger[6];
        for (int i = 0; i < _sortable.length; i++) {
            AtomicInteger _tmp = new AtomicInteger();
            _tmp.set(i);
            _sortable[i] = _tmp;
        }
        algoHelper = new AlgoHelper(new Visualizer());
    }

    @Test
    void mergeSort() {
        algoHelper.mergeSort(_sortable);
        Assert.assertEquals(5, _sortable[0].get());
        Assert.assertEquals(4, _sortable[1].get());
        Assert.assertEquals(3, _sortable[2].get());
        Assert.assertEquals(2, _sortable[3].get());
        Assert.assertEquals(1, _sortable[4].get());
        Assert.assertEquals(0, _sortable[5].get());

    }
}