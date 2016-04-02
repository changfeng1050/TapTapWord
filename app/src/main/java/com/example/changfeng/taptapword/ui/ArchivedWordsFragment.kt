package com.example.changfeng.taptapword.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import com.example.changfeng.taptapword.R
import com.example.changfeng.taptapword.WordManger
import com.example.changfeng.taptapword.WordsFragment
import org.jetbrains.anko.support.v4.toast

/**
 * Created by changfeng on 2016/2/12.
 */
class ArchivedWordsFragment : WordsFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        archived = true
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity.menuInflater.inflate(R.menu.menu_archived_words, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            R.id.unarchive_all_words -> {
                WordManger.get(activity).unArchiveAllWords()
                setRecyclerAdapter()
                toast(R.string.message_unarchive_success)
            }
            R.id.delete_all_archive_words -> AlertDialog.Builder(activity).setTitle(R.string.title_confirm_delete).setMessage(R.string.message_confirm_delete).setPositiveButton(R.string.button_ok) { dialog, which ->
                WordManger.get(context).deleteArchivedWords()
                setRecyclerAdapter()
                toast(R.string.message_delete_success)
            }.setCancelable(true).create().show()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = "ArchivedWordsFragment"
    }
}
