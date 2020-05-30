package com.flav.mymovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {
    public static final String AUTHORITY =
            "com.flav.mymovies.data";

    private static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    // nested class defines contents of the contacts table
    public static final class Movie implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_DIRECTOR = "director";
        public static final String COLUMN_MAINCHAR = "main_character";
        public static final String COLUMN_ACTORS = "actors";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_SYNOPSIS = "synopsis";


        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
