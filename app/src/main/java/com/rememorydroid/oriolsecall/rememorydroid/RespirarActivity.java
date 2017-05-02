package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class RespirarActivity extends AppCompatActivity {

    private Intent intentRespirar;
    private int Reproduccio;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respirar);

        ImageView ivRespirar = (ImageView) findViewById(R.id.ivRespirar);
        intentRespirar = new Intent(RespirarActivity.this, VisualitzarFragmentsActivity.class);
        mp=new MediaPlayer();

        //Configurem els elements que apareixeran i es reproduiran en funció si és la segona vegada que es crida la activity
        //d'aquesta forma ens evitem duplicar classes (activities)
        if(getIntent().hasExtra("Segon")){
            ivRespirar.setImageResource(R.drawable.abstracte2);

            if(Locale.getDefault().getLanguage().toString().matches("ca")){
                Reproduccio = R.raw.respirar234;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("es")){
                Reproduccio = R.raw.respirar234;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("en")){
                Reproduccio = R.raw.respirar234;

            }
            else{
                Reproduccio = R.raw.respirar234;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Segon","Segon");
            ReproduirMissatge(mp);
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Tercer")){
            ivRespirar.setImageResource(R.drawable.abstracte3);

            if(Locale.getDefault().getLanguage().toString().matches("ca")){
                Reproduccio = R.raw.respirar234;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("es")){
                Reproduccio = R.raw.respirar234;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("en")){
                Reproduccio = R.raw.respirar234;

            }
            else{
                Reproduccio = R.raw.respirar234;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Tercer","Tercer");
            ReproduirMissatge(mp);
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Quarta")){
            ivRespirar.setImageResource(R.drawable.abstracte4);

            if(Locale.getDefault().getLanguage().toString().matches("ca")){
                Reproduccio = R.raw.respirar234;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("es")){
                Reproduccio = R.raw.respirar234;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("en")){
                Reproduccio = R.raw.respirar234;

            }
            else{
                Reproduccio = R.raw.respirar234;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Quarta","Quarta");
            ReproduirMissatge(mp);
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Curta1")){
            ivRespirar.setImageResource(R.drawable.abstracte5);

            if(Locale.getDefault().getLanguage().toString().matches("ca")){
                Reproduccio = R.raw.respirar1;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("es")){
                Reproduccio = R.raw.respirar1;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("en")){
                Reproduccio = R.raw.respirar1;

            }
            else{
                Reproduccio = R.raw.respirar1;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this,VisualitzarActivity.class);
            intentRespirar.putExtra("Curta1","Curta1");
            ReproduirMissatge(mp);
            DialogInstruccionsRespirar();
        }
        else if(getIntent().hasExtra("Curta2")){
            ivRespirar.setImageResource(R.drawable.abstracte6);

            if(Locale.getDefault().getLanguage().toString().matches("ca")){
                Reproduccio = R.raw.respirar234;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("es")){
                Reproduccio = R.raw.respirar234;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("en")){
                Reproduccio = R.raw.respirar234;

            }
            else{
                Reproduccio = R.raw.respirar234;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this,TestActivity.class);
            intentRespirar.putExtra("SegonTest","SegonTest");
            ReproduirMissatge(mp);
            DialogInstruccionsRespirar();
        }
        else{

            if(Locale.getDefault().getLanguage().toString().matches("ca")){
                Reproduccio = R.raw.respirar1;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("es")){
                Reproduccio = R.raw.respirar1;

            }
            else if(Locale.getDefault().getLanguage().toString().matches("en")){
                Reproduccio = R.raw.respirar1;

            }
            else{
                Reproduccio = R.raw.respirar1;
            }


            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this, VisualitzarActivity.class);
            ReproduirMissatge(mp);
            DialogInstruccionsRespirar();
        }
    }

    private void DialogInstruccionsRespirar(){
        final AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(RespirarActivity.this);
        LayoutInflater factory = LayoutInflater.from(RespirarActivity.this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);
        tv.setText(R.string.InstructonsBreathing);


        DialegFormControl
                .setCancelable(false)
                .setView(textEntryView)
                .setTitle(getString(R.string.Attention));

        final AlertDialog alerta = DialegFormControl.create();

        alerta.show();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });
    }

    private void ReproduirMissatge(final MediaPlayer mp){
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.stop();
                mp.release();
                startActivity(intentRespirar);
            }
        });
        mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
        mp=null;
    }
}
