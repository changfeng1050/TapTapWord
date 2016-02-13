package com.example.changfeng.taptapword.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.changfeng.taptapword.R;
import com.example.changfeng.taptapword.WordManger;
import com.example.changfeng.taptapword.WordsFragment;

/**
 * Created by changfeng on 2016/2/12.
 */
public class UnArchiveWordsFragment extends WordsFragment{

    private static final String TAG = "UnArchiveWordsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        archived = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_unarchived_words, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.archive_all_words:
                WordManger.get(getActivity()).archiveAllWords();
                setRecyclerAdapter();
                showToast(R.string.message_archive_success);
                break;
            case R.id.delete_all_unarchive_words:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_confirm_delete)
                        .setMessage(R.string.message_confirm_delete)
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WordManger.get(getContext()).deleteUnArchivedWords();
                                setRecyclerAdapter();
                                showToast(R.string.message_delete_success);
                            }
                        })
                        .setCancelable(true)
                        .create().show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
