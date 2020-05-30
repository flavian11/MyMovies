package com.flav.mymovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {

    private Uri movieUri;
    private DetailFragment.DetailFragmentListener listener;

    public MyDialogFragment(Uri movieUri, DetailFragment.DetailFragmentListener listener) {
        this.movieUri = movieUri;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Are You Sure?");
        builder.setMessage("This will permanently delete the contact");

        // provide an OK button that simply dismisses the dialog
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface dialog, int button) {

                        getActivity().getContentResolver().delete(
                                movieUri, null, null);
                        listener.onMovieDeleted(); // notify listener
                    }
                }
        );

        builder.setNegativeButton("Cancel", null);
        return builder.create(); // return the AlertDialog
    }
}
