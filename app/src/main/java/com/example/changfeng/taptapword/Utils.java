package com.example.changfeng.taptapword;

import java.util.regex.Pattern;

/**
 * Created by changfeng on 2015/12/28.
 */
public class Utils {
    public static String getToken() {
        return String.valueOf((int) (Math.random() * 1000000) % 1000000);
    }

    public static Boolean isEnglishWord(String word) {
        // 判断是否有汉字
        char[] array = word.toCharArray();
        for (char a : array) {
            if ((char) (byte) a != a) {
                return false;
            }
        }

        // 判断是否有数字
        Pattern digitPattern = Pattern.compile("[\\+\\-=0-9.\\(\\)<>\\|\\[\\]\\s\\\\!\\?@#\\$%\\^&\\*,\\./~`]+");
        if (digitPattern.matcher(word).matches()) {
            return false;
        }
        // 判断是是否是网址
        Pattern httpPattern = Pattern.compile("(http://|https://)+");
        if (httpPattern.matcher(word).find()) {
            return false;
        }
        return true;
    }
}
