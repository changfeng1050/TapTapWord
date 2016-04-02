package com.example.changfeng.taptapword

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.changfeng.taptapword.net.ApiClient
import com.example.changfeng.taptapword.util.Utils
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ConsultWordActivity : Activity() {
    private var type = TYPE_CONSULT

    var baiduResult: com.example.changfeng.taptapword.net.result.BaiduResult ?= null
    var youdaoResult: com.example.changfeng.taptapword.net.result.YoudaoResult ?= null
    var query: String? = null

    var foundResult = false


    var wordConsultLayout: LinearLayout ?= null
    var wordEditText: TextView ?= null
    var addNewTextView: TextView ?=null
    var resultInfoTextView: TextView ?= null
    var wordResultLayout: LinearLayout ? = null
    var wordNameTextView: TextView ? = null
    var wordPhonesTextView: TextView ?= null
    var wordMeansTextView: TextView ?= null
    var wordWebExplainsTextView: TextView ?= null
    var youdaoTranslateTextView: TextView ?= null
    var baiduTranslateTextView: TextView ?= null
    var addNoteTextView: TextView ?= null
    var noteEditText: TextView ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_consult_word)

        wordConsultLayout = find<LinearLayout>(R.id.word_consult_layout)
        wordEditText = find<EditText>(R.id.word_edit_text)
        addNewTextView = find<TextView>(R.id.add_new_word_text_view)
        addNewTextView?.onClick {
            resetViews()
            val word = wordEditText?.text.toString()
            resultInfoTextView?.visibility = View.VISIBLE
            if (Utils.isEnglishWord(word)) {
                resultInfoTextView?.text = getString(R.string.requesting_result_from_internet)
                getResult(word)
            } else {
                resultInfoTextView?.text = getString(R.string.message_not_support_other_language)
            }
        }
        resultInfoTextView = find<TextView>(R.id.result_info_text_view)
        wordResultLayout = find<LinearLayout>(R.id.word_result_layout)
        wordNameTextView = find<TextView>(R.id.word_name_text_view)
        wordPhonesTextView = find<TextView>(R.id.word_phones_text_view)
        wordMeansTextView = find<TextView>(R.id.word_means_text_view)
        wordWebExplainsTextView = find<TextView>(R.id.word_web_explains_text_view)
        youdaoTranslateTextView = find<TextView>(R.id.youdao_translate_text_view)
        baiduTranslateTextView = find<TextView>(R.id.baidu_translate_text_view)
        addNoteTextView = find<TextView>(R.id.add_note_text_view)
        addNoteTextView?.onClick {
            addNoteTextView?.visibility = View.GONE
            noteEditText?.visibility = View.VISIBLE
        }
        noteEditText = find<TextView>(R.id.note_edit_text);

        type = intent.getIntExtra(TYPE, TYPE_CONSULT)
        if (type == TYPE_COPY) {
            wordConsultLayout?.visibility = View.GONE
            resultInfoTextView?.visibility = View.VISIBLE
            resultInfoTextView?.text = getString(R.string.requesting_result_from_internet)
            val clip = intent.getStringExtra(ClipboardService.clipboardText).trim { it <= ' ' }
            getResult(clip)
        }

        updateViewVisibility()
    }

    override fun onDestroy() {
        if (foundResult) {
            saveWord()
        }
        super.onDestroy()
    }

    private fun getResult(query: String) {
        foundResult = false
        this.query = query
        val apiClient = ApiClient()
        val pref = getSharedPreferences(SharedPref.NAME, Context.MODE_PRIVATE)
        if (pref.getBoolean(SharedPref.BAIDU_TRANSLATE, true)) {
            apiClient.getBaiduResult(query, object : Callback<com.example.changfeng.taptapword.net.result.BaiduResult> {
                override fun onResponse(call: Call<com.example.changfeng.taptapword.net.result.BaiduResult>, response: Response<com.example.changfeng.taptapword.net.result.BaiduResult>) {
                    baiduResult = response.body()
                    baiduTranslateTextView?.text = baiduResult?.result
                    updateViewVisibility()
                    foundResult = true
                }

                override fun onFailure(call: Call<com.example.changfeng.taptapword.net.result.BaiduResult>, t: Throwable) {
                    resultInfoTextView?.text = getString(R.string.message_cannot_find_word_result)
                }
            })
        }

        apiClient.getYoudaoResult(query, object : Callback<com.example.changfeng.taptapword.net.result.YoudaoResult> {
            override fun onResponse(call: Call<com.example.changfeng.taptapword.net.result.YoudaoResult>, response: Response<com.example.changfeng.taptapword.net.result.YoudaoResult>) {
                youdaoResult = response.body()
                resultInfoTextView?.text = ""
                if (pref.getBoolean(SharedPref.YOUDAO_DICT, true)) {
                    try {
                        wordPhonesTextView?.text = youdaoResult?.formatPhones
                        wordMeansTextView?.text = youdaoResult?.parsedExplains
                    } catch (e: Exception) {

                    }

                }

                if (pref.getBoolean(SharedPref.WEB_EXPLAIN, true)) {
                    try {
                        wordWebExplainsTextView?.text = youdaoResult?.parseWebTranslate
                    } catch (e: Exception) {

                    }

                }

                if (pref.getBoolean(SharedPref.YOUDAO_TRANSLATE, true)) {
                    try {
                        youdaoTranslateTextView?.text = youdaoResult?.translateResult
                    } catch (e: Exception) {

                    }

                }
                updateViewVisibility()
                foundResult = true
            }

            override fun onFailure(call: Call<com.example.changfeng.taptapword.net.result.YoudaoResult>, t: Throwable) {
                resultInfoTextView?.text = getString(R.string.message_cannot_find_word_result)
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
        word.note = noteEditText?.text.toString()

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
        wordNameTextView?.visibility = if(wordNameTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
        wordPhonesTextView?.visibility = if (wordPhonesTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
        wordMeansTextView?.visibility = if (wordMeansTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
        wordWebExplainsTextView?.visibility = if (wordWebExplainsTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
        youdaoTranslateTextView?.visibility = if (youdaoTranslateTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
        baiduTranslateTextView?.visibility = if (baiduTranslateTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
        addNewTextView?.visibility = if (addNoteTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
        resultInfoTextView?.visibility = if (resultInfoTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
        addNoteTextView?.visibility = if (wordPhonesTextView?.text!!.isEmpty() && wordMeansTextView?.text!!.isEmpty() && youdaoTranslateTextView?.text!!.isEmpty() && baiduTranslateTextView?.text!!.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun resetViews() {
        wordNameTextView?.text = ""
        wordPhonesTextView?.text = ""
        wordMeansTextView?.text = ""
        wordWebExplainsTextView?.text = ""
        youdaoTranslateTextView?.text = ""
        baiduTranslateTextView?.text = ""
        noteEditText?.text= ""
        resultInfoTextView?.text = ""
        updateViewVisibility()
    }

    companion object {

        private val TAG = "ConsultWordActivity"

        val TYPE = "type"
        val TYPE_CONSULT = 1
        val TYPE_COPY = 2
    }


}
