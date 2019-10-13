package com.example.changfeng.taptapword.util;

/**
 * Created by changfeng on 2015/12/28.
 */
public class Utils {
    public static String getToken() {
        return String.valueOf((int) (Math.random() * 1000000) % 1000000);
    }
}
