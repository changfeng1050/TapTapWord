package com.example.changfeng.taptapword;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.changfeng.taptapword.adapter.RecyclerViewAdapter;
import com.example.changfeng.taptapword.adapter.util.DividerItemDecoration;
import com.example.changfeng.taptapword.listener.WordItemClickListener;
import com.example.changfeng.taptapword.listener.WordItemDeleteListener;
import com.example.changfeng.taptapword.listener.WordItemLongClickListener;
import com.example.changfeng.taptapword.util.Attributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecyclerViewExample extends Activity implements WordItemClickListener, WordItemLongClickListener, WordItemDeleteListener {


    /**
     * RecyclerView: The new recycler view replaces the list view. Its more modular and therefore we
     * must implement some of the functionality ourselves and attach it to our recyclerview.
     * <p>
     * 1) Position items on the screen: This is done with LayoutManagers
     * 2) Animate & Decorate views: This is done with ItemAnimators & ItemDecorators
     * 3) Handle any touch events apart from scrolling: This is now done in our adapter's ViewHolder
     */

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_word_recycler_view);
        recyclerView = (RecyclerView) findViewById(R.id.word_recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("RecyclerView");
            }
        }

        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        words = WordManger.get().getUnarchivedWords();
        mAdapter = new RecyclerViewAdapter(this, words, false);

        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        ((RecyclerViewAdapter) mAdapter).setOnItemClickListener(this);
        ((RecyclerViewAdapter) mAdapter).setOnItemLongClickListener(this);
        ((RecyclerViewAdapter) mAdapter).setOnItemDeleteListener(this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        words = WordManger.get(this).getUnarchivedWords();
    }
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "click:" + words.get(position).getName(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, WordActivity.class);
        intent.putExtra(WordActivity.EXTRA_WORD_NAME, words.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Toast.makeText(this, "long click:" + words.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDelete(View view, int position) {
        WordManger.get(this).deleteWord(words.get(position));
        words = WordManger.get(this).getUnarchivedWords();
    }
}
