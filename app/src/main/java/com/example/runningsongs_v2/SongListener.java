package com.example.runningsongs_v2;

/*
 *  Ta klasa bedzie nasluchiwac czy piosenka jakas leci
 *  przynajmniej powinna
 *
 * */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**  Klasa reprezentująca obiekt 'nasłuchujący' obecnie grającą aplikację w celu pobrania metadanych utworu
 *
 */

public final class SongListener {

    public SongListenerDelegate delegate = null;  /**< \Klasa 'odbierająca' obiekt typu 'Song' w metodzie 'onSongReceived' */
    private Activity activity;  /**< \ */

    /** \brief Konstruktor pobierający nasłuchujące Activity
     *
     * Tu trochę bardziej wymyślny opis
     * @param activity Obiekt Activity
     */

    public SongListener(Activity activity) {
        this.activity = activity;
    }

    /**  Metoda startująca nasłuchiwanie
     *
     */

    public void start() {
        Log.v("tag", "Startuje listenera");
        IntentFilter iF = new IntentFilter();

        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");
        iF.addAction("com.spotify.music.metadatachanged");

        activity.registerReceiver(mReceiver, iF);
    }

    /**  Metoda stopująca nasłuchiwanie
     *
     */

    public void stop() {
        Log.v("tag", "Stopuje listenera");
        activity.unregisterReceiver(mReceiver);
    }

    /**  Obiekt odbierący komunikaty od odtwarzającej aplikacji muzycznej
     *
     */

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");
            Log.v("tag ", action + " / " + cmd);
            String artist = intent.getStringExtra("artist");
            String album = intent.getStringExtra("album");
            String track = intent.getStringExtra("track");

            if (track == null) {
                return;
            }
            if (album == null) {
                album = "Nieznany album";
            }
            if (artist == null) {
                artist = "Nieznany wykonawca";
            }

            Song song = new Song(track, artist, album);
            Log.v("tag", artist + ":" + album + ":" + track);
            if (delegate != null) {
                delegate.onSongReceived(song);
            }
        }
    };

}
