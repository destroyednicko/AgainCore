package com.nicko.core.util;

import java.util.Random;


public class RandomUtils {

    public static Random RANDOM = new Random();

    public static int getRandomNumber(int max) {
        return RANDOM.nextInt(max);
    }

}
