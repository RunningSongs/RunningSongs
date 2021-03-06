package com.example.runningsongs_v2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.runningsongs_v2.DBHelper;
import com.example.runningsongs_v2.R;

/**  Activity otwierające aplikację
 *
 */

public class MainActivity extends AppCompatActivity {
    private Button button;
    DBHelper dbHelper;  /**< \Obiekt typu DBHelper */
    SQLiteDatabase db;  /**< \Baza danych */
    private TextView textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);

        DBHelper dbHelper = new DBHelper(this, null, null, 1);
        db = dbHelper.getWritableDatabase();

    }
    public void onClickStartNewRun(View view) {
        startActivity(new Intent(this, NewRunActivity.class));

    }
    public void onClickStartNewDay(View view) {
        startActivity(new Intent(this, LogsForEachDay.class));
    }
    public void onClickStartRunHistory(View view) {
        startActivity(new Intent(this, HistoryRunActivity.class));

    }
}
