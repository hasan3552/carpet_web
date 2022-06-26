package com.company.util;

import java.util.Random;

public class RandomUtil {

    private static Random random = new Random();

    public static String getRandomSmsCode(){

        int n = random.nextInt(89999)+10000;

        return String.valueOf(n);
    }
}
