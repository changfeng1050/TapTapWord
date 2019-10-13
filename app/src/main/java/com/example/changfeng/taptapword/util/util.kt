package com.example.changfeng.taptapword.util

import java.util.regex.Pattern

/**
 * Created by chang on 2017-07-23.
 */

fun String.isEnglishWord(): Boolean {
    // 判断是否有汉字
    val array = this.toCharArray()

//    if (array.any { it.toByte().toChar() != it }) {
//        LogUtils.LOGI("", "isEnglishWord() found not english() $this")
//        return false
//    }
    if (array.any { it.isChinese() }){
        return false
    }

    // 判断是是否是网址
    val httpPattern = Pattern.compile("(http://|https://)+")

    if (httpPattern.matcher(this).find()) {
        return false
    }
    return true
}

fun Char.isChinese(): Boolean {
    return this.toInt() in 19969..171941
}