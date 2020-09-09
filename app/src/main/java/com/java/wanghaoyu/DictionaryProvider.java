package com.java.wanghaoyu;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

public class DictionaryProvider extends ContentProvider {

    public static String AUTHORITY = "com.schaepher.memorywarehouse.DictionaryProvider";

    private DatabaseHelper mDatabaseHelper = null;

    private static final int SEARCH_SUGGEST = 0;
    private static final UriMatcher mURIMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = DatabaseHelper.getHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String query = uri.getLastPathSegment();
        int i = mURIMatcher.match(uri);
        if (i == SEARCH_SUGGEST) {
            return mDatabaseHelper.getSuggestionWords(query);
        } else {
            throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int i = mURIMatcher.match(uri);
        if (i == SEARCH_SUGGEST) {
            return SearchManager.SUGGEST_MIME_TYPE;
        } else {
            throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }



}
