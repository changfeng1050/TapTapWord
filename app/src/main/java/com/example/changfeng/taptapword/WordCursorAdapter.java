package com.example.changfeng.taptapword;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by changfeng on 2015/5/11.
 */
public class WordCursorAdapter extends CursorAdapter {

    private static final String TAG = "WordCursorAdapter";

    private DatabaseHelper.WordCursor wordCursor;

    public WordCursorAdapter(Context context, DatabaseHelper.WordCursor cursor) {
        super(context, cursor, 0);
        wordCursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Word word = wordCursor.getWord();

        TextView textView = (TextView) view;
        textView.setText(word.getName());
    }
}
