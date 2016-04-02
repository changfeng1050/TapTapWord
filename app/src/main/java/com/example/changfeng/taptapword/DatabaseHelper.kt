package com.example.changfeng.taptapword

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.database.CursorWrapper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import java.util.ArrayList
import java.util.Currency

/**
 * Created by changfeng on 2015/5/10.
 */
class DatabaseHelper(private val mContent: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(mContent, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_WORD)
        MyLog.d(TAG, "table word created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        when (oldVersion) {
            1 -> {
                db.execSQL("alter table $TABLE_WORD add column $COLUMN_NOTE text")
                db.execSQL("alter table $TABLE_WORD add column $COLUMN_WEB_EXPLAINS text")
            }
            2 -> db.execSQL("alter table $TABLE_WORD add column $COLUMN_WEB_EXPLAINS text")
        }
    }

    fun insertWord(word: Word): Long {
        return writableDatabase.insert(TABLE_WORD, null, getContentValues(word))
    }

    private fun getContentValues(word: Word): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_LANGUAGE, word.language)
        cv.put(COLUMN_NAME, word.name)
        cv.put(COLUMN_PH_AM, word.amPhone)
        cv.put(COLUMN_PH_EN, word.enPhone)
        cv.put(COLUMN_MEANS, word.means)
        cv.put(COLUMN_ARCHIVE, if (word.isArchived) 1 else 0)
        cv.put(COLUMN_YEAR, word.year)
        cv.put(COLUMN_MONTH, word.month)
        cv.put(COLUMN_DATE, word.date)
        cv.put(COLUMN_HOUR, word.hour)
        cv.put(COLUMN_MINUTE, word.minute)
        cv.put(COLUMN_SECOND, word.second)
        cv.put(COLUMN_NOTE, word.note)
        cv.put(COLUMN_WEB_EXPLAINS, word.webExplains)

        return cv
    }

    fun queryWords(): WordCursor {
        val wrapped = readableDatabase.query(TABLE_WORD, null, null, null, null, null, null)
        return WordCursor(wrapped)
    }


    private fun getWords(cursor: Cursor): ArrayList<Word> {
        val words = ArrayList<Word>()
        if (cursor.moveToLast()) {
            do {
                val word = Word()
                word.id = cursor.getLong(cursor.getColumnIndex(COLUMN_WORD_ID))
                word.language = cursor.getString(cursor.getColumnIndex(COLUMN_LANGUAGE))
                word.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                word.amPhone = cursor.getString(cursor.getColumnIndex(COLUMN_PH_AM))
                word.enPhone = cursor.getString(cursor.getColumnIndex(COLUMN_PH_EN))
                word.means = cursor.getString(cursor.getColumnIndex(COLUMN_MEANS))
                if (cursor.getInt(cursor.getColumnIndex(COLUMN_ARCHIVE)) == 1) {
                    word.isArchived = true
                } else {
                    word.isArchived = false
                }
                word.year = cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR))
                word.month = cursor.getInt(cursor.getColumnIndex(COLUMN_MONTH))
                word.date = cursor.getInt(cursor.getColumnIndex(COLUMN_DATE))
                word.hour = cursor.getInt(cursor.getColumnIndex(COLUMN_HOUR))
                word.minute = cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTE))
                word.second = cursor.getInt(cursor.getColumnIndex(COLUMN_SECOND))
                word.note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE))
                word.webExplains = cursor.getString(cursor.getColumnIndex(COLUMN_WEB_EXPLAINS))

                words.add(word)
            } while (cursor.moveToPrevious())
        }
        return words
    }

    fun loadUnarchivedWords(): ArrayList<Word> {
        val cursor = readableDatabase.query(TABLE_WORD, null, "archive=?", arrayOf("0"), null, null, null)
        return getWords(cursor)
    }

    fun loadArchivedWords(): ArrayList<Word> {
        val cursor = readableDatabase.query(TABLE_WORD, null, "archive=?", arrayOf("1"), null, null, null)
        return getWords(cursor)
    }

    fun loadWords(): ArrayList<Word> {
        val cursor = readableDatabase.query(TABLE_WORD, null, null, null, null, null, null)
        return getWords(cursor)
    }

    fun getWord(name: String): ArrayList<Word> {
        val cursor = readableDatabase.query(TABLE_WORD, null, COLUMN_NAME + "=?", arrayOf(name), null, null, null)
        return getWords(cursor)
    }

    fun updateWord(word: Word) {
        writableDatabase.update(TABLE_WORD, getContentValues(word), COLUMN_WORD_ID + " = ?", arrayOf(word.id.toString()))
    }

    fun deleteWord(word: Word) {
        writableDatabase.delete(TABLE_WORD, COLUMN_WORD_ID + " = ?", arrayOf(word.id.toString()))
    }

    fun replaceWord(word: Word) {
        writableDatabase.replace(TABLE_WORD, "_id", getContentValues(word))
    }

    fun deleteExistingWords(word: Word) {
        val cursor = readableDatabase.query(TABLE_WORD, null, COLUMN_NAME + "=?", arrayOf(word.name), null, null, null)
        if (cursor.moveToLast()) {
            do {
                writableDatabase.delete(TABLE_WORD, COLUMN_NAME + "=?", arrayOf(word.name))
            } while (cursor.moveToPrevious())
        }
    }

    class WordCursor(c: Cursor) : CursorWrapper(c) {

        val word: Word?
            get() {
                if (isBeforeFirst || isAfterLast) {
                    return null
                }

                val word = Word()
                word.id = getLong(getColumnIndex(COLUMN_WORD_ID))
                word.language = getString(getColumnIndex(COLUMN_LANGUAGE))
                word.name = getString(getColumnIndex(COLUMN_NAME))
                word.amPhone = getString(getColumnIndex(COLUMN_PH_AM))
                word.enPhone = getString(getColumnIndex(COLUMN_PH_EN))
                word.means = getString(getColumnIndex(COLUMN_MEANS))
                val archive = getInt(getColumnIndex(COLUMN_ARCHIVE))
                if (archive == 1) {
                    word.isArchived = true
                } else {
                    word.isArchived = false
                }
                word.year = getInt(getColumnIndex(COLUMN_YEAR))
                word.month = getInt(getColumnIndex(COLUMN_MONTH))
                word.date = getInt(getColumnIndex(COLUMN_DATE))
                word.hour = getInt(getColumnIndex(COLUMN_HOUR))
                word.minute = getInt(getColumnIndex(COLUMN_MINUTE))
                word.second = getInt(getColumnIndex(COLUMN_SECOND))
                word.note = getString(getColumnIndex(COLUMN_NOTE))
                word.webExplains = getString(getColumnIndex(COLUMN_WEB_EXPLAINS))

                return word
            }

    }

    companion object {

        private val TAG = "DatabaseHelper"

        private val TABLE_WORD = "Word"
        private val COLUMN_WORD_ID = "_id"
        private val COLUMN_LANGUAGE = "language"
        private val COLUMN_NAME = "name"
        private val COLUMN_PH_AM = "ph_am"
        private val COLUMN_PH_EN = "ph_en"
        private val COLUMN_MEANS = "means"
        private val COLUMN_ARCHIVE = "archive"
        private val COLUMN_YEAR = "year"
        private val COLUMN_MONTH = "month"
        private val COLUMN_DATE = "date"
        private val COLUMN_HOUR = "hour"
        private val COLUMN_MINUTE = "minute"
        private val COLUMN_SECOND = "second"
        private val COLUMN_NOTE = "note"
        private val COLUMN_WEB_EXPLAINS = "web_explains"


        val CREATE_WORD = "create table $TABLE_WORD($COLUMN_WORD_ID integer primary key autoincrement,$COLUMN_LANGUAGE text,$COLUMN_NAME text,$COLUMN_PH_AM text,$COLUMN_PH_EN text,$COLUMN_MEANS text,$COLUMN_ARCHIVE integer,$COLUMN_YEAR integer,$COLUMN_MONTH integer,$COLUMN_DATE integer,$COLUMN_HOUR integer,$COLUMN_MINUTE integer,$COLUMN_SECOND integer,$COLUMN_NOTE text,$COLUMN_WEB_EXPLAINS text)"
    }

}
