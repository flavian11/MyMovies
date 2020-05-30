package com.flav.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity
        implements MoviesFragment.MoviesFragmentListener,
        DetailFragment.DetailFragmentListener,
        AddEditFragment.AddEditFragmentListener {

    public static final String MOVIE_URI = "movie_uri";
    private MoviesFragment moviesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null &&
                findViewById(R.id.fragmentContainer) != null) {
            moviesFragment = new MoviesFragment();

            // add the fragment to the FrameLayout
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, moviesFragment);
            transaction.commit(); // display ContactsFragment
        } else {
            moviesFragment =
                    (MoviesFragment) getSupportFragmentManager().
                            findFragmentById(R.id.moviesFragment);
        }
    }

    // display DetailFragment for selected movie
    @Override
    public void onMovieSelected(Uri contactUri) {
        if (findViewById(R.id.fragmentContainer) != null) { // phone
            displayMovie(contactUri, R.id.fragmentContainer);
        }
        else { // tablet
            // removes top of back stack
            getSupportFragmentManager().popBackStack();

            displayMovie(contactUri, R.id.rightPaneContainer);
        }
    }

    // display AddEditFragment to add a new movie
    @Override
    public void onAddMovie() {
        if (findViewById(R.id.fragmentContainer) != null) // phone
            displayAddEditFragment(null, R.id.fragmentContainer);
        else // tablet
            displayAddEditFragment(null, R.id.rightPaneContainer);
    }

    // display a movie
    private void displayMovie(Uri movieUri, int viewID) {
        DetailFragment detailFragment = new DetailFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(MOVIE_URI, movieUri);
        detailFragment.setArguments(arguments);

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // display fragment for adding a new or editing an existing movie
    private void displayAddEditFragment(Uri movieUri, int viewID) {
        AddEditFragment addEditFragment = new AddEditFragment();

        if (movieUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MOVIE_URI, movieUri);
            addEditFragment.setArguments(arguments);
        }

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // return to movie list when displayed movie deleted
    @Override
    public void onMovieDeleted() {
        getSupportFragmentManager().popBackStack();
        moviesFragment.updateContactList(); // refresh contacts
    }

    // display the AddEditFragment to edit an existing contact
    @Override
    public void onEditMovie(Uri movieUri) {
        if (findViewById(R.id.fragmentContainer) != null) // phone
            displayAddEditFragment(movieUri, R.id.fragmentContainer);
        else // tablet
            displayAddEditFragment(movieUri, R.id.rightPaneContainer);
    }

    // update GUI after new movie or updated movie saved
    @Override
    public void onAddEditCompleted(Uri movieUri) {
        getSupportFragmentManager().popBackStack();
        moviesFragment.updateContactList();

        if (findViewById(R.id.fragmentContainer) == null) { // tablet
            getSupportFragmentManager().popBackStack();

            displayMovie(movieUri, R.id.rightPaneContainer);
        }
    }
}
