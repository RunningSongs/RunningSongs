package com.example.runningsongs_v2;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryRunActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    ListView recipeList;
    private TextView textView2;

    List<RunnerTracker> trackerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runhistory);

        DBHelper dbHelper = new DBHelper(this, null, null, 1);
        db = dbHelper.getWritableDatabase();

        loadTrackers();
        setupView();

    }

    public void loadTrackers() {
        Cursor  c = db.rawQuery("select * from RunnerTracker2",null);
        trackerList = new ArrayList<>();

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                double distance = c.getDouble(1);
                int id = c.getInt(0);
                String date = c.getString(2);
                String time = c.getString(3);
                String locations = c.getString(4);
                String songs = c.getString(5);

                List<GeoStamp> nLocations = new ArrayList<>();
                String[] locationParts = locations.split(";");
                for (String part: locationParts) {
                    if (part.isEmpty()) { continue; }
                    nLocations.add(new GeoStamp(part));
                }

                List<SongStamp> nStamps = new ArrayList<SongStamp>();
                String[] songParts = songs.split(";");
                for (String part: songParts) {
                    if (part.isEmpty()) { continue; }
                    nStamps.add(new SongStamp(part));
                }

                RunnerTracker n = new RunnerTracker(distance, date, time, nStamps, nLocations);
                trackerList.add(n);
                c.moveToNext();
            }
        }
    }

    public void setupView(){
        Cursor  c = db.rawQuery("select * from RunnerTracker2",null);
        recipeList = (ListView) findViewById(R.id.ListView);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                double distance = c.getDouble(1);
                int id = c.getInt(0);
                String date = c.getString(2);
                String time = c.getString(3);
// Setup cursor adapter using cursor from last step
                CustomDataAdapter todoAdapter = new CustomDataAdapter(this, c);
// Attach cursor adapter to the ListView
                recipeList.setAdapter(todoAdapter);
                c.moveToNext();
            }
        }

        if (recipeList != null) {
            recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3)
                {
                    Log.d("CHUJ", String.valueOf(trackerList.get(position).getSongStamps().size()) + " songs");
                    //String value = (String)adapter.getItemAtPosition(position);
                    // assuming string and if you want to get the value on click of list item
                    // do what you intend to do on click of listview row
                    RunnerTracker tracker = trackerList.get(position);
                    launchResultsActivity(tracker);
                }
            });
        }
    }

    private void launchResultsActivity(RunnerTracker tracker) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tracker", tracker);
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
