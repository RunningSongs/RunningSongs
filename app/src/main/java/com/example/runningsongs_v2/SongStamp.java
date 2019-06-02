package com.example.runningsongs_v2;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public final class SongStamp implements Serializable {

    private Integer id;
    private Song song;
    private Double latitude;
    private Double longitude;

    public SongStamp(Integer id, Song song, LatLng latLng) {
        this.id = id;
        this.song = song;
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    public SongStamp(Integer id, Song song) {
        this.id = id;
        this.song = song;
    }

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
