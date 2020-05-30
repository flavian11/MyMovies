package com.flav.mymovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.flav.mymovies.data.DatabaseDescription.Movie;

public class MoviesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIES_TABLE =
                "CREATE TABLE " + Movie.TABLE_NAME + "(" +
                        Movie._ID + " integer primary key, " +
                        Movie.COLUMN_TITLE + " TEXT, " +
                        Movie.COLUMN_YEAR + " TEXT, " +
                        Movie.COLUMN_DIRECTOR + " TEXT, " +
                        Movie.COLUMN_MAINCHAR + " TEXT, " +
                        Movie.COLUMN_ACTORS + " TEXT, " +
                        Movie.COLUMN_GENRE +" TEXT, " +
                        Movie.COLUMN_LANGUAGE +" TEXT, " +
                        Movie.COLUMN_SYNOPSIS +" TEXT);";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    // normally defines how to upgrade the database when the schema changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) { }
}
