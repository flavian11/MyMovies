package com.flav.mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.flav.mymovies.data.DatabaseDescription.Movie;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface AddEditFragmentListener {
        void onAddEditCompleted(Uri contactUri);
    }

    // constant used to identify the Loader
    private static final int MOVIE_LOADER = 0;

    private AddEditFragmentListener listener;
    private Uri movieUri;
    private boolean addingNewMovie = true;

    private TextInputLayout titleTextInputLayout;
    private TextInputLayout yearTextInputLayout;
    private TextInputLayout directorTextInputLayout;
    private TextInputLayout maincharTextInputLayout;
    private TextInputLayout actorsTextInputLayout;
    private TextInputLayout genreTextInputLayout;
    private TextInputLayout languageTextInputLayout;
    private TextInputLayout synopsisTextInputLayout;


    private FloatingActionButton saveMovieFAB;

    private CoordinatorLayout coordinatorLayout; // used with SnackBars

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddEditFragmentListener) context;
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
        setHasOptionsMenu(true);

        View view =
                inflater.inflate(R.layout.fragment_add_edit, container, false);
        titleTextInputLayout = view.findViewById(R.id.titleTextInputLayout);
        titleTextInputLayout.getEditText().addTextChangedListener(
                titleChangedListener);

        yearTextInputLayout = view.findViewById(R.id.yearTextInputLayout);
        directorTextInputLayout = view.findViewById(R.id.directorTextInputLayout);
        maincharTextInputLayout = view.findViewById(R.id.mainCharacterTextInputLayout);
        actorsTextInputLayout = view.findViewById(R.id.actorsTextInputLayout);
        genreTextInputLayout = view.findViewById(R.id.genreTextInputLayout);
        languageTextInputLayout = view.findViewById(R.id.languageTextInputLayout);
        synopsisTextInputLayout = view.findViewById(R.id.synopsisTextInputLayout);

        saveMovieFAB = (FloatingActionButton) view.findViewById(
                R.id.saveFloatingActionButton);
        saveMovieFAB.setOnClickListener(saveMovieButtonClicked);
        updateSaveButtonFAB();

        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(
                R.id.coordinatorLayout);

        Bundle arguments = getArguments();

        if (arguments != null) {
            addingNewMovie = false;
            movieUri = arguments.getParcelable(MainActivity.MOVIE_URI);
        }

        if (movieUri != null)
            getLoaderManager().initLoader(MOVIE_LOADER, null, this);

        return view;
    }

    private final TextWatcher titleChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            updateSaveButtonFAB();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void updateSaveButtonFAB() {
        String input =
                titleTextInputLayout.getEditText().getText().toString();

        // if there is a title for the movie, show the FloatingActionButton
        if (input.trim().length() != 0)
            saveMovieFAB.show();
        else
            saveMovieFAB.hide();
    }

    private final View.OnClickListener saveMovieButtonClicked =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getView().getWindowToken(), 0);
                    saveMovie();
                }
            };

    private void saveMovie() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Movie.COLUMN_TITLE,
                titleTextInputLayout.getEditText().getText().toString());
        contentValues.put(Movie.COLUMN_YEAR,
                yearTextInputLayout.getEditText().getText().toString());
        contentValues.put(Movie.COLUMN_DIRECTOR,
                directorTextInputLayout.getEditText().getText().toString());
        contentValues.put(Movie.COLUMN_MAINCHAR,
                maincharTextInputLayout.getEditText().getText().toString());
        contentValues.put(Movie.COLUMN_ACTORS,
                actorsTextInputLayout.getEditText().getText().toString());
        contentValues.put(Movie.COLUMN_GENRE,
                genreTextInputLayout.getEditText().getText().toString());
        contentValues.put(Movie.COLUMN_LANGUAGE,
                languageTextInputLayout.getEditText().getText().toString());
        contentValues.put(Movie.COLUMN_SYNOPSIS,
                synopsisTextInputLayout.getEditText().getText().toString());

        if (addingNewMovie) {

            Uri newContactUri = getActivity().getContentResolver().insert(
                    Movie.CONTENT_URI, contentValues);

            if (newContactUri != null) {
                Snackbar.make(coordinatorLayout,
                        "Movie added successfully", Snackbar.LENGTH_LONG).show();
                listener.onAddEditCompleted(newContactUri);
            }
            else {
                Snackbar.make(coordinatorLayout,
                        "Error when adding a movie", Snackbar.LENGTH_LONG).show();
            }
        }
        else {

            int updatedRows = getActivity().getContentResolver().update(
                    movieUri, contentValues, null, null);

            if (updatedRows > 0) {
                listener.onAddEditCompleted(movieUri);
                Snackbar.make(coordinatorLayout,
                        "movie updated successfully", Snackbar.LENGTH_LONG).show();
            }
            else {
                Snackbar.make(coordinatorLayout,
                        "Error when updating the movie", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        switch (id) {
            case MOVIE_LOADER:
                return new CursorLoader(getActivity(),
                        movieUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
            default:
                return null;
        }
    }

    // called by LoaderManager when loading completes
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

            // fill EditTexts with the retrieved data
            titleTextInputLayout.getEditText().setText(
                    data.getString(titleIndex));
            yearTextInputLayout.getEditText().setText(
                    data.getString(yearIndex));
            directorTextInputLayout.getEditText().setText(
                    data.getString(directorIndex));
            maincharTextInputLayout.getEditText().setText(
                    data.getString(maincharIndex));
            actorsTextInputLayout.getEditText().setText(
                    data.getString(actorsIndex));
            genreTextInputLayout.getEditText().setText(
                    data.getString(genreIndex));
            languageTextInputLayout.getEditText().setText(
                    data.getString(languageIndex));
            synopsisTextInputLayout.getEditText().setText(
                    data.getString(synopsisIndex));

            updateSaveButtonFAB();
        }
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
