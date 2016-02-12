package com.example.changfeng.taptapword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.changfeng.taptapword.adapter.WordAdapter;
import com.example.changfeng.taptapword.listener.WordItemClickListener;
import com.example.changfeng.taptapword.listener.WordItemLongClickListener;

import java.util.List;

/**
 * Created by changfeng on 2015/5/10.
 */
public class TestFragment extends Fragment implements WordItemClickListener, WordItemLongClickListener{

    private static final String TAG = "TestFragment";

    private RecyclerView recyclerView;
    private WordAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Word> words;
//
//    private ListView listView;
//    private WordCursorAdapter adapter;
//    private DatabaseHelper.WordCursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCursor = WordManger.get(getActivity()).queryWords();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.fragment_test, container, false);
//        listView = (ListView) view.findViewById(R.id.listView);
//        adapter = new WordCursorAdapter(getActivity(), mCursor);
//        listView.setAdapter(adapter);

        View view = inflater.inflate(R.layout.fragment_word_recycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.word_recycler_view);
//        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        words = WordManger.get(getActivity()).getUnarchivedWords();

        adapter = new WordAdapter(words);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "click:" + words.get(position).getName(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), WordActivity.class);
        intent.putExtra(WordActivity.EXTRA_WORD_NAME, words.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Toast.makeText(getActivity(), "long click:" + words.get(position).getName(), Toast.LENGTH_SHORT).show();
    }
}
