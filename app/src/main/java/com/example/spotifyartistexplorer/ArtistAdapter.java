/**
 * ArtistAdapter.java
 * Function: RecyclerView Adapter for displaying a list of artists with image, name, and overlay button.
 *
 * @author Lorenz Aparentado
 * @since 2023-12
 */
package com.example.spotifyartistexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    /**
     * List of artists to be displayed in the RecyclerView.
     */
    private List<Artist> artists;

    /**
     * Reference to the SearchFragment for launching artist details.
     */
    private SearchFragment searchFragment;

    /**
     * Constructs an ArtistAdapter with the given list of artists and SearchFragment.
     *
     * @param artists        List of artists to be displayed.
     * @param searchFragment Reference to the SearchFragment.
     */
    public ArtistAdapter(List<Artist> artists, SearchFragment searchFragment) {
        this.artists = artists;
        this.searchFragment = searchFragment;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    /**
     * Called to display the data at the specified position.
     *
     * @param holder   The ViewHolder that should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bindArtistData(holder, position);
        setOverlayClickListener(holder, position);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in the adapter.
     */
    @Override
    public int getItemCount() {
        return artists.size();
    }

    /**
     * Inflates a view and returns the corresponding ViewHolder.
     *
     * @param parent The parent ViewGroup.
     * @return The inflated View wrapped in a ViewHolder.
     */
    private View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
    }

    /**
     * Binds artist data to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    private void bindArtistData(ViewHolder holder, int position) {
        Artist artist = artists.get(position);

        // Load image using Picasso
        holder.textViewArtistName.setText(artist.getName());

        String imageUrl = artist.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageViewArtist);
        }
    }

    /**
     * Sets a click listener for the overlay button to launch artist details.
     *
     * @param holder   The ViewHolder containing the overlay button.
     * @param position The position of the item within the adapter's data set.
     */
    private void setOverlayClickListener(ViewHolder holder, int position) {
        holder.buttonOverlay.setOnClickListener(view -> launchArtistDetails(position));
    }

    /**
     * Launches the artist details fragment for the selected artist.
     *
     * @param position The position of the item within the adapter's data set.
     */
    private void launchArtistDetails(int position) {
        Artist artist = artists.get(position);
        searchFragment.launchArtistDetailsFragment(artist);
    }

    /**
     * ViewHolder class representing the views for each item in the RecyclerView.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewArtist;
        TextView textViewArtistName;
        Button buttonOverlay;

        /**
         * Constructs a ViewHolder with the given item view.
         *
         * @param itemView The item view for the ViewHolder.
         */
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonOverlay = itemView.findViewById(R.id.buttonOverlay);
            imageViewArtist = itemView.findViewById(R.id.imageViewArtist);
            textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        }
    }
}
