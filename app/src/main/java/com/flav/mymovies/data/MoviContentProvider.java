package com.flav.mymovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.flav.mymovies.R;
import com.flav.mymovies.data.DatabaseDescription.Movie;

public class MoviContentProvider extends ContentProvider {

    private MoviesDatabaseHelper dbHelper;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ONE_MOVIE = 1;
    private static final int MOVIES = 2;

    static {
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                Movie.TABLE_NAME + "/#", ONE_MOVIE);

        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                Movie.TABLE_NAME, MOVIES);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MoviesDatabaseHelper(getContext());
        return true;
    }

    // required method: Not used in this app, so we return null
    @Override
    public String getType(Uri uri) {
        return null;
    }

    // query the database
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Movie.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_MOVIE:
                queryBuilder.appendWhere(
                        Movie._ID + "=" + uri.getLastPathSegment());
                break;
            case MOVIES:
                break;
            default:
                throw new UnsupportedOperationException(
                        "Invalid query Uri:" + uri);
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newMovieUri;

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                long rowId = dbHelper.getWritableDatabase().insert(
                        Movie.TABLE_NAME, null, values);

                if (rowId > 0) {
                    newMovieUri = Movie.buildContactUri(rowId);

                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else
                    throw new SQLException("Insert failed" + uri);
                break;
            default:
                throw new UnsupportedOperationException("Invalid insert Uri:" + uri);
        }

        return newMovieUri;
    }

    // update an existing contact in the database
    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int numberOfRowsUpdated; // 1 if update successful; 0 otherwise

        switch (uriMatcher.match(uri)) {
            case ONE_MOVIE:
                String id = uri.getLastPathSegment();

                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        Movie.TABLE_NAME, values, Movie._ID + "=" + id,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Invalid update Uri:" + uri);
        }

        // if changes were made, notify observers that the database changed
        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    // delete an existing contact from the database
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri)) {
            case ONE_MOVIE:
                String id = uri.getLastPathSegment();

                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        Movie.TABLE_NAME, Movie._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        "Invalid delete Uri:" + uri);
        }

        if (numberOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }
}
