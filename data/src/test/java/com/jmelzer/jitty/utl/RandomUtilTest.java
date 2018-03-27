/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.utl;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 27.03.2018.
 */
public class RandomUtilTest {


    @Test
    public void randomIntFromInterval() {

        for (int i = 0; i < 20; i++) {
            int n = RandomUtil.randomIntFromInterval(0, 10);
            assertTrue("must be between 0 and 10 not " + n, n > -1 && n < 11);
        }
    }

    @Test
    public void nextInt() {
        RandomUtil randomUtil = new RandomUtil(0, 10);
        for (int i = 0; i < 11; i++) {
            assertTrue(randomUtil.hasMoreNumbers());
            int n = randomUtil.nextInt();
            assertTrue("must be between 0 and 10 not " + n, n > -1 && n < 11);
        }
        assertFalse(randomUtil.hasMoreNumbers());
    }

}