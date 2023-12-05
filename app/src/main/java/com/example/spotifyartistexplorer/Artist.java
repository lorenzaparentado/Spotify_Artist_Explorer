/**
 * Artist.java
 * This class represents an artist with information such as name, image URL, and number of followers.
 *
 * @author Lorenz Aparentado
 * @since 2023-12
 */
package com.example.spotifyartistexplorer;

public class Artist {
    private final String name;
    private final String imageUrl;
    private final int followers;

    /**
     * Constructs an Artist object with the specified name, image URL, and number of followers.
     *
     * @param name      The name of the artist.
     * @param imageUrl  The URL of the artist's image.
     * @param followers The number of followers the artist has.
     */
    public Artist(String name, String imageUrl, int followers) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.followers = followers;
    }

    /**
     * Gets the name of the artist.
     *
     * @return The name of the artist.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the URL of the artist's image.
     *
     * @return The URL of the artist's image.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Gets the number of followers the artist has.
     *
     * @return The number of followers.
     */
    public int getNumberOfFollowers() {
        return followers;
    }
}
