package com.example.runningsongs_v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**  Klasa umożliwożliwiająca komunikację z lokalną bazą danych
 *
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;  /**< \Wersja bazy danych */
    private static final String DATABASE_NAME = "RunnerTrackerDB.db";  /**< \Nazwa bazy danych */
    public static final String TABLE_RUNNERTRACKER = "RunnerTracker2";  /**< \Nazwa projektu dla bazy danych */

    public static final String COLUMN_ID = "_id";  /**< \Nazwa kolumny 'id' */
    public static final String COLUMN_DISTANCE = "distance";  /**< \Nazwa kolumny 'distance' */
    public static final String COLUMN_DATE = "date";  /**< \Nazwa kolumny 'date' */
    public static final String COLUMN_TIME = "time";  /**< \Nazwa kolumny 'time' */
    public static final String COLUMN_LOCATIONS = "locations";  /**< \Nazwa kolumny 'locations' */
    public static final String COLUMN_SONG_STAMPS = "song_stamps";  /**< \Nazwa kolumny 'song_stamps' */

    /** \brief Konstruktor parametryczny tworzący pocisk na podstawie jego początku i końca.
     *
     * Tu trochę bardziej wymyślny opis
     * @param context Współrzędna OX dla początku pocisku
     * @param name Współrzędna OY dla początku pocisku
     * @param factory Współrzędna OX dla końca pocisku
     * @param version Współrzędna OY dla końca pocisku
     */

    public DBHelper(Context context, String name,
                    SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
//        String CREATE_RUNNERTRACKER_TABLE = "CREATE TABLE " +
//                TABLE_RUNNERTRACKER + "("
//                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_DISTANCE
//                + " INTEGER," + COLUMN_DATE + " TEXT,"+
//                COLUMN_TIME + " TEXT"+ ")";
        String CREATE_RUNNERTRACKER_TABLE = "CREATE TABLE " +
                TABLE_RUNNERTRACKER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_DISTANCE
                + " INTEGER," + COLUMN_DATE + " TEXT,"+
                COLUMN_TIME + " TEXT," + COLUMN_LOCATIONS + " TEXT," + COLUMN_SONG_STAMPS + " TEXT"+ ")";
        db.execSQL(CREATE_RUNNERTRACKER_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUNNERTRACKER);
            onCreate(db);
        }

    }

    /** \brief Konstruktor parametryczny tworzący pocisk na podstawie jego początku i końca.
     *
     * Tu trochę bardziej wymyślny opis
     * @param runnerTracker Współrzędna OX dla początku pocisku
     */

    public void addRunnerTracker(RunnerTracker runnerTracker) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DISTANCE, runnerTracker.getRunnerTrackerDistance());
        values.put(COLUMN_DATE, runnerTracker.getRunnerTrackerDate());
        values.put(COLUMN_TIME, runnerTracker.getRunnerTrackerTime());

        int idx = 0;
        String f = "";
        for(SongStamp s: runnerTracker.getSongStamps()) {
            // Example 1:title###artist:12.9219###;
            f += (String.valueOf(idx) + ":" + s.getSong().title + "###" + s.getSong().artist + ":" + String.valueOf(s.getLatLng().latitude) + "###" + String.valueOf(s.getLatLng().longitude) + ";");
            idx += 1;
        }
        values.put(COLUMN_SONG_STAMPS, f);

        idx = 0;
        f = "";
        for(GeoStamp g: runnerTracker.getGeoStamps()) {
            // Example 1:lat###lng;
            f += (String.valueOf(idx) + ":" + String.valueOf(g.getLatLng().latitude) + "###" + String.valueOf(g.getLatLng().longitude) + ";");
            idx += 1;
        }
        values.put(COLUMN_LOCATIONS, f);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_RUNNERTRACKER, null, values);

        db.close();
    }
}
