package com.java.wanghaoyu;

import android.app.SearchManager;
import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "tb_def")
public class Word {
    @DatabaseField(generatedId = true, columnName = COLUMN_ID)
    private int id;
    @DatabaseField(columnName = COLUMN_WORD)
    private String word;
    @DatabaseField(columnName = COLUMN_SUGGESTION)
    private String suggestion;

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_WORD = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String COLUMN_SUGGESTION = SearchManager.SUGGEST_COLUMN_INTENT_DATA;

    Word() {

    }

    public Word(String word) {
        this.word = word;
        this.suggestion = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", suggestion='" + suggestion + '\'' +
                '}';
    }
}

