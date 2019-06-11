package com.example.runningsongs_v2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView distanceTextView;
    private TextView timeTextView;
    private GoogleMap googleMap;
    private Button songsListButton;

    private RunnerTracker tracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        distanceTextView = (TextView)findViewById(R.id.textView);
        timeTextView = (TextView) findViewById(R.id.textView10);
        songsListButton = (Button) findViewById(R.id.songsListButton);
        songsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSongsList();
            }
        });

        tracker = getTracker();
        if (tracker == null) {
            Log.d("CO JEST GRANE", "Tracker jest nullem");
            return;
        }
        setActivityDetails();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setLocations();
        setSongs();
    }

    private RunnerTracker getTracker() {
        Bundle bundle = getIntent().getExtras();
        RunnerTracker nTracker = (RunnerTracker)bundle.getSerializable("tracker");
        return nTracker;
    }

    private void setActivityDetails() {
        Toast.makeText(this, tracker.getRunnerTrackerDate(), Toast.LENGTH_SHORT).show();
        // Ustawienie danych (czas, dystans itd)

        timeTextView.setText(tracker.getRunnerTrackerTime());
        distanceTextView.setText(Double.toString(tracker.getRunnerTrackerDistance()));
        songsListButton.setText(Integer.toString(tracker.getSongStamps().size()) + " piosenek");
    }

    private void setLocations() {
        if (googleMap == null) {
            return;
        }
        List<GeoStamp> stamps = tracker.getGeoStamps();

        // Ustawienie kamery na punkcie startowym
        GeoStamp firstStamp = stamps.get(0);
        if (firstStamp == null) {
            return;
        }
        LatLng firstLocation = firstStamp.getLatLng();

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(firstLocation, 15);
        googleMap.animateCamera(yourLocation);

        // Połączenie punktów
        PolylineOptions lineOptions = new PolylineOptions();
        List<LatLng> points = new ArrayList<>();

        for(GeoStamp s: stamps) {
            points.add(s.getLatLng());
        }

        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(Color.RED);

        // Drawing polyline in the Google Map for the entire route
        googleMap.addPolyline(lineOptions);

    }

    private void setSongs() {
        if (googleMap == null) {
            return;
        }
        // Ustawienie markerów z piosenkami na mapie kurwa
        List<SongStamp> stamps = tracker.getSongStamps();

        for(SongStamp s: stamps) {
            Song song = s.getSong();
            MarkerOptions options = new MarkerOptions()
                    .position(s.getLatLng())
                    .draggable(false)
                    .title(song.title)
                    .snippet(song.artist);

            Marker marker = googleMap.addMarker(options);
        }

    }

    private void openSongsList() {
        Intent i = new Intent(this, SongsActivity.class);
        Bundle b = new Bundle();
        List<SongStamp> stamps = tracker.getSongStamps();
        ArrayList<Song> songs = new ArrayList<>();
        for(SongStamp s: stamps) {
            songs.add(s.getSong());
        }

        b.putSerializable("songs", songs);
        i.putExtras(b);
        startActivity(i);
    }

}
