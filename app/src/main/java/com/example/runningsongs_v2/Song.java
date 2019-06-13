package com.example.runningsongs_v2;

import java.io.Serializable;

/**  Klasa reprezentująca pojedynczy utwór implementująca interfejs Serializable
 *
 */

public final class Song implements Serializable {
    public String title;  /**< \Tytuł utworu */
    public String artist;  /**< \Wykonawca utworu */
    public String album;  /**< \Album utworu */

    /** \brief Konstruktor tworzący utwór na podstawie podanych danych.
     *
     * @param title Tytuł
     * @param artist Wykonawca
     * @param album Nazwa albumu
     */

    public Song(String title, String artist, String album) {
        this.title = title;
        this.artist = artist;
        this.album = album;
    }
}
