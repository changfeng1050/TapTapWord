package com.example.changfeng.taptapword.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*

import com.example.changfeng.taptapword.R
import com.example.changfeng.taptapword.WordManger
import com.example.changfeng.taptapword.WordsFragment
import org.jetbrains.anko.support.v4.toast

/**
 * Created by changfeng on 2016/2/12.
 */
class UnArchiveWordsFragment : WordsFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        archived = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_unarchived_words, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.archive_all_words -> {
                WordManger.get(activity).archiveAllWords()
                setRecyclerAdapter()
                toast(R.string.message_archive_success)
            }
            R.id.delete_all_unarchive_words -> AlertDialog.Builder(activity).setTitle(R.string.title_confirm_delete).setMessage(R.string.message_confirm_delete).setPositiveButton(R.string.button_ok) { dialog, which ->
                WordManger.get(context).deleteUnArchivedWords()
                setRecyclerAdapter()
                toast(R.string.message_delete_success)
            }.setCancelable(true).create().show()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = "UnArchiveWordsFragment"
    }
}
