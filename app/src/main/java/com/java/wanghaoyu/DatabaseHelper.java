package com.java.wanghaoyu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "wordbook.db";

    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper databaseHelper = null;

    private Dao<Word, Integer> wordDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean isWordTableEmpty() {
        try {
            return getWordDao().countOf() == 0L;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Cursor getSuggestionWords(String word) {
        QueryBuilder<Word, Integer> qb = getWordDao().queryBuilder();
        CloseableIterator<Word> iterator;
        Cursor cursor = null;
        try {
            qb.distinct().where().like(Word.COLUMN_WORD, word + "%");
            iterator = getWordDao().iterator(qb.prepare());
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            cursor = results.getRawCursor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public void insertWord(Word word) {
        try {
            getWordDao().create(word);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<Word, Integer> getWordDao() {
        if (wordDao == null) {
            try {
                wordDao = getDao(Word.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return wordDao;
    }

    static DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void close() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Word.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Word.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
