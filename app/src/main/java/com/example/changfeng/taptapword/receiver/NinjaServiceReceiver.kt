package com.example.changfeng.taptapword.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import com.example.changfeng.taptapword.ClipboardService
import com.example.changfeng.taptapword.MainActivity
import com.example.changfeng.taptapword.R
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.notificationManager
import org.jetbrains.anko.toast

/**
 * Created by changfeng on 2016/4/2.
 */
class NinjaServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == MainActivity.NINJA_BROADCAST) {
            val ninjaOpenFlag = intent?.getBooleanExtra(MainActivity.NINJA_OPEN_FLAG, true)
            when (ninjaOpenFlag) {
                true -> {
                    updateNotification(context!!,"点击开启忍者监听", false)
                    stopClipboardService(context)
                    context.toast("单词忍者停止监听")
                }
                else -> {
                    updateNotification(context!!,"点击停止忍者监听", true)
                    startClipboardService(context)
                    context.toast("单词忍者正在监听")
                }

            }
        }
    }

    fun startClipboardService(context: Context) {
        stopClipboardService(context)
        context.startService(context.intentFor<ClipboardService>())
    }

    internal fun stopClipboardService(context: Context) {
        context.stopService(context.intentFor<ClipboardService>())
    }

    fun updateNotification(context: Context, text: String, opened: Boolean) {
        val ninjaServiceNotificationBuilder = NotificationCompat.Builder(context).setSmallIcon(R.drawable.image_ninja).setContentTitle(context.getString(R.string.app_name)).setContentText(text)
        val ninjaIntent = PendingIntent.getBroadcast(context, AppCompatActivity.RESULT_OK, Intent(MainActivity.NINJA_BROADCAST).putExtra(MainActivity.NINJA_OPEN_FLAG, opened), PendingIntent.FLAG_CANCEL_CURRENT)
        ninjaServiceNotificationBuilder.setContentIntent(ninjaIntent)
        context.notificationManager.notify(2, ninjaServiceNotificationBuilder.build())
    }
}