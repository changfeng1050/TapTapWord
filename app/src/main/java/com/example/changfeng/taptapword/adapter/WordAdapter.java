package com.example.changfeng.taptapword.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.changfeng.taptapword.R;
import com.example.changfeng.taptapword.Word;
import com.example.changfeng.taptapword.listener.WordItemClickListener;
import com.example.changfeng.taptapword.listener.WordItemLongClickListener;

import java.util.List;

/**
 * Created by changfeng on 2016/2/10.
 */
public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    private static final String TAG = "WordAdapter";

    private List<Word> words;
    private WordItemClickListener itemClickListener;
    private WordItemLongClickListener itemLongClickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public CardView cardView;
        public TextView wordNameTextView;
        public TextView wordPhonesTextView;
        public TextView wordMeansTextView;

        private WordItemClickListener listener;
        private WordItemLongClickListener longClickListener;

        public ViewHolder(View v, WordItemClickListener listener, WordItemLongClickListener longClickListener) {
            super(v);
            cardView = (CardView)v.findViewById(R.id.word_card_view);
            wordNameTextView = (TextView) cardView.findViewById(R.id.word_name_text_view);
            wordPhonesTextView = (TextView) cardView.findViewById(R.id.word_phones_text_view);
            wordMeansTextView = (TextView) cardView.findViewById(R.id.word_means_text_view);
            this.listener = listener;
            this.longClickListener = longClickListener;

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(v, getLayoutPosition());
            }
            return true;
        }


    }

    public WordAdapter(List<Word> words) {
        this.words = words;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_card_view, parent, false);
        return new ViewHolder(v, itemClickListener, itemLongClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.wordNameTextView.setText(words.get(position).getName());
        holder.wordPhonesTextView.setText(words.get(position).getFormatPhones());
        holder.wordMeansTextView.setText(words.get(position).getMeans());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setOnItemClickListener(WordItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnItemLongClickListener(WordItemLongClickListener listener) {
        itemLongClickListener = listener;
    }

}
