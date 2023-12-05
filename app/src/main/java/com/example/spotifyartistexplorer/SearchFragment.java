/**
 * SearchFragment.java
 * Function: This fragment allows users to search for artists on Spotify, displaying results in a RecyclerView.
 * It handles user input, authentication, and API requests.
 *
 * @author Lorenz Aparentado
 * @since 2023-12
 */
package com.example.spotifyartistexplorer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchFragment extends Fragment {

    private SpotifyAuthenticator spotifyAuthenticator;
    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private SearchFragment searchFragment;

    /**
     * Default constructor for the SearchFragment.
     * Required empty public constructor.
     */
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The inflated view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializePicasso();
        initializeComponents(view);
    }

    /**
     * Initializes the Picasso library for image loading.
     */
    private void initializePicasso() {
        if (Picasso.get() == null) {
            Picasso.setSingletonInstance(new Picasso.Builder(requireContext()).build());
        }
    }

    /**
     * Initializes UI components and sets up the search functionality.
     *
     * @param view The root view of the fragment.
     */
    private void initializeComponents(View view) {
        spotifyAuthenticator = new SpotifyAuthenticator(requireContext());
        searchFragment = this;

        TextInputEditText editTextArtist = view.findViewById(R.id.editTextArtist);

        editTextArtist.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                            keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = editTextArtist.getText().toString().trim(); // Trim to remove leading and trailing whitespaces
                if (!query.isEmpty()) { // Check if the query is not empty
                    authenticateSpotify(query);
                    showArtist();
                } else {
                    // Handle empty query, e.g., show a message to the user
                    editTextArtist.setError("Please enter an artist name.");
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Authenticates the Spotify API and fetches artist information.
     *
     * @param query The search query for artists.
     */
    private void authenticateSpotify(String query) {
        spotifyAuthenticator.authenticate(new SpotifyAuthenticator.AuthCallback() {
            @Override
            public void onSuccess(String accessToken) {
                fetchArtistInfo(accessToken, query);
            }

            @Override
            public void onError(String errorMessage) {
                handleAuthenticationError(errorMessage);
            }
        });
    }

    /**
     * Fetches artist information from the Spotify API.
     *
     * @param accessToken The access token for Spotify API authentication.
     * @param query       The search query for artists.
     */
    private void fetchArtistInfo(String accessToken, String query) {
        SpotifyApiHelper spotifyApiHelper = new SpotifyApiHelper(requireContext(), accessToken);

        spotifyApiHelper.searchArtists(query, new SpotifyApiHelper.SearchCallback() {
            @Override
            public void onSuccess(List<Artist> artists) {
                displayArtists(artists);
            }

            @Override
            public void onError(String errorMessage) {
                handleApiRequestError(errorMessage);
            }
        });
    }

    /**
     * Displays the list of artists in a RecyclerView.
     *
     * @param artists The list of artists to display.
     */
    private void displayArtists(List<Artist> artists) {
        recyclerView = requireView().findViewById(R.id.recyclerViewArtists);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        artistAdapter = new ArtistAdapter(artists, this);
        recyclerView.setAdapter(artistAdapter);
    }

    /**
     * Updates the UI to show the searched artist.
     */
    private void showArtist() {
        TextInputEditText editTextArtist = requireView().findViewById(R.id.editTextArtist);
        String artistName = editTextArtist.getText().toString();

        TextView textViewResult = requireView().findViewById(R.id.textViewResult);
        textViewResult.setText("Showing Results For: " + artistName);
        hideKeyboard(editTextArtist);
    }

    /**
     * Hides the soft keyboard.
     *
     * @param view The view that currently has focus.
     */
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Handles authentication errors and logs them.
     *
     * @param errorMessage The error message from the authentication process.
     */
    private void handleAuthenticationError(String errorMessage) {
        // Handle authentication errors
        Log.e("SpotifyAuthenticator", errorMessage);
    }

    /**
     * Handles errors from Spotify API requests and logs them.
     *
     * @param errorMessage The error message from the Spotify API request.
     */
    private void handleApiRequestError(String errorMessage) {
        // Handle API request errors
        Log.e("SpotifyApiHelper", errorMessage);
    }

    /**
     * Launches the ArtistDetailsFragment to display detailed information about the selected artist.
     *
     * @param artist The selected artist for detailed information.
     */
    public void launchArtistDetailsFragment(Artist artist) {
        ArtistDetailsFragment detailsFragment = ArtistDetailsFragment.newInstance(artist.getName(), artist.getNumberOfFollowers(), artist.getImageUrl());
        replaceFragment(detailsFragment);
    }

    /**
     * Replaces the current fragment with a new fragment and adds the transaction to the back stack.
     *
     * @param fragment The new fragment to replace the current one.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
