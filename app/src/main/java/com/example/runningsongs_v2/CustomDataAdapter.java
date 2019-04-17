package com.example.runningsongs_v2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;



public class CustomDataAdapter extends CursorAdapter {
    public CustomDataAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listdata, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvBody = view.findViewById(R.id.textView5);
        TextView tvPriority =  view.findViewById(R.id.textView6);
        TextView timeview =  view.findViewById(R.id.textView7);
        // Extract properties from cursor
        double distance = cursor.getDouble(cursor.getColumnIndexOrThrow("distance"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        String time= cursor.getString(cursor.getColumnIndexOrThrow("time"));
        // Populate fields with extracted properties
        tvBody.setText("Distance:\t"+Double.toString(distance)+"m");
        tvPriority.setText("Date:\t"+String.valueOf(date));
        timeview.setText("Time taken to Jog:\t"+String.valueOf(time));

    }
}
