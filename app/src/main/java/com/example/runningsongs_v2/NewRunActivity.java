package com.example.runningsongs_v2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.runningsongs_v2.R.id.chronometer2;

public class NewRunActivity extends AppCompatActivity implements SongListenerDelegate, OnMapReadyCallback {
    DBHelper dbHelper;
    SQLiteDatabase db;
    FragmentActivity FA = new FragmentActivity();

    private TextView textView;
    private TextView textView2;
    private TextView songTitleTextView;
    private Button btn_start;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver broadcastReceiver2;
    private static String Tag;
    private String distance;
    private String time;
    private String date;
    private SongListener songListener;
    private List<SongStamp> songStamps;
    private List<GeoStamp> geoStamps;
    private GPS_Service myService;
    private boolean bounded;
    private SongStamp awaitingSongStamp = null;

    private GoogleMap mMap;
    GeoStamp location;
    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newrun);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_start = (Button) findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textView);
        songTitleTextView = (TextView) findViewById(R.id.textView8);
        songListener = new SongListener(this);
        songListener.delegate = this;
        songListener.start();
        songStamps = new ArrayList<SongStamp>();

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (geoStamps == null) {
                            geoStamps = new ArrayList<>();
                        }
                        GeoStamp g = new GeoStamp(geoStamps.size(), getLocation());
                        geoStamps.add(g);
            }
        }, 0, 10000);

        if(!runtime_permissions()){
            enable_buttons();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    distance = String.valueOf(intent.getExtras().get("coordinates"));
                    textView.setText("" +intent.getExtras().get("coordinates"));

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));

        if(broadcastReceiver2 == null){
            broadcastReceiver2 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Float latitude = intent.getExtras().getFloat("latitude");
                    Float longitude = intent.getExtras().getFloat("longitude");
                    newSongLocationReceived(new LatLng(latitude, longitude));
                }
            };
            registerReceiver(broadcastReceiver2, new IntentFilter("location_demand"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
        Log.d(Tag,"Destroyed Activity");
    }

    @Override
    protected void onStart() {
        super.onStart();


//        Intent mIntent = new Intent(this, GPS_Service.class);
//        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    };

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bounded = false;
            myService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bounded = true;
//            GPS_Service.LocalBinder mLocalBinder = (GPS_Service.LocalBinder)service;
//            myService = mLocalBinder.getServerInstance();
        }
    };

    public void stopTrackerApplication(View view) {
        DBHelper dbHandler = new DBHelper(this, null, null, 1);
        Intent i = new Intent(getApplicationContext(),GPS_Service.class);
        ((Chronometer) findViewById(chronometer2)).stop();
        stopService(i);
        songListener.stop();
        doStuff();
        Log.d("g53mdp","Stop Service");
    }

    private void doStuff(){
        DBHelper dbHandler = new DBHelper(this, null, null, 1);
        double distance = Double.parseDouble(textView.getText().toString());
        String time =  ((Chronometer) findViewById(chronometer2)).getText().toString();
        String date = new SimpleDateFormat("dd--MM--yyyy").format(new Date());

        List<GeoStamp> fakeStamps = Arrays.asList(GeoStamp.getFakeStamps());

        RunnerTracker runnerTracker = new RunnerTracker(distance,date,time, songStamps, geoStamps);

        dbHandler.addRunnerTracker(runnerTracker);
    }

    private void enable_buttons() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),GPS_Service.class);
                startService(i);

                ((Chronometer) findViewById(chronometer2)).setBase(SystemClock.elapsedRealtime());
                ((Chronometer) findViewById(chronometer2)).start();
            }
        });

    }


    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }
    }

    public void newLocationReceived(LatLng nLocation) {
        GeoStamp g = new GeoStamp(geoStamps.size(), nLocation);
        geoStamps.add(g);
    }

    public void setSongMarker(SongStamp stamp) {
        if (mMap == null) { return; }

        MarkerOptions options = new MarkerOptions()
                .position(stamp.getLatLng())
                .draggable(false)
                .title(stamp.getSong().title)
                .snippet(stamp.getSong().artist);

        Marker marker = mMap.addMarker(options);
    }

    private void newSongLocationReceived(LatLng location) {
        awaitingSongStamp.setLatitude(location.latitude);
        awaitingSongStamp.setLongitude(location.longitude);
        songStamps.add(awaitingSongStamp);
        setSongMarker(awaitingSongStamp);

        awaitingSongStamp = null;
    }

    @Override
    public void onSongReceived(Song song) {
        if (song.title.isEmpty() || song.artist.isEmpty()) { return; }
        //Toast.makeText(this, song.title, Toast.LENGTH_SHORT).show();
        songTitleTextView.setText(song.title);
        Integer id = songStamps.size();

        awaitingSongStamp = new SongStamp(id, song);
        LatLng loc = getLocation();
        if (loc == null) { return; }
        newSongLocationReceived(loc);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        LatLng myPosition;


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        LatLng location = getLocation();
        if (location == null) { return; }
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 15);
        mMap.animateCamera(yourLocation);
    }

    private LatLng getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            return new LatLng(latitude, longitude);
        }
        return null;
    }
}
