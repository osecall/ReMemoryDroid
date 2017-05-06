package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

        InternetPermissos();
        AudioRecordPermissos();
        WriteStoragePermissos();
        ReadStoragePermissos();


        //Comprobar si hi ha Internet
        /*

        if(!isThereInternet()){
            AlertDialog.Builder Dialeg = new AlertDialog.Builder(LanguageActivity.this);
            Dialeg.setCancelable(true).setMessage(R.string.InternetNeeded)
                    .setNeutralButton("D'acord", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            LanguageActivity.this.finish();
                            startActivity(new Intent(Intent.ACTION_MAIN));
                        }
                    }).show();
        }*/

        //------------------------------------------

        btcaIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locale = new Locale("ca");
                showToast("Ha sel·leccionat Català", true);
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
                showToast("Ha seleccionado Español", true);
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
                showToast("You chose English as a Language", true);
                config.setLocale(locale);
                getResources().updateConfiguration(config,null);
                Intent inici = new Intent(LanguageActivity.this, SignInActivity.class);
                startActivity(inici);
            }
        });

    }


    private boolean isThereInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo actNetInfo2 = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(actNetInfo!=null){
            if(actNetInfo.isConnected()) return true;
        }
        else if (actNetInfo2!=null){
            if(actNetInfo2.isConnected()) return true;
        }
        return false;

    }
}
