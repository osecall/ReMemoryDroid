package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;

import java.util.Locale;

public class LanguageActivity extends BaseActivity {

    private ImageButton btcaIdioma, btesIdioma, btenIdioma;
    private Locale locale;
    private Configuration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        config = new Configuration();

        btcaIdioma = (ImageButton) findViewById(R.id.btcaIdioma);
        btesIdioma = (ImageButton) findViewById(R.id.btesIdioma);
        btenIdioma = (ImageButton) findViewById(R.id.btenIdioma);

        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LanguageActivity.this);
            builder.setMessage(R.string.InternetNeeded)
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            finish();
                        }
                    });
            builder.create().show();

        }
        InternetPermissos();
        AudioRecordPermissos();
        WriteStoragePermissos();
        ReadStoragePermissos();



        btcaIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("ca");
                showToast("Ha sel·leccionat Català", true);
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                GuardarLlenguatge("ca");
                Intent inici = new Intent(LanguageActivity.this, SignInActivity.class);
                startActivity(inici);
            }
        });

        btesIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("es","ES");
                showToast("Ha seleccionado Español", true);
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                GuardarLlenguatge("es");
                Intent inici = new Intent(LanguageActivity.this, SignInActivity.class);
                startActivity(inici);
            }
        });

        btenIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("en");
                showToast("You chose English as a Language", true);
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                GuardarLlenguatge("en");
                Intent inici = new Intent(LanguageActivity.this, SignInActivity.class);
                startActivity(inici);
            }
        });

        if (!ObtenirLlenguatge().toString().matches("non")) {
            startActivity(new Intent(LanguageActivity.this, SignInActivity.class));
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
