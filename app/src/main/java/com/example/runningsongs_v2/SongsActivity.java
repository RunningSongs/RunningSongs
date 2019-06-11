package com.example.runningsongs_v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.runningsongs_v2.R.layout.song_cell;

public class SongsActivity extends AppCompatActivity {

    private List<Song> songs;
    private ListView list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            songs = (List<Song>)getIntent().getSerializableExtra("songs");
        }

        setContentView(R.layout.activity_songs);

        list = (ListView) findViewById(R.id.listView1);

        ArrayList<String> rows = new ArrayList<String>();
        for (Song s: songs) {
            rows.add(s.artist + " - " + s.title);
        }

        adapter = new ArrayAdapter<String>(this, song_cell, rows);

        list.setAdapter(adapter);
    }
}
