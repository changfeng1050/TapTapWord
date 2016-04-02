package com.example.changfeng.taptapword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import org.jetbrains.anko.find
import org.jetbrains.anko.onItemClick

import java.io.File
import java.util.ArrayList

/**
 * Created by changfeng on 2015/3/4.
 */
class FileListActivity : Activity(), AdapterView.OnItemClickListener {

    var fileInfoList: MutableList<FileInfo> ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_file_list)


        val intent = intent
        fileInfoList = getFileList(intent.getStringExtra(FILE_KEY_WORD))
        fileInfoList?.let {
            if (fileInfoList!!.isEmpty()) {
                fileInfoList!!.add(FileInfo(getString(R.string.filename_no_backup_file_found), getString(R.string.filepath_no_backup_file_found)))
            }
        }


        val adapter = FileListAdapter(this@FileListActivity,
                R.layout.file_list_item, fileInfoList)

        val listView = find<ListView>(R.id.file_list_view)
        listView.adapter = adapter
        listView.onItemClickListener = this

    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        fileInfoList?.let {
            val fileInfo = fileInfoList!![position]
            if (fileInfo.fileName != getString(R.string.filename_no_backup_file_found)) {
                setResults(fileInfo)
            }
        }
        finish()
    }

    private fun setResults(fileInfo: FileInfo) {
        setResult(Activity.RESULT_OK, Intent().putExtra(FILE_KEY_WORD,fileInfo.filePath))
    }

    private fun getFileList(fileKeyWord: String): ArrayList<FileInfo> {
        val list = ArrayList<FileInfo>()
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val files = Environment.getExternalStorageDirectory().listFiles { file, s -> s.contains(fileKeyWord) }.filter { s -> s.isFile }
            for (file in files) {
                list.add(FileInfo(file.name, file.path))
            }
        }
        return list
    }

    companion object {

        private val TAG = "FileListActivity"
        val FILE_KEY_WORD = "word_ninja"
    }
}
