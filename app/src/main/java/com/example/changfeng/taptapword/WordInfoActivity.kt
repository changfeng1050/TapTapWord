package com.example.changfeng.taptapword

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.example.changfeng.taptapword.util.LogUtils
import com.umeng.analytics.MobclickAgent
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast


class WordInfoActivity : Activity() {

    val wordNameEditText: TextView get() = find(R.id.word_name)
    val dateTimeTextView: TextView get() = find(R.id.date_time)
    val archiveCheckBox: CheckBox get() = find(R.id.archive)
    val phoneLabelTextView: TextView get() = find(R.id.phone_label)
    val phoneTextView: TextView get() = find(R.id.phone)
    val meansLabelTextView: TextView get() = find(R.id.means_label)
    val meansTextView: TextView get() = find(R.id.means)
    val webExplainsLabelTextView: TextView get() = find(R.id.web_explains_label)
    val webExplainsTextView: TextView get() = find(R.id.web_explains)
    val noteEditText: EditText get() = find(R.id.note)


    var word: Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)

        find<TextView>(R.id.save).onClick {
            updateWord()
            WordManger.get(this@WordInfoActivity).updateWord(word)
            toast(R.string.message_save_success)
            finish()
        }

        find<TextView>(R.id.cancel).onClick {
            finish()
        }

        val wordName = intent.getStringExtra(EXTRA_WORD_NAME)
        word = WordManger.get(this).getWord(wordName)

        val w = word

        if (w != null) {
            wordNameEditText.text = w.name
            dateTimeTextView.text = "${w.year}-${w.month}-${w.date} ${w.hour}:${w.minute}:${w.second}"
            archiveCheckBox.isChecked = w.isArchived
            phoneTextView.text = w.formatPhones
            meansTextView.text = w.means
            webExplainsTextView.text = w.webExplains
            noteEditText.setText(w.note)

            if (w.formatPhones.isNullOrEmpty()) {
                phoneLabelTextView.visibility = View.GONE
                phoneTextView.visibility = View.GONE
            }

            if (w.means.isNullOrEmpty()) {
                meansLabelTextView.visibility = View.GONE
                meansTextView.visibility = View.GONE
            }

            if (w.webExplains.isNullOrEmpty()) {
                webExplainsLabelTextView.visibility = View.GONE
                webExplainsTextView.visibility = View.GONE
            }

        }
    }


    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("单词详情界面")
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
        MobclickAgent.onPageEnd("单词详情界面")
    }

    private fun updateWord() {
        word?.isArchived = archiveCheckBox.isChecked
        word?.means = meansTextView.text.toString()
        word?.note = noteEditText.text.toString()
    }


    companion object {

        private val TAG: String = LogUtils.makeLogTag(WordInfoActivity::class.java)

        val EXTRA_WORD_NAME = "word_name"
    }
}
