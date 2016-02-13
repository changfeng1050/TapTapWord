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
public class ArchivedWordsFragment extends WordsFragment {

    private static final String TAG = "ArchivedWordsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        archived = true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_archived_words, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.unarchive_all_words:
                WordManger.get(getActivity()).unArchiveAllWords();
                setRecyclerAdapter();
                showToast(R.string.message_unarchive_success);
                break;
            case R.id.delete_all_archive_words:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_confirm_delete)
                        .setMessage(R.string.message_confirm_delete)
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WordManger.get(getContext()).deleteArchivedWords();
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
