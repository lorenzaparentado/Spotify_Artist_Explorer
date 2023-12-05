/*
 * ArtistDetailsFragment.java
 * Function: Fragment for displaying detailed information about an artist, including name, number of followers, and image.
 *
 * @author Lorenz Aparentado
 * @since 2023-12
 */
package com.example.spotifyartistexplorer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class ArtistDetailsFragment extends Fragment {

    private static final String ARG_ARTIST_NAME = "artistName";
    private static final String ARG_NUMBER_OF_FOLLOWERS = "numberOfFollowers";
    private static final String ARG_IMAGE_URL = "imageUrl";

    /**
     * Creates a new instance of ArtistDetailsFragment with the provided artist details.
     *
     * @param artistName         The name of the artist.
     * @param numberOfFollowers The number of followers of the artist.
     * @param imageUrl           The URL of the artist's image.
     * @return A new instance of ArtistDetailsFragment.
     */
    public static ArtistDetailsFragment newInstance(String artistName, int numberOfFollowers, String imageUrl) {
        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARTIST_NAME, artistName);
        args.putInt(ARG_NUMBER_OF_FOLLOWERS, numberOfFollowers);
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to create the view for this fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views.
     * @param container          This is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_details, container, false);

        initializeUI(view);

        return view;
    }

    /**
     * Initializes the UI components with the artist details.
     *
     * @param view The root view of the fragment.
     */
    private void initializeUI(View view) {
        Bundle args = getArguments();
        if (args != null) {
            String artistName = args.getString(ARG_ARTIST_NAME, "");
            int numberOfFollowers = args.getInt(ARG_NUMBER_OF_FOLLOWERS, 0);
            String imageUrl = args.getString(ARG_IMAGE_URL, "");

            setTextViewText(view, R.id.textViewArtistName, artistName);
            setTextViewText(view, R.id.textViewFollowers, String.format("Followers: %,d", numberOfFollowers));
            loadImageWithPicasso(view, R.id.imageViewArtist, imageUrl);

            setBackButtonClickListener(view);
        }
    }

    /**
     * Sets the text of a TextView.
     *
     * @param view      The root view of the fragment.
     * @param textViewId The resource ID of the TextView.
     * @param text      The text to set.
     */
    private void setTextViewText(View view, int textViewId, String text) {
        TextView textView = view.findViewById(textViewId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    /**
     * Loads an image into an ImageView using Picasso.
     *
     * @param view       The root view of the fragment.
     * @param imageViewId The resource ID of the ImageView.
     * @param imageUrl    The URL of the image to load.
     */
    private void loadImageWithPicasso(View view, int imageViewId, String imageUrl) {
        ImageView imageView = view.findViewById(imageViewId);
        if (imageView != null) {
            Picasso.get().load(imageUrl).into(imageView);
        }
    }

    /**
     * Sets a click listener for the back button to pop the fragment from the back stack.
     *
     * @param view The root view of the fragment.
     */
    private void setBackButtonClickListener(View view) {
        ImageButton backButton = view.findViewById(R.id.buttonBack);
        if (backButton != null) {
            backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        }
    }

}
