package com.example.changfeng.taptapword.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changfeng.taptapword.R;
import com.example.changfeng.taptapword.Word;
import com.example.changfeng.taptapword.WordManger;
import com.example.changfeng.taptapword.listener.WordItemArchivedListener;
import com.example.changfeng.taptapword.listener.WordItemClickListener;
import com.example.changfeng.taptapword.listener.WordItemDeleteListener;
import com.example.changfeng.taptapword.listener.WordItemLongClickListener;
import com.example.changfeng.taptapword.listener.WordItemUnArchivedListener;
import com.example.changfeng.taptapword.ui.SwipeLayout;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private List<Word> words;
    private boolean archived = false;
    private WordItemClickListener itemClickListener;
    private WordItemLongClickListener itemLongClickListener;
    private WordItemArchivedListener itemArchivedListener;
    private WordItemUnArchivedListener itemUnArchivedListener;
    private WordItemDeleteListener itemDeleteListener;


    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        SwipeLayout swipeLayout = itemView.findViewById(R.id.swipe);
        CardView wordCardView = itemView.findViewById(R.id.word_card_view);
        TextView wordNameTextView = itemView.findViewById(R.id.word_name_text_view);
        TextView wordPhonesTextView = itemView.findViewById(R.id.word_phones_text_view);
        TextView wordMeansTextView = itemView.findViewById(R.id.word_means_text_view);

        LinearLayout unArchiveLayout = itemView.findViewById(R.id.unarchive_layout);
        LinearLayout archiveLayout = itemView.findViewById(R.id.archive_layout);
        LinearLayout deleteLayout = itemView.findViewById(R.id.delete_layout);

        TextView unArchiveTextView = itemView.findViewById(R.id.unarchive_text_view);
        TextView archiveTextView = itemView.findViewById(R.id.archive_text_view);
        TextView deleteTextView = itemView.findViewById(R.id.delete_text_view);

        private WordItemClickListener listener;
        private WordItemLongClickListener longClickListener;

        public SimpleViewHolder(final View itemView, WordItemClickListener listener, WordItemLongClickListener longClickListener) {
            super(itemView);
            this.listener = listener;
            this.longClickListener = longClickListener;

            wordCardView.setOnClickListener(this);
            wordCardView.setOnLongClickListener(this);
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

    public RecyclerViewAdapter(Context context, List<Word> words, boolean archived) {
        this.mContext = context;
        this.words = words;
        this.archived = archived;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new SimpleViewHolder(view, itemClickListener, itemLongClickListener);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final Word word = words.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        if (!archived) {
            viewHolder.archiveLayout.setVisibility(View.VISIBLE);
            viewHolder.archiveTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                    WordManger.get().archiveWord(words.get(position));
                    words.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, words.size());
                    mItemManger.closeAllItems();
                    if (itemArchivedListener != null) {
                        itemArchivedListener.onItemArchived(v, position);
                    }
                }
            });
        } else {
            viewHolder.archiveLayout.setVisibility(View.GONE);
        }

        if (archived) {
            viewHolder.unArchiveLayout.setVisibility(View.VISIBLE);
            viewHolder.unArchiveTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                    WordManger.get().unarchiveWord(words.get(position));
                    words.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, words.size());
                    mItemManger.closeAllItems();
                    if (itemUnArchivedListener != null) {
                        itemUnArchivedListener.onItemUnArchived(v, position);
                    }
                }
            });
        } else {
            viewHolder.unArchiveLayout.setVisibility(View.GONE);
        }

        viewHolder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                WordManger.get().deleteWord(words.get(position));
                words.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, words.size());
                mItemManger.closeAllItems();
                if (itemDeleteListener != null) {
                    itemDeleteListener.onItemDelete(view, position);
                }
            }
        });

        viewHolder.wordNameTextView.setText(word.getName());
        if (viewHolder.wordNameTextView.getText().toString().isEmpty()) {
            viewHolder.wordNameTextView.setVisibility(View.GONE);
        } else {
            viewHolder.wordNameTextView.setVisibility(View.VISIBLE);
        }

        viewHolder.wordPhonesTextView.setText(word.getFormatPhones());
        if (viewHolder.wordPhonesTextView.getText().toString().isEmpty()) {
            viewHolder.wordPhonesTextView.setVisibility(View.GONE);
        } else {
            viewHolder.wordPhonesTextView.setVisibility(View.VISIBLE);
        }

        viewHolder.wordMeansTextView.setText(word.getMeans());
        if (viewHolder.wordMeansTextView.getText().toString().isEmpty()) {
            viewHolder.wordMeansTextView.setVisibility(View.GONE);
        } else {
            viewHolder.wordMeansTextView.setVisibility(View.VISIBLE);
        }
        mItemManger.bind(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public void removeAll() {


        for (int i = 0; i < getOpenLayouts().size(); i++) {
            removeShownLayouts(getOpenLayouts().get(i));
            notifyItemRemoved(i);
            words.remove(i);
            notifyItemRangeChanged(i, words.size());
        }

        mItemManger.closeAllItems();

    }

    public void setOnItemClickListener(WordItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnItemLongClickListener(WordItemLongClickListener listener) {
        itemLongClickListener = listener;
    }

    public void setOnItemArchiveListener(WordItemArchivedListener listener) {
        itemArchivedListener = listener;
    }

    public void setOnItemUnarchivedListener(WordItemUnArchivedListener listener) {
        itemUnArchivedListener = listener;
    }

    public void setOnItemDeleteListener(WordItemDeleteListener listener) {
        itemDeleteListener = listener;
    }

}
