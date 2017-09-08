package com.algo.visual.algos;

import com.algo.visual.ui.Visualizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class AlgoHelperTest {

    private AtomicInteger[] _sortable;
    private AlgoHelper algoHelper;

    @Before
    public void setUp() {
        _sortable = new AtomicInteger[6];
        AtomicInteger _tmp0 = new AtomicInteger();
        _tmp0.set(4);
        AtomicInteger _tmp1 = new AtomicInteger();
        _tmp1.set(1);
        AtomicInteger _tmp2 = new AtomicInteger();
        _tmp2.set(2);
        AtomicInteger _tmp3 = new AtomicInteger();
        _tmp3.set(6);
        AtomicInteger _tmp4 = new AtomicInteger();
        _tmp4.set(20);
        AtomicInteger _tmp5 = new AtomicInteger();
        _tmp5.set(0);
        _sortable[0] = _tmp0;
        _sortable[1] = _tmp1;
        _sortable[2] = _tmp2;
        _sortable[3] = _tmp3;
        _sortable[4] = _tmp4;
        _sortable[5] = _tmp5;

        algoHelper = new AlgoHelper(new Visualizer());
    }

    @Test
   public void mergeSort() {
        algoHelper.mergeSort(_sortable);
        Assert.assertEquals(20, _sortable[0].get());
        Assert.assertEquals(6, _sortable[1].get());
        Assert.assertEquals(4, _sortable[2].get());
        Assert.assertEquals(2, _sortable[3].get());
        Assert.assertEquals(1, _sortable[4].get());
        Assert.assertEquals(0, _sortable[5].get());

    }
}