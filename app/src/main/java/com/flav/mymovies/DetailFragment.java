package com.flav.mymovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.flav.mymovies.data.DatabaseDescription.Movie;

public class DetailFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface DetailFragmentListener {
        void onMovieDeleted();
        void onEditMovie(Uri movieUri);
    }

    private static final int MOVIE_LOADER = 0; // identifies the Loader

    private DetailFragmentListener listener;
    private Uri movieUri;

    private TextView titleTextView;
    private TextView yearTextView;
    private TextView directorTextView;
    private TextView maincharTextView;
    private TextView actorsTextView;
    private TextView genreTextView;
    private TextView languageTextView;
    private TextView synopsisTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DetailFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true); // this fragment has menu items to display

        Bundle arguments = getArguments();

        if (arguments != null)
            movieUri = arguments.getParcelable(MainActivity.MOVIE_URI);

        // inflate DetailFragment's layout
        View view =
                inflater.inflate(R.layout.fragment_details, container, false);

        // get the EditTexts
        titleTextView = view.findViewById(R.id.titleTextView);
        yearTextView = view.findViewById(R.id.yearTextView);
        directorTextView = view.findViewById(R.id.directorTextView);
        maincharTextView = view.findViewById(R.id.mainCharacterTextView);
        actorsTextView = view.findViewById(R.id.actorsTextView);
        genreTextView = view.findViewById(R.id.genreTextView);
        languageTextView = view.findViewById(R.id.languageTextView);
        synopsisTextView = view.findViewById(R.id.synoppsisTextView);


        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        return view;
    }

    // display this fragment's menu items
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                listener.onEditMovie(movieUri); // pass Uri to listener
                return true;
            case R.id.action_delete:
                deleteMovie();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteMovie() {
        DialogFragment confirmDelete = new MyDialogFragment(movieUri, listener);
        confirmDelete.show(getFragmentManager(), "confirm delete");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;

        switch (id) {
            case MOVIE_LOADER:
                cursorLoader = new CursorLoader(getActivity(),
                        movieUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            int titleIndex = data.getColumnIndex(Movie.COLUMN_TITLE);
            int yearIndex = data.getColumnIndex(Movie.COLUMN_YEAR);
            int directorIndex = data.getColumnIndex(Movie.COLUMN_DIRECTOR);
            int maincharIndex = data.getColumnIndex(Movie.COLUMN_MAINCHAR);
            int actorsIndex = data.getColumnIndex(Movie.COLUMN_ACTORS);
            int genreIndex = data.getColumnIndex(Movie.COLUMN_GENRE);
            int languageIndex = data.getColumnIndex(Movie.COLUMN_LANGUAGE);
            int synopsisIndex = data.getColumnIndex(Movie.COLUMN_SYNOPSIS);


            // fill TextViews with the retrieved data
            titleTextView.setText(data.getString(titleIndex));
            yearTextView.setText(data.getString(yearIndex));
            directorTextView.setText(data.getString(directorIndex));
            maincharTextView.setText(data.getString(maincharIndex));
            actorsTextView.setText(data.getString(actorsIndex));
            genreTextView.setText(data.getString(genreIndex));
            languageTextView.setText(data.getString(languageIndex));
            synopsisTextView.setText(data.getString(synopsisIndex));

        }
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
