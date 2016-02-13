package com.example.changfeng.taptapword;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by changfeng on 2015/5/4.
 */
public class HelpFragment extends Fragment {

    private static final String TAG = "HelpFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        return view;
    }
}
