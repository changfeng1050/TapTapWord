package com.example.changfeng.taptapword

import android.app.Notification
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.changfeng.taptapword.util.LogUtils
import com.example.changfeng.taptapword.util.isEnglishWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import android.app.NotificationManager
import android.R
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService


class ClipboardService : Service(), ClipboardManager.OnPrimaryClipChangedListener {

    private val cb: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.LOGI(TAG, "onCreate()")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground()
        } else {
            startForeground(1, Notification())
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cb.addPrimaryClipChangedListener(this)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        LogUtils.LOGI(TAG, "onDestroy()")
        cb.removePrimaryClipChangedListener(this)
        super.onDestroy()
    }


    override fun onPrimaryClipChanged() {
        LogUtils.LOGI(TAG, "onPrimaryClipChanged() ${cb.hasPrimaryClip()}")
        if (!cb.hasPrimaryClip()) {
            return
        }

        for (i in 0 until cb.primaryClip!!.itemCount) {
            LogUtils.LOGI(TAG, "onPrimaryClipChanged: ${cb.primaryClip!!.getItemAt(i).text}")
        }

        for (i in 0 until cb.primaryClip!!.itemCount) {
            val item = cb.primaryClip!!.getItemAt(i)
            val text = item.text.toString()

            if (!text.isNullOrEmpty()) {
                if (text.contains("changfeng")) {
                    continue
                }

                if (!text.isEnglishWord()) {
                    LogUtils.LOGI(TAG, "onPrimaryClipChanged() $text is not english word")
                    continue
                }
                LogUtils.LOGI(TAG, "onPrimaryClipChanged() start activity")
                startActivity(intentFor<ConsultWordActivity>(clipboardText to text, ConsultWordActivity.TYPE to ConsultWordActivity.TYPE_COPY).newTask())
                return
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val channelName = "My Background Service"
        val chan = NotificationChannel(notificationId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(this, notificationId)
        val notification = notificationBuilder.setOngoing(true)
                .setContentTitle("单词忍者")
                .setContentText("单词忍者正在后台运行")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        startForeground(2, notification)
    }

    companion object {
        private const val TAG = "ClipboardService"
        var notificationId = "channelId_tap_tap_word"
        const val clipboardText = "clipboardText"
    }
}
