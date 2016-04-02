package com.example.changfeng.taptapword

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

import com.example.changfeng.taptapword.util.Utils
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class ClipboardService : Service() {

    private var cb: ClipboardManager? = null


    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cb!!.primaryClip = ClipData.newPlainText("", "")
        cb!!.addPrimaryClipChangedListener(cbListener)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        cb!!.removePrimaryClipChangedListener(cbListener)
        MyLog.d(TAG, "onDestroy()")
    }


    internal var cbListener: ClipboardManager.OnPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener {
        if (cb!!.hasPrimaryClip()) {
            for (i in 0..cb!!.primaryClip.itemCount - 1) {

                if (cb!!.primaryClip.getItemAt(i).text != null && cb!!.primaryClip.getItemAt(i).text.length > 0) {
                    if (!cb!!.primaryClip.getItemAt(i).text.toString().trim { it <= ' ' }.isEmpty()) {
                        val clipboardItem = cb!!.primaryClip.getItemAt(i).text.toString().trim { it <= ' ' }

                        if (!Utils.isEnglishWord(clipboardItem)) {
                            break
                        }
                        startActivity(intentFor<ConsultWordActivity>(clipboardText to clipboardItem, ConsultWordActivity.TYPE to ConsultWordActivity.TYPE_COPY).newTask())
                        break
                    }
                }
            }
        }
    }
    companion object {

        private val TAG = "ClipboardService"

        val clipboardText = "clipboardText"
    }
}
