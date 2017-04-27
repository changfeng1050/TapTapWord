package com.example.changfeng.taptapword

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.changfeng.taptapword.util.LogUtils
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import java.util.regex.Pattern

class ClipboardService : Service(), ClipboardManager.OnPrimaryClipChangedListener {

    val cb: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreate() {
        super.onCreate()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = Notification.Builder(this)
            .setSmallIcon(R.drawable.image_ninja)
            .setTicker(getString(R.string.notification_service_intent_description))
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
            .setContentInfo("this is a context").build()
        startForeground(1, notification)
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        LogUtils.LOGI(TAG, "onCreate()")
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cb.addPrimaryClipChangedListener(this)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        cb.removePrimaryClipChangedListener(this)
    }


    override fun onPrimaryClipChanged() {
        if (!cb.hasPrimaryClip()) {
            return
        }

        for (i in 0..cb.primaryClip.itemCount - 1) {
            val item = cb.primaryClip.getItemAt(i) ?: return

            val text = item.text.toString()

            if (!text.isNullOrEmpty()) {
                if (text.contains("changfeng")) {
                    continue
                }

                if (!text.isEnglishWord()) {
                    continue
                }
                startActivity(intentFor<ConsultWordActivity>(clipboardText to text, ConsultWordActivity.TYPE to ConsultWordActivity.TYPE_COPY).newTask())
                return
            }
        }
    }

    fun String.isEnglishWord(): Boolean {
        // 判断是否有汉字
        val array = this.toCharArray()

        if (array.any { it.toByte().toChar() != it }) {
            return false
        }


        // 判断是否有数字
        val digitPattern = Pattern.compile("[\\+\\-=0-9.\\(\\)<>\\|\\[\\]\\s\\\\!\\?@#\\$%\\^&\\*,\\./~`]+")
        if (digitPattern.matcher(this).matches()) {
            return false
        }
        // 判断是是否是网址
        val httpPattern = Pattern.compile("(http://|https://)+")

        if (httpPattern.matcher(this).find()) {
            return false
        }
        return true
    }

    companion object {
        private val TAG = "ClipboardService"
        val clipboardText = "clipboardText"
    }
}
