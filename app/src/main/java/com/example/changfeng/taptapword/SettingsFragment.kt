package com.example.changfeng.taptapword


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast


class SettingsFragment : Fragment(), View.OnClickListener {
    val youdaoDictSwitch: Switch get() = find(R.id.youdao_dict_switch)
    val webExplainSwitch: Switch get() = find(R.id.web_explain_switch)
    val youdaoTranslateSwitch: Switch get() = find(R.id.youdao_translate_switch)
    val baiduTranslateSwitch: Switch get() = find(R.id.baidu_translate_switch)
    val backupDataTextView: TextView get() = find(R.id.backup_data)
    val restoreDataTextView: TextView get() = find(R.id.restore_data)
    val clearBackupDataTextView: TextView get() = find(R.id.clear_backup_data)
    val ninjaWatchNotificationSwitch: Switch get() = find(R.id.notification_ninja_watch)
    val newWordNotificationSwitch: Switch get() = find(R.id.notification_new_word)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backupDataTextView.setOnClickListener(this)
        restoreDataTextView.setOnClickListener(this)
        clearBackupDataTextView.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        readSettings()
    }

    override fun onPause() {
        super.onPause()
        saveSettings()
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.backup_data -> {
                requireActivity().alert(R.string.message_backup_data, R.string.backup) {
                    yesButton {
                        if (WordManger.get(act).copyDbToSdcard()) {
                            toast(R.string.message_data_backupped)
                        } else {
                            toast(R.string.message_data_backupped_failed)
                        }
                    }
                    noButton { }
                }.show()
            }

            R.id.restore_data -> {
                requireActivity().alert(R.string.message_restore_data, R.string.restore) {
                    yesButton {
                        startActivityForResult(requireActivity().intentFor<FileListActivity>(FILE_KEY_WORD to "word_ninja"), REQUEST_BACKUP_FILE)
                    }
                    noButton { }
                }.show()
            }
            R.id.clear_backup_data ->
                requireActivity().alert(R.string.message_clear_backup_data, R.string.clear_backup_data) {
                    yesButton {
                        clearAllBackupData()
                        toast(R.string.message_all_backup_data_cleared)
                    }
                    noButton {  }
                }.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_BACKUP_FILE -> if (WordManger.get(activity).restoreDb(data!!.extras!!.getString(FileListActivity.FILE_KEY_WORD))) {
                toast(R.string.message_data_restored)
            } else {
                toast(R.string.message_data_restored_failed)
            }
            else -> {
            }
        }
    }

    private fun clearAllBackupData() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val path = Environment.getExternalStorageDirectory()
            path.listFiles { _, s -> s.contains(FILE_KEY_WORD) }.filter { it -> it.isFile }.forEach { it.delete() }
        }
    }

    private fun readSettings() {
        val pref = requireActivity().defaultSharedPreferences
        youdaoDictSwitch.isChecked = pref.getBoolean(SharedPref.YOUDAO_DICT, true)
        webExplainSwitch.isChecked = pref.getBoolean(SharedPref.WEB_EXPLAIN, true)
        youdaoTranslateSwitch.isChecked = pref.getBoolean(SharedPref.YOUDAO_TRANSLATE, true)
        baiduTranslateSwitch.isChecked = pref.getBoolean(SharedPref.BAIDU_TRANSLATE, true)
        ninjaWatchNotificationSwitch.isChecked = pref.getBoolean(SharedPref.NOTIFICATION_NINJA_WATCH, true)
        newWordNotificationSwitch.isChecked = pref.getBoolean(SharedPref.NOTIFICATION_NEW_WORD, true)
    }

    private fun saveSettings() {
        val editor = requireActivity().defaultSharedPreferences.edit()
        editor
            .putBoolean(SharedPref.YOUDAO_DICT, youdaoDictSwitch.isChecked)
            .putBoolean(SharedPref.WEB_EXPLAIN, webExplainSwitch.isChecked)
            .putBoolean(SharedPref.YOUDAO_TRANSLATE, youdaoTranslateSwitch.isChecked)
            .putBoolean(SharedPref.BAIDU_TRANSLATE, baiduTranslateSwitch.isChecked)
            .putBoolean(SharedPref.NOTIFICATION_NINJA_WATCH, ninjaWatchNotificationSwitch.isChecked)
            .putBoolean(SharedPref.NOTIFICATION_NEW_WORD, newWordNotificationSwitch.isChecked)
            .apply()
    }

    companion object {

        private val TAG = "SettingsFragment"
        internal val REQUEST_BACKUP_FILE = 1
        private val FILE_KEY_WORD = "word_ninja"
    }
}