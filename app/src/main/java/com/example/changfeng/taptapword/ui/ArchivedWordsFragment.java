package com.example.changfeng.taptapword.ui;

import android.os.Bundle;

import com.example.changfeng.taptapword.WordsFragment;

/**
 * Created by changfeng on 2016/2/12.
 */
public class ArchivedWordsFragment extends WordsFragment {

    private static final String TAG = "ArchivedWordsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        archived = true;
    }
}
