package com.example.changfeng.taptapword

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.example.changfeng.taptapword.net.ApiClient
import com.example.changfeng.taptapword.net.result.BaiduResult
import com.example.changfeng.taptapword.net.result.YoudaoResult
import com.example.changfeng.taptapword.util.LogUtils
import com.example.changfeng.taptapword.util.isEnglishWord
import com.umeng.analytics.MobclickAgent
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.find
import org.jetbrains.anko.notificationManager
import org.jetbrains.anko.sdk27.coroutines.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ConsultWordActivity : Activity() {
    private var type = TYPE_CONSULT

    var baiduResult: BaiduResult? = null
    var youdaoResult: YoudaoResult? = null
    var query: String? = null

    var foundResult = false


    val wordConsultLayout: LinearLayout get() = find(R.id.word_consult_layout)
    val wordEditText: TextView get() = find(R.id.word_edit_text)
    val addNewTextView: TextView get() = find(R.id.add_new_word_text_view)
    val resultInfoTextView: TextView get() = find(R.id.result_info_text_view)
    val wordResultLayout: LinearLayout get() = find(R.id.word_result_layout)
    val wordNameTextView: TextView get() = find(R.id.word_name_text_view)
    val wordPhonesTextView: TextView get() = find(R.id.word_phones_text_view)
    val wordMeansTextView: TextView get() = find(R.id.word_means_text_view)
    val wordWebExplainsTextView: TextView get() = find(R.id.word_web_explains_text_view)
    val youdaoTranslateTextView: TextView get() = find(R.id.youdao_translate_text_view)
    val baiduTranslateTextView: TextView get() = find(R.id.baidu_translate_text_view)
    val addNoteTextView: TextView get() = find(R.id.add_note_text_view)
    val noteEditText: TextView get() = find(R.id.note_edit_text)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.LOGI(TAG, "onCreate()")
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_consult_word)
        addNewTextView.onClick {
            resetViews()
            val word = wordEditText.text.toString()
            resultInfoTextView.visibility = View.VISIBLE
            if (word.isEnglishWord()) {
                resultInfoTextView.text = getString(R.string.requesting_result_from_internet)
                getResult(word)
            } else {
                resultInfoTextView.text = getString(R.string.message_not_support_other_language)
            }
        }

        addNoteTextView.onClick {
            addNoteTextView.visibility = View.GONE
            noteEditText.visibility = View.VISIBLE
        }

        type = intent.getIntExtra(TYPE, TYPE_CONSULT)


        if (type == TYPE_COPY) {
            wordConsultLayout.visibility = View.GONE
            resultInfoTextView.visibility = View.VISIBLE
            resultInfoTextView.text = getString(R.string.requesting_result_from_internet)
            val clip = intent.getStringExtra(ClipboardService.clipboardText).trim()
            getResult(clip)
        }

        updateViewVisibility()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("网络查询单词页面")
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        if (defaultSharedPreferences.getBoolean(SharedPref.NOTIFICATION_NEW_WORD, true) && !wordNameTextView.text.isNullOrEmpty()) {
            val notificationBuilder = NotificationCompat.Builder(this).setSmallIcon(R.drawable.image_ninja).setContentTitle(getString(R.string.app_name)).setContentText("新单词 ${wordNameTextView.text.toString()}")
            val i = Intent(this, MainActivity::class.java).putExtra(MainActivity.NINJA_NEW_WORD, true)
            val pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT)
            notificationBuilder.setContentIntent(pi)
            notificationManager.notify(1, notificationBuilder.build())
        }

        MobclickAgent.onPageEnd("网络查询单词页面")
        MobclickAgent.onPause(this)
    }


    override fun onDestroy() {
        if (foundResult) {
            saveWord()
        }

        super.onDestroy()
    }

    private fun getResult(query: String) {
        LogUtils.LOGI(TAG, "getResult() $query")
        foundResult = false
        this.query = query
        val apiClient = ApiClient()
        val pref = defaultSharedPreferences
        if (pref.getBoolean(SharedPref.BAIDU_TRANSLATE, true)) {
            apiClient.getBaiduResult(query, object : Callback<BaiduResult> {
                override fun onResponse(call: Call<BaiduResult>, response: Response<BaiduResult>) {
                    baiduResult = response.body()
                    baiduTranslateTextView.text = baiduResult?.result
                    updateViewVisibility()
                    foundResult = true
                }

                override fun onFailure(call: Call<BaiduResult>, t: Throwable) {
                    t.printStackTrace()
                    resultInfoTextView.text = getString(R.string.message_cannot_find_word_result)
                }
            })
        }

        apiClient.getYoudaoResult(query, object : Callback<YoudaoResult> {
            override fun onResponse(call: Call<YoudaoResult>, response: Response<YoudaoResult>) {

                youdaoResult = response.body()
                wordNameTextView.text = query
                resultInfoTextView.text = ""
                if (pref.getBoolean(SharedPref.YOUDAO_DICT, true)) {
                    try {
                        LogUtils.LOGI(TAG, "onResponse() $query  phone:${youdaoResult?.formatPhones}")
                        wordPhonesTextView.text = youdaoResult?.formatPhones
                        wordMeansTextView.text = youdaoResult?.parsedExplains
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                if (pref.getBoolean(SharedPref.WEB_EXPLAIN, true)) {
                    try {
                        wordWebExplainsTextView.text = youdaoResult?.parseWebTranslate
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                if (pref.getBoolean(SharedPref.YOUDAO_TRANSLATE, true)) {
                    try {
                        youdaoTranslateTextView.text = youdaoResult?.translateResult
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                updateViewVisibility()
                foundResult = true
            }

            override fun onFailure(call: Call<YoudaoResult>, t: Throwable) {
                t.printStackTrace()
                resultInfoTextView.text = getString(R.string.message_cannot_find_word_result)
            }
        })


    }

    private fun saveWord() {
        if (query == null || query!!.isEmpty()) {
            return
        }
        val word = Word()
        word.language = "English"
        word.name = query
        word.enPhone = youdaoResult?.ukPhonetic
        word.amPhone = youdaoResult?.usPhonetic
        try {
            word.means = youdaoResult?.parsedExplains
        } catch (expected: NullPointerException) {
            return
        }

        word.webExplains = youdaoResult?.parseWebTranslate
        word.isArchived = false
        word.note = noteEditText.text.toString()

        val c = Calendar.getInstance()
        word.year = c.get(Calendar.YEAR)
        word.month = c.get(Calendar.MONTH) + 1
        word.date = c.get(Calendar.DAY_OF_MONTH)
        word.hour = c.get(Calendar.HOUR_OF_DAY)
        word.minute = c.get(Calendar.MINUTE)
        word.second = c.get(Calendar.SECOND)

        WordManger.get(applicationContext).insertWord(word)
    }

    private fun updateViewVisibility() {
        wordNameTextView.visibility = if (wordNameTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
        wordPhonesTextView.visibility = if (wordPhonesTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
        wordMeansTextView.visibility = if (wordMeansTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
        wordWebExplainsTextView.visibility = if (wordWebExplainsTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
        youdaoTranslateTextView.visibility = if (youdaoTranslateTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
        baiduTranslateTextView.visibility = if (baiduTranslateTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
        addNewTextView.visibility = if (addNoteTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
        resultInfoTextView.visibility = if (resultInfoTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
        addNoteTextView.visibility = if (wordPhonesTextView.text!!.isEmpty() && wordMeansTextView.text!!.isEmpty() && youdaoTranslateTextView.text!!.isEmpty() && baiduTranslateTextView.text!!.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun resetViews() {
        wordNameTextView.text = ""
        wordPhonesTextView.text = ""
        wordMeansTextView.text = ""
        wordWebExplainsTextView.text = ""
        youdaoTranslateTextView.text = ""
        baiduTranslateTextView.text = ""
        noteEditText.text = ""
        resultInfoTextView.text = ""
        updateViewVisibility()
    }

    companion object {

        private const val TAG = "ConsultWordActivity"

        const val TYPE = "type"
        const val TYPE_CONSULT = 1
        const val TYPE_COPY = 2
    }


}
