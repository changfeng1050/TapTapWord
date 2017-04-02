package com.example.changfeng.taptapword.util

/**
 * Created by changfeng on 2015/5/10.
 */

import android.util.Log

/**
 * 对日志进行管理
 * 在DeBug模式开启，其它模式关闭
 * @author LILIN
 * * 下午2:52:59
 */
object MyLog {

    /**
     * 是否开启debug
     */
    var isLogable = false
        private set

    fun enableLog(flag: Boolean) {
        isLogable = flag
    }


    fun e(tag: String, msg: String) {
        if (isLogable) {
            Log.e(tag, msg)
        }
    }


    fun i(tag: String, msg: String) {
        if (isLogable) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (isLogable) {
            Log.w(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (isLogable) {
            Log.d(tag, msg)
        }
    }

}
