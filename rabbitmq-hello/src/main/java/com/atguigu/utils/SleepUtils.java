package com.atguigu.utils;

/**
 * SleepUtils
 *
 * @author fj
 * @date 2022/10/20 11:17
 */
public class SleepUtils {
    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
