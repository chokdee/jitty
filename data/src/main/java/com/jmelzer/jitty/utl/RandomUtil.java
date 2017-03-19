package com.jmelzer.jitty.utl;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by J. Melzer on 16.06.2016.
 * Nice methods
 */
public class RandomUtil {

    ArrayList<Integer> list = new ArrayList<>();

    public RandomUtil(int min, int max) {
        for (int i = min; i <= max; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
    }

    public static int randomIntFromInterval(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public int nextInt() {
        int n = list.get(0);
        list.remove(0);
        return n;
    }

    public boolean hasMoreNumbers() {
        return !list.isEmpty();
    }
}
