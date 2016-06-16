package com.jmelzer.jitty.utl;

/**
 * Created by J. Melzer on 16.06.2016.
 * Nice methods
 */
public class RandomUtil {

    public static int randomIntFromInterval(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
