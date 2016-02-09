package com.example.changfeng.taptapword;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;
import com.dexafree.materialList.view.MaterialListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentWordFragment extends Fragment {

    private static final String TAG = "RecentWordFragment";

    private static final int REQUEST_WORD = 1;
    @Bind(R.id.material_list_view)
    MaterialListView materialListView;
    private Word currentWord;
    private ArrayList<Word> selectedWords;
    private ArrayList<Word> mRecentWords = new ArrayList<>();
    private boolean isActionMode = false;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            isActionMode = true;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_recent_words_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    if (!selectedWords.isEmpty()) {
                        deleteWords();
                        showToast(getString(R.string.message_delete_success), Toast.LENGTH_SHORT);
//                    Log.d(TAG, "delete words:" + selectedItemPositions);
                        mode.finish();
                    }
                    break;
                case R.id.action_archive:
                    if (!selectedWords.isEmpty()) {
                        archiveWords();
                        showToast(getString(R.string.message_archive_success), Toast.LENGTH_SHORT);
//                    Log.d(TAG, "archived words Positions:" + selectedItemPositions);
                    }
                    mode.finish();
                    break;
                case R.id.action_select_all:
                    selectAll();
                    checkAll();
                    break;
                default:
                    return false;
            }
            return true;
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
//            Log.d(TAG, "onDestroyActionMode() called");
            updateListView(Color.WHITE);
            isActionMode = false;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mRecentWords = WordManger.get(getActivity()).getUnarchivedWords();
        updateListView(Color.WHITE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.material_list_view, container, false);
        ButterKnife.bind(this,view);
        mRecentWords = WordManger.get(getActivity()).getUnarchivedWords();
        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(CardItemView cardItemView, int i) {
                if (mRecentWords.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.title_no_recent_words), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isActionMode) {

                    if (selectedWords.contains(mRecentWords.get(i))) {
                        selectedWords.remove(mRecentWords.get(i));
                        cardItemView.setBackgroundColor(Color.WHITE);
                    } else {
                        selectedWords.add(mRecentWords.get(i));
                        cardItemView.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    }
                } else {
                    currentWord = mRecentWords.get(i);
                    Intent intent = new Intent(getActivity(), WordActivity.class);
                    intent.putExtra(WordActivity.EXTRA_WORD_NAME, currentWord.getName());
                    startActivity(intent);
                }

            }

            @Override
            public void onItemLongClick(CardItemView cardItemView, int i) {
                if (!isActionMode) {
                    isActionMode = true;
                    getActivity().startActionMode(mActionModeCallback);
                    selectedWords = new ArrayList<>();
                    if (!mRecentWords.isEmpty()) {
                        selectedWords.add(mRecentWords.get(i));
                        cardItemView.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fratment_new_word, menu);
    }

    private void updateListView(int color) {
        materialListView.removeAllViews();
        materialListView.clear();

        if (mRecentWords.isEmpty()) {
            SimpleCard card = new SmallImageCard(getActivity());
            card.setTitle(getString(R.string.title_no_recent_words));
            card.setDescription(getString(R.string.description_no_recent_words));
            card.setTag("SIMPLE_CARD");
            card.setDismissible(true);
            card.setBackgroundColor(color);
            card.setTitleColor(getResources().getColor(R.color.colorGreen));
            card.setDescriptionColor(getResources().getColor(R.color.colorGrey));
            materialListView.add(card);
        } else {
            for (Word word : mRecentWords) {
                SimpleCard card = new SmallImageCard(getActivity());
                card.setTitle(word.getName());

                StringBuilder description = new StringBuilder();

                if (!word.getFormatPhones().isEmpty()) {
                    description.append(word.getFormatPhones());
                }

                if (!word.getMeans().isEmpty()) {
                    description.append("\n\n").append(word.getMeans());
                }
                if (!description.toString().isEmpty()) {
                    card.setDescription(description.toString());
                }

                card.setTag("SIMPLE_CARD");
                card.setDismissible(false);
                card.setBackgroundColor(color);
                card.setTitleColor(getResources().getColor(R.color.colorGreen));
                card.setDescriptionColor(getResources().getColor(R.color.colorGrey));
                materialListView.add(card);
            }
        }
        materialListView.setBackgroundColor(Color.WHITE);
    }

    private void archiveWords() {
        if (selectedWords.isEmpty()) {
            return;
        }

        for (Word word : selectedWords) {
            word.setArchived(true);
            WordManger.get(getActivity()).updateWord(word);
        }
        mRecentWords.removeAll(selectedWords);
    }

    private void deleteWords() {
//        Log.d(TAG,"deleteWords() called positions:" + positions);
        if (selectedWords.isEmpty()) {
            return;
        }
        for (Word word : selectedWords) {
            WordManger.get(getActivity()).deleteWord(word);
        }
        mRecentWords.removeAll(selectedWords);
    }

    private void selectAll() {
        if (mRecentWords.isEmpty()) {
            return;
        }
        selectedWords = mRecentWords;
    }

    private void checkAll() {
        if (mRecentWords.isEmpty()) {
            return;
        }
        materialListView.setBackgroundColor(getResources().getColor(R.color.colorGreen));

    }

    private void showToast(String message, int duration) {
        Toast.makeText(getActivity(), message, duration).show();
    }

}