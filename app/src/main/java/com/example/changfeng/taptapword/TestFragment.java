package com.example.changfeng.taptapword;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dexafree.materialList.view.MaterialListView;

import java.util.List;

/**
 * Created by changfeng on 2015/5/10.
 */
public class TestFragment extends Fragment{

    private static final String TAG = "TestFragment";

    private ListView listView;
    private WordCursorAdapter adapter;
    private DatabaseHelper.WordCursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCursor = WordManger.get(getActivity()).queryWords();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new WordCursorAdapter(getActivity(), mCursor);
        listView.setAdapter(adapter);
        return view;
    }
}
