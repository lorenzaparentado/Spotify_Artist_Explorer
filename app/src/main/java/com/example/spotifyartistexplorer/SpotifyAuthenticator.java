/**
 * SpotifyAuthenticator.java
 * Function: This class handles the authentication process with the Spotify API using client credentials.
 * It performs the necessary steps to obtain an access token for making authenticated requests.
 *
 * @author Lorenz Aparentado
 * @since 2023-12
 */
package com.example.spotifyartistexplorer;

import android.content.Context;
import android.util.Base64;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SpotifyAuthenticator {

    private static final String BASE_URL = "https://accounts.spotify.com/api/token";
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;

    private final RequestQueue requestQueue;
    private final Context context;

    /**
     * Constructor for SpotifyAuthenticator.
     *
     * @param context The application context.
     */
    public SpotifyAuthenticator(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        loadCredentials();
    }
    /**
     * Loads client_id and client_secret for SpotifyAuthenticator.
     *
     */
    private void loadCredentials() {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.spotify);
            Properties properties = new Properties();
            properties.load(inputStream);

            CLIENT_ID = properties.getProperty("client_id");
            CLIENT_SECRET = properties.getProperty("client_secret");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initiates the Spotify API authentication process.
     *
     * @param callback The callback to handle the authentication result or errors.
     */
    public void authenticate(final AuthCallback callback) {
        String url = BASE_URL;

        StringRequest stringRequest = createStringRequest(callback);

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    /**
     * Creates a StringRequest for Spotify API authentication.
     *
     * @param callback The callback to handle the response or errors.
     * @return The StringRequest for Spotify API authentication.
     */
    private StringRequest createStringRequest(AuthCallback callback) {
        return new StringRequest(Request.Method.POST, BASE_URL,
                response -> handleSuccessResponse(response, callback),
                error -> handleError(error, callback)) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            public byte[] getBody() {
                return createRequestBody();
            }

            @Override
            public Map<String, String> getHeaders() {
                return createRequestHeaders();
            }
        };
    }

    /**
     * Handles the successful response from the Spotify API authentication request.
     *
     * @param response The response from the authentication request.
     * @param callback The callback to handle the authentication result.
     */
    private void handleSuccessResponse(String response, AuthCallback callback) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String accessToken = jsonResponse.getString("access_token");
            callback.onSuccess(accessToken);
        } catch (JSONException e) {
            callback.onError(e.getMessage());
        }
    }

    /**
     * Handles errors that occur during the Spotify API authentication process.
     *
     * @param error    The error that occurred during the authentication process.
     * @param callback The callback to handle the error.
     */
    private void handleError(VolleyError error, AuthCallback callback) {
        callback.onError(error.getMessage());
    }

    /**
     * Creates the request body for the Spotify API authentication request.
     *
     * @return The byte array representing the request body.
     */
    private byte[] createRequestBody() {
        String body = "grant_type=client_credentials";
        return body.getBytes();
    }

    /**
     * Creates the headers for the Spotify API authentication request.
     *
     * @return The map of headers.
     */
    private Map<String, String> createRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", authHeader);
        return headers;
    }

    /**
     * Interface for handling authentication callbacks.
     */
    public interface AuthCallback {
        /**
         * Called when Spotify API authentication is successful.
         *
         * @param accessToken The access token obtained from the authentication process.
         */
        void onSuccess(String accessToken);

        /**
         * Called when an error occurs during Spotify API authentication.
         *
         * @param errorMessage The error message describing the issue.
         */
        void onError(String errorMessage);
    }
}
