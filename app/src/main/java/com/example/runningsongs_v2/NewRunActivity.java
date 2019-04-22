package com.example.runningsongs_v2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.runningsongs_v2.R.id.chronometer2;

public class NewRunActivity extends AppCompatActivity implements SongListenerDelegate {
    DBHelper dbHelper;
    SQLiteDatabase db;

    private TextView textView;
    private TextView textView2;
    private Button btn_start;
    private BroadcastReceiver broadcastReceiver;
    private static String Tag;
    private String distance;
    private String time;
    private String date;
    private SongListener songListener;
    private List<SongStamp> songStamps;
    private List<GeoStamp> geoStamps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newrun);
        btn_start = (Button) findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textView);
        songListener = new SongListener(this);
        songListener.delegate = this;
        songListener.start();
        songStamps = new ArrayList<SongStamp>();

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
                    textView.setText("\n" +intent.getExtras().get("coordinates"));

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
        Log.d(Tag,"Destroyed Activity");
    }



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

    @Override
    public void onSongReceived(Song song) {
        if (song.title.isEmpty() || song.artist.isEmpty()) { return; }
        Toast.makeText(this, song.title, Toast.LENGTH_SHORT).show();
        Integer id = songStamps.size();
        LatLng latLng = new LatLng(2, 2);

        SongStamp s = new SongStamp(id, song, latLng);
        songStamps.add(s);
    }
}
