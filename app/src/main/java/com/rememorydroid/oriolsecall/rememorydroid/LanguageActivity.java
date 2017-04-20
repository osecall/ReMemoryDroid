package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    private ImageButton btcaIdioma, btesIdioma, btenIdioma;
    private Locale locale;
    private Configuration config = new Configuration();

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

    }

}
