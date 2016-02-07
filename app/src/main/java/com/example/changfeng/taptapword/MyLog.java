package com.example.changfeng.taptapword;

/**
 * Created by changfeng on 2015/5/10.
 */

import android.util.Log;

/**
 * 对日志进行管理
 * 在DeBug模式开启，其它模式关闭
 * @author LILIN
 * 下午2:52:59
 */
public class MyLog {

    /**
     * 是否开启debug
     */
    private static boolean isDebug = false;

    public static void enableLog(boolean flag) {
        isDebug = flag;
    }

    public static boolean isLogable() {
        return isDebug;
    }


    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }


    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

}
