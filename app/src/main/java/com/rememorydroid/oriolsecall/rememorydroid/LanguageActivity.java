package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    private ImageButton btcaIdioma, btesIdioma, btenIdioma;
    private Locale locale;
    private Configuration config = new Configuration();

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2 ;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 3 ;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 4 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);


        btcaIdioma = (ImageButton) findViewById(R.id.btcaIdioma);
        btesIdioma = (ImageButton) findViewById(R.id.btesIdioma);
        btenIdioma = (ImageButton) findViewById(R.id.btenIdioma);

        btcaIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("ca");

                Toast.makeText(LanguageActivity.this,
                        "Ha sel·leccionat Català", Toast.LENGTH_LONG).show();
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                Intent inici = new Intent(LanguageActivity.this, SignInActivity.class);
                startActivity(inici);
            }
        });

        btesIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("es","ES");

                Toast.makeText(LanguageActivity.this,
                        "Ha seleccionado Español", Toast.LENGTH_LONG).show();
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                Intent inici = new Intent(LanguageActivity.this, SignInActivity.class);
                startActivity(inici);
            }
        });

        btenIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("en");

                Toast.makeText(LanguageActivity.this,
                        "You chose English as a Language", Toast.LENGTH_LONG).show();
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                Intent inici = new Intent(LanguageActivity.this, SignInActivity.class);
                startActivity(inici);
            }
        });



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            } case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;

            } case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            } case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }


}
