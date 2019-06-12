package com.example.runningsongs_v2;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**  Klasa reprezentująca obiekt opisujący pozycję piosenki na mapie
 *
 */

public final class SongStamp implements Serializable {

    private Integer id;  /**< \Unikatowy identyfikator obiektu */
    private Song song;  /**< \Utwór */
    private Double latitude;  /**< \Pozycja Y na mapie */
    private Double longitude;  /**< \Pozycja X na mapie */

    /** \brief Konstruktor parametryczny tworzący pocisk na podstawie Identyfikatora, utworu i koordynatów.
     *
     * Tu trochę bardziej wymyślny opis
     * @param id Identyfikator
     * @param song Utwór
     * @param latLng Koordynaty
     */

    public SongStamp(Integer id, Song song, LatLng latLng) {
        this.id = id;
        this.song = song;
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    /** \brief Konstruktor parametryczny tworzący pocisk na podstawie Identyfikatora, utworu i koordynatów.
     *
     * Tu trochę bardziej wymyślny opis
     * @param id Identyfikator
     * @param song Utwór
     */

    public SongStamp(Integer id, Song song) {
        this.id = id;
        this.song = song;
    }

    /** \brief Konstruktor parametryczny tworzący obiekt na podstawie otrzymanego wyrażenia z bazy danych
     *
     * Tu trochę bardziej wymyślny opis
     * @param expression Wyrażenie
     */

    public SongStamp(String expression) {
        // Example 1:title-artist:12.9219-92.009;
        String[] parts = expression.split(":");

        String identifier = parts[0];
        id = Integer.parseInt(identifier);

        String[] songParts = parts[1].split("###");
        String title = songParts[0];
        String artist = songParts[1];
        song = new Song(title, artist, "album");

        String[] latLngParts = parts[2].split("###");
        String lat = latLngParts[0];
        String lng = latLngParts[1];
        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lng);
    }

    public Song getSong() { return song; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LatLng getLatLng() { return new LatLng(latitude, longitude); }

}
