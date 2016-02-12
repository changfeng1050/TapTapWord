package com.example.changfeng.taptapword.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changfeng.taptapword.R;
import com.example.changfeng.taptapword.Word;
import com.example.changfeng.taptapword.listener.WordItemArchivedListener;
import com.example.changfeng.taptapword.listener.WordItemClickListener;
import com.example.changfeng.taptapword.listener.WordItemDeleteListener;
import com.example.changfeng.taptapword.listener.WordItemLongClickListener;
import com.example.changfeng.taptapword.listener.WordItemUnArchivedListener;
import com.example.changfeng.taptapword.ui.SwipeLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        @Bind(R.id.swipe)
        SwipeLayout swipeLayout;
        @Bind(R.id.word_card_view)
        CardView wordCardView;
        @Bind(R.id.word_name_text_view)
        TextView wordNameTextView;
        @Bind(R.id.word_phones_text_view)
        TextView wordPhonesTextView;
        @Bind(R.id.word_means_text_view)
        TextView wordMeansTextView;

        @Bind(R.id.unarchive_layout)
        LinearLayout unArchiveLayout;
        @Bind(R.id.archive_layout)
        LinearLayout archiveLayout;
        @Bind(R.id.delete_layout)
        LinearLayout deleteLayout;

        @Bind(R.id.unarchive_text_view)
        TextView unArchiveTextView;
        @Bind(R.id.archive_text_view)
        TextView archiveTextView;
        @Bind(R.id.delete_text_view)
        TextView deleteTextView;

        private WordItemClickListener listener;
        private WordItemLongClickListener longClickListener;

        public SimpleViewHolder(final View itemView, WordItemClickListener listener, WordItemLongClickListener longClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
                    words.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, words.size());
                    mItemManger.closeAllItems();
                    Toast.makeText(v.getContext(), "Archived: " + viewHolder.wordNameTextView.getText().toString() + "!", Toast.LENGTH_SHORT).show();
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
                    words.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, words.size());
                    mItemManger.closeAllItems();
                    Toast.makeText(v.getContext(), "Unarchived: " + viewHolder.wordNameTextView.getText().toString() + "!", Toast.LENGTH_SHORT).show();
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
                words.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, words.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + viewHolder.wordNameTextView.getText().toString() + "!", Toast.LENGTH_SHORT).show();
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
