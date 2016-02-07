package com.example.changfeng.taptapword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changfeng on 2015/5/10.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DB_NAME = "WORDS";
    private static final int VERSION = 1;

    private static final String TABLE_WORD = "Word";
    private static final String COLUMN_WORD_ID = "_id";
    private static final String COLUMN_LANGUAGE = "language";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PH_AM = "ph_am";
    private static final String COLUMN_PH_EN = "ph_en";
    private static final String COLUMN_MEANS = "means";
    private static final String COLUMN_ARCHIVE = "archive";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_MINUTE = "minute";
    private static final String COLUMN_SECOND = "second";


    public static final String CREATE_WORD = "create table " + TABLE_WORD + "(" +
            COLUMN_WORD_ID + " integer primary key autoincrement," +
            COLUMN_LANGUAGE + " text," +
            COLUMN_NAME + " text," +
            COLUMN_PH_AM + " text," +
            COLUMN_PH_EN + " text," +
            COLUMN_MEANS + " text," +
            COLUMN_ARCHIVE + " integer," +
            COLUMN_YEAR + " integer," +
            COLUMN_MONTH + " integer," +
            COLUMN_DATE + " integer," +
            COLUMN_HOUR + " integer," +
            COLUMN_MINUTE + " integer," +
            COLUMN_SECOND + " integer)";

    private Context mContent;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContent = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORD);
        MyLog.d(TAG, "table word created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist " + TABLE_WORD);
        onCreate(db);
    }

    public long insertWord (Word word) {
        return getWritableDatabase().insert(TABLE_WORD, null, getContentValues(word));
    }

    private ContentValues getContentValues(Word word) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LANGUAGE, word.getLanguage());
        cv.put(COLUMN_NAME, word.getName());
        cv.put(COLUMN_PH_AM, word.getAmPhone());
        cv.put(COLUMN_PH_EN, word.getEnPhone());
        cv.put(COLUMN_MEANS, word.getMeans());
        cv.put(COLUMN_ARCHIVE, word.isArchived() ? 1 : 0);
        cv.put(COLUMN_YEAR, word.getYear());
        cv.put(COLUMN_MONTH, word.getMonth());
        cv.put(COLUMN_DATE, word.getDate());
        cv.put(COLUMN_HOUR, word.getHour());
        cv.put(COLUMN_MINUTE, word.getMinute());
        cv.put(COLUMN_SECOND, word.getSecond());

        return cv;
    }

    public WordCursor queryWords() {
        Cursor wrapped = getReadableDatabase().query(TABLE_WORD, null, null,null,null,null, null);
        Log.d(TAG, wrapped.getColumnCount() + " " + wrapped.getColumnNames());
        return new WordCursor(wrapped);
    }


    private ArrayList<Word> getWords(Cursor cursor) {
        ArrayList<Word> words = new ArrayList<>();
        if (cursor.moveToLast()) {
            do {
                Word word = new Word();
                word.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_WORD_ID)));
                word.setLanguage(cursor.getString(cursor.getColumnIndex(COLUMN_LANGUAGE)));
                word.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                word.setAmPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PH_AM)));
                word.setEnPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PH_EN)));
                word.setMeans(cursor.getString(cursor.getColumnIndex(COLUMN_MEANS)));
                if (cursor.getInt(cursor.getColumnIndex(COLUMN_ARCHIVE)) == 1) {
                    word.setArchived(true);
                } else {
                    word.setArchived(false);
                }
                word.setYear(cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR)));
                word.setMonth(cursor.getInt(cursor.getColumnIndex(COLUMN_MONTH)));
                word.setDate(cursor.getInt(cursor.getColumnIndex(COLUMN_DATE)));
                word.setHour(cursor.getInt(cursor.getColumnIndex(COLUMN_HOUR)));
                word.setMinute(cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTE)));
                word.setSecond(cursor.getInt(cursor.getColumnIndex(COLUMN_SECOND)));
                words.add(word);
            } while (cursor.moveToPrevious());
        }
        return words;
    }

    public ArrayList<Word> loadUnarchivedWords() {
        Cursor cursor = getReadableDatabase().query(TABLE_WORD, null, "archive=?", new String[] {"0"},null,null, null);
        return getWords(cursor);
    }

    public ArrayList<Word> loadArchivedWords() {
        Cursor cursor = getReadableDatabase().query(TABLE_WORD, null, "archive=?", new String[] {"1"},null,null, null);
        return getWords(cursor);
    }

    public ArrayList<Word> loadWords() {
        Cursor cursor = getReadableDatabase().query(TABLE_WORD, null, null,null,null,null, null);
        return getWords(cursor);
    }

    public void updateWord(Word word) {
        getWritableDatabase().update(TABLE_WORD, getContentValues(word), COLUMN_WORD_ID + " = ?", new String[]{String.valueOf(word.getId())});
    }

    public void deleteWord(Word word) {
        getWritableDatabase().delete(TABLE_WORD, COLUMN_WORD_ID + " = ?", new String[] {String.valueOf(word.getId())});
    }

    public void replaceWord(Word word) {
        getWritableDatabase().replace(TABLE_WORD, "_id", getContentValues(word));
    }

    public void deleteExistingWords(Word word) {
        Cursor cursor = getReadableDatabase().query(TABLE_WORD, null, COLUMN_NAME +"=?", new String[] {word.getName()},null,null, null);
        if (cursor.moveToLast()) {
            do {
               getWritableDatabase().delete(TABLE_WORD, COLUMN_NAME +"=?", new String[] {word.getName()});
            } while (cursor.moveToPrevious());
        }

    }
    public static class WordCursor extends CursorWrapper{

        public WordCursor(Cursor c) {
            super(c);
        }

        public Word getWord() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }

            Word word = new Word();
            word.setId(getLong(getColumnIndex(COLUMN_WORD_ID)));
            word.setLanguage(getString(getColumnIndex(COLUMN_LANGUAGE)));
            word.setName(getString(getColumnIndex(COLUMN_NAME)));
            word.setAmPhone(getString(getColumnIndex(COLUMN_PH_AM)));
            word.setEnPhone(getString(getColumnIndex(COLUMN_PH_EN)));
            word.setMeans(getString(getColumnIndex(COLUMN_MEANS)));
            int archive = getInt(getColumnIndex(COLUMN_ARCHIVE));
            if (archive == 1) {
                word.setArchived(true);
            } else {
                word.setArchived(false);
            }
            word.setYear(getInt(getColumnIndex(COLUMN_YEAR)));
            word.setMonth(getInt(getColumnIndex(COLUMN_MONTH)));
            word.setDate(getInt(getColumnIndex(COLUMN_DATE)));
            word.setHour(getInt(getColumnIndex(COLUMN_HOUR)));
            word.setMinute(getInt(getColumnIndex(COLUMN_MINUTE)));
            word.setSecond(getInt(getColumnIndex(COLUMN_SECOND)));

            return word;
        }

    }

}
