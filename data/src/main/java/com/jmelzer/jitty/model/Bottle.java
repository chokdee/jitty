package com.jmelzer.jitty.model;

/**
 * Created by J. Melzer on 19.04.2016.
 */
public class Bottle {
    public static void main(String[] args) {
        int bottles = 99;
        String text = "99 bottles";

        while (bottles != 0) {
            System.out.println(text + " of beer on the wall,");
            System.out.println(text + " of beer.");
            System.out.println("Take one down, pass it around,");

            if (--bottles == 1) {
                text = "1 bottle";
            } else {
                text = String.format("%d bottles", bottles);
            }
            System.out.println(text + " of beer on the wall.\n");
        }
    }
}
