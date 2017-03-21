package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton btcaIdioma, btesIdioma;
    private Locale locale;
    private Configuration config = new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btcaIdioma = (ImageButton) findViewById(R.id.btcaIdioma);
        btesIdioma = (ImageButton) findViewById(R.id.btesIdioma);

        btcaIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("ca");

                Toast.makeText(MainActivity.this,
                        "Ha sel·leccionat Català", Toast.LENGTH_SHORT).show();
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
            }
        });

        btesIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("es");

                Toast.makeText(MainActivity.this,
                        "Ha seleccionado Español", Toast.LENGTH_SHORT).show();
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
            }
        });

    }

}
