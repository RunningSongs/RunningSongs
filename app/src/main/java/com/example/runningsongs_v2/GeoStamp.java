package com.example.runningsongs_v2;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**  Klasa reprezentująca pozycję użytkownika
 *
 */

public final class GeoStamp implements Serializable {

    private Integer _id;  /**< \Identyfikator */
    private Double latitude;  /**< \Szerokość geograficzna */
    private Double longitude;  /**< \Długość geograficzna */

    /** \brief Konstruktor parametryczny tworzący obiekt na podstawie koordynatów i identyfikatora
     *
     * Tu trochę bardziej wymyślny opis
     * @param id Identyfikator
     * @param latLng Koordynaty
     */

    public GeoStamp(int id, LatLng latLng) {
        _id = id;
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    /** \brief Konstruktor parametryczny tworzący obiekt na podstawie wyrażenia z bazy danych.
     * Tu trochę bardziej wymyślny opis
     * @param expression Wyrażenie z bazy
     */

    public GeoStamp(String expression) {
        // Example 1:lat###lng
        String[] parts = expression.split(":");
        _id = Integer.parseInt(parts[0]);

        String[] latLngParts = parts[1].split("###");
        latitude = Double.parseDouble(latLngParts[0]);
        longitude = Double.parseDouble(latLngParts[1]);
    }

    public Integer getId() { return _id; }
    public void setId(Integer id) { _id = id; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LatLng getLatLng() { return new LatLng(latitude, longitude); }

    public static GeoStamp[] getFakeStamps() {
        GeoStamp[] fakeStamps = {
                new GeoStamp(0, new LatLng(53.446950, 14.491260)),
                new GeoStamp(0, new LatLng(53.448367, 14.487604)),
                new GeoStamp(0, new LatLng(53.452766, 14.484147)),
                new GeoStamp(0, new LatLng(53.454680, 14.485626)),
                new GeoStamp(0, new LatLng(53.456775, 14.490344)),
                new GeoStamp(0, new LatLng(53.457133, 14.495578)),
                new GeoStamp(0, new LatLng(53.455753, 14.497294)),
                new GeoStamp(0, new LatLng(53.451713, 14.500205)),
                new GeoStamp(0, new LatLng(53.450690, 14.500977)),
                new GeoStamp(0, new LatLng(53.449975, 14.496172)),
                new GeoStamp(0, new LatLng(53.444863, 14.497717)),
                new GeoStamp(0, new LatLng(53.447214, 14.491196))
        };
        return fakeStamps;
    }

}
