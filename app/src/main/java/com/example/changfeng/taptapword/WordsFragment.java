package com.example.changfeng.taptapword;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.changfeng.taptapword.adapter.RecyclerViewAdapter;
import com.example.changfeng.taptapword.listener.WordItemArchivedListener;
import com.example.changfeng.taptapword.listener.WordItemClickListener;
import com.example.changfeng.taptapword.listener.WordItemDeleteListener;
import com.example.changfeng.taptapword.listener.WordItemLongClickListener;
import com.example.changfeng.taptapword.listener.WordItemUnArchivedListener;
import com.example.changfeng.taptapword.util.Attributes;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by changfeng on 2015/4/17.
 */
public class WordsFragment extends Fragment implements WordItemClickListener, WordItemLongClickListener, WordItemArchivedListener, WordItemUnArchivedListener, WordItemDeleteListener {

    private static final String TAG = "WordsFragment";

    public static final String ARG_ARCHIVED = "archived";

    private List<Word> words;

    @Bind(R.id.word_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.no_recent_words_card_view)
    CardView noRecentWordsCardView;
    @Bind(R.id.no_archived_words_card_view)
    CardView noArchivedWordsCardView;

    RecyclerViewAdapter adapter;

    protected boolean archived = false;


    @Override
    public void onResume() {
        super.onResume();
        setRecyclerAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_word_recycler_view, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), WordActivity.class);
        intent.putExtra(WordActivity.EXTRA_WORD_NAME, words.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
    }

    @Override
    public void onItemArchived(View view, int position) {
        update();
        showToast(R.string.message_archive_success);
    }

    @Override
    public void onItemUnArchived(View view, int position) {
        update();
        showToast(R.string.message_unarchive_success);
    }

    @Override
    public void onItemDelete(View view, int position) {
        update();
        showToast(R.string.message_delete_success);
    }

    public void setRecyclerAdapter() {
        words = WordManger.get(getActivity()).getWords(archived);
        adapter = new RecyclerViewAdapter(getActivity(), words, archived);

        adapter.setMode(Attributes.Mode.Single);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        adapter.setOnItemArchiveListener(this);
        adapter.setOnItemUnarchivedListener(this);
        adapter.setOnItemDeleteListener(this);
        recyclerView.setAdapter(adapter);
        update();
    }

    private void update() {
        words = WordManger.get(getActivity()).getWords(archived);
        if (words.isEmpty()) {
            if (archived) {
                if (noArchivedWordsCardView.getVisibility() != View.VISIBLE) {
                    noArchivedWordsCardView.setVisibility(View.VISIBLE);
                }
            } else {
                if (noRecentWordsCardView.getVisibility() != View.VISIBLE) {
                    noRecentWordsCardView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (archived) {
                if (noArchivedWordsCardView.getVisibility() != View.GONE) {
                    noArchivedWordsCardView.setVisibility(View.GONE);
                }
            } else {
                if (noRecentWordsCardView.getVisibility() != View.GONE) {
                    noRecentWordsCardView.setVisibility(View.GONE);
                }
            }
        }
    }

    public void showToast(int resourceId) {
        Toast.makeText(getActivity(), getString(resourceId), Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
