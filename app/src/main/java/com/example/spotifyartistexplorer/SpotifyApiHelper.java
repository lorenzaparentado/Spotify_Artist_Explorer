/**
 * SpotifyApiHelper.java
 * Function: This class provides methods to interact with the Spotify API for artist search.
 * It handles building URLs, making API requests, and parsing responses.
 *
 * @author Lorenz Aparentado
 * @since 2023-12
 */
package com.example.spotifyartistexplorer;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpotifyApiHelper {

    private static final String BASE_URL = "https://api.spotify.com/v1/search";
    private static final String SEARCH_TYPE = "artist";
    private static final String MARKET = "US";
    private static final int LIMIT = 20;

    private final Context context;
    private final String accessToken;

    /**
     * Constructor for SpotifyApiHelper.
     *
     * @param context      The application context.
     * @param accessToken  The Spotify API access token.
     */
    public SpotifyApiHelper(Context context, String accessToken) {
        this.context = context;
        this.accessToken = accessToken;
    }

    /**
     * Interface for handling search callbacks.
     */
    public interface SearchCallback {
        /**
         * Called when the artist search is successful.
         *
         * @param artists The list of artists matching the search query.
         */
        void onSuccess(List<Artist> artists);

        /**
         * Called when an error occurs during the artist search.
         *
         * @param errorMessage The error message describing the issue.
         */
        void onError(String errorMessage);
    }

    /**
     * Performs a search for artists on Spotify.
     *
     * @param query    The search query for artists.
     * @param callback The callback to handle the search results or errors.
     */
    public void searchArtists(String query, SearchCallback callback) {
        String url = buildSearchUrl(query);

        JsonObjectRequest jsonObjectRequest = createJsonObjectRequest(url, callback);

        // Add the request to the RequestQueue
        getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * Creates a JsonObjectRequest for artist search.
     *
     * @param url      The URL for the artist search request.
     * @param callback The callback to handle the response or errors.
     * @return The JsonObjectRequest for the artist search.
     */
    private JsonObjectRequest createJsonObjectRequest(String url, SearchCallback callback) {
        return new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<Artist> artists = parseResponse(response);
                        callback.onSuccess(artists);
                    } catch (JSONException e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> callback.onError(error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                return createHeaders();
            }
        };
    }

    /**
     * Creates the headers for the API request, including the Authorization header with the access token.
     *
     * @return The map of headers.
     */
    private Map<String, String> createHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        return headers;
    }

    /**
     * Builds the URL for the artist search request.
     *
     * @param query The search query for artists.
     * @return The formatted URL for the artist search request.
     */
    private String buildSearchUrl(String query) {
        return String.format("%s?q=%s&type=%s&market=%s&limit=%d", BASE_URL, query, SEARCH_TYPE, MARKET, LIMIT);
    }

    /**
     * Parses the JSON response from the artist search request.
     *
     * @param response The JSON response from the artist search request.
     * @return The list of artists parsed from the response.
     * @throws JSONException If an error occurs while parsing the JSON response.
     */
    private List<Artist> parseResponse(JSONObject response) throws JSONException {
        List<Artist> artists = new ArrayList<>();
        JSONObject artistsObject = response.getJSONObject("artists");

        JSONArray itemsArray = artistsObject.getJSONArray("items");
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject artistObject = itemsArray.getJSONObject(i);
            String name = artistObject.getString("name");

            String imageUrl = "";
            JSONArray imagesArray = artistObject.getJSONArray("images");
            if (imagesArray.length() > 0) {
                imageUrl = imagesArray.getJSONObject(0).getString("url");
            }

            int followers = artistObject.getJSONObject("followers").getInt("total");

            artists.add(new Artist(name, imageUrl, followers));
        }

        return artists;
    }

    /**
     * Gets the RequestQueue for making API requests using Volley.
     *
     * @return The RequestQueue.
     */
    private RequestQueue getRequestQueue() {
        return Volley.newRequestQueue(context.getApplicationContext());
    }
}
