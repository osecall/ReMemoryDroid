package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private final long SPLASH_SCREEN_DELAY = 2000; //Modifica el valor del temps d'espera

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los SPLASH_SCREEN_DELAY segundos, pasamos a la actividad principal de la aplicación
                Intent intent = new Intent(SplashActivity.this, LanguageActivity.class);

                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_DELAY);

    }
}
