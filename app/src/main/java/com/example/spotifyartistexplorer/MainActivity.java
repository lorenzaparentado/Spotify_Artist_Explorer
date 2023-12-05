/**
 * MainActivity.java
 * Function: This activity serves as the main entry point for the Spotify Artist Explorer app.
 * It manages the fragment transactions for navigation.
 *
 * @author Lorenz Aparentado
 * @since 2023-12
 */
package com.example.spotifyartistexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down, this Bundle contains the data it most recently supplied
     *                           in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadInitialFragment();
    }

    /**
     * Loads the initial fragment when the activity is created.
     */
    private void loadInitialFragment() {
        loadFragment(new SearchFragment());
    }

    /**
     * Loads a fragment into the fragment container.
     *
     * @param fragment The fragment to be loaded.
     */
    private void loadFragment(Fragment fragment) {
        replaceFragment(fragment);
    }

    /**
     * Replaces the current fragment with a new fragment.
     *
     * @param fragment The new fragment to replace the current one.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        commitTransaction(transaction);
    }

    /**
     * Commits the provided fragment transaction.
     *
     * @param transaction The FragmentTransaction to be committed.
     */
    private void commitTransaction(FragmentTransaction transaction) {
        transaction.commit();
    }
}
