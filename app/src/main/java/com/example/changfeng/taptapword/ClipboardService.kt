package com.example.changfeng.taptapword

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Parcel
import android.widget.Toast
import com.example.changfeng.taptapword.util.LogUtils

import com.example.changfeng.taptapword.util.Utils
import org.jetbrains.anko.clipboardManager
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class ClipboardService : Service() {

    var cb: ClipboardManager? = null


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
        LogUtils.LOGI(TAG, "onStartCommand()")
        cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cb!!.primaryClip = ClipData.newPlainText("", "")
        cb!!.addPrimaryClipChangedListener(cbListener)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        cb?.removePrimaryClipChangedListener(cbListener)
        LogUtils.LOGI(TAG, "onDestroy()")
    }


    internal var cbListener: ClipboardManager.OnPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener {
        if (cb == null || !cb!!.hasPrimaryClip()) {
            return@OnPrimaryClipChangedListener
        }
        for (i in 0..cb!!.primaryClip.itemCount - 1) {
            val item = try {
                cb!!.primaryClip.getItemAt(i).text.toString()
            } catch(e: NullPointerException) {
                ""
            }
            if (!item.isNullOrBlank()) {
                if (!Utils.isEnglishWord(item)) {
                    break
                }
                if (clipboardText.contains("changfeng")) {
                    return@OnPrimaryClipChangedListener
                }
                startActivity(intentFor<ConsultWordActivity>(clipboardText to item, ConsultWordActivity.TYPE to ConsultWordActivity.TYPE_COPY).newTask())
                break
            }
        }
    }

    companion object {
        private val TAG = "ClipboardService"
        val clipboardText = "clipboardText"
    }
}
