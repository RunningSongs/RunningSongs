package com.example.runningsongs_v2;

import android.content.Intent;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**  Klasa reprezentująca serwis GPS pobierający aktualną pozycję użytkownika
 *
 */

public class welcomeScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginScreen = new Intent(welcomeScreen.this ,MainActivity.class);
                startActivity(loginScreen);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
