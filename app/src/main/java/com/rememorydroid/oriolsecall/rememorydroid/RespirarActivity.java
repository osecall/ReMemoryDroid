package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RespirarActivity extends BaseActivity {

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

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                Reproduccio = R.raw.respirar234_ca;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                Reproduccio = R.raw.respirar234_es;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                Reproduccio = R.raw.respirar234_en;

            }
            else{
                Reproduccio = R.raw.respirar234_es;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Segon","Segon");
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Tercer")){
            ivRespirar.setImageResource(R.drawable.abstracte3);

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                Reproduccio = R.raw.respirar234_ca;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                Reproduccio = R.raw.respirar234_es;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                Reproduccio = R.raw.respirar234_en;

            }
            else{
                Reproduccio = R.raw.respirar234_es;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Tercer","Tercer");
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Quarta")){
            ivRespirar.setImageResource(R.drawable.abstracte4);

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                Reproduccio = R.raw.respirar234_ca;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                Reproduccio = R.raw.respirar234_es;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                Reproduccio = R.raw.respirar234_en;

            }
            else{
                Reproduccio = R.raw.respirar234_es;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Quarta","Quarta");
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Curta1")){
            ivRespirar.setImageResource(R.drawable.abstracte5);

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                Reproduccio = R.raw.respirar1_ca;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                Reproduccio = R.raw.respirar1_es;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                Reproduccio = R.raw.respirar1_en;

            }
            else{
                Reproduccio = R.raw.respirar1_es;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this,VisualitzarActivity.class);
            intentRespirar.putExtra("Curta1","Curta1");
            DialogInstruccionsRespirar();
        }
        else if(getIntent().hasExtra("Curta2")){
            ivRespirar.setImageResource(R.drawable.abstracte6);

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                Reproduccio = R.raw.respirar234_ca;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                Reproduccio = R.raw.respirar234_es;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                Reproduccio = R.raw.respirar234_en;

            }
            else{
                Reproduccio = R.raw.respirar234_es;
            }

            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this,TestActivity.class);
            intentRespirar.putExtra("SegonTest","SegonTest");
            DialogInstruccionsRespirar();
        }
        else{

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                Reproduccio = R.raw.respirar1_ca;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                Reproduccio = R.raw.respirar1_es;

            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                Reproduccio = R.raw.respirar1_en;

            }
            else{
                Reproduccio = R.raw.respirar1_es;
            }


            mp = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this, VisualitzarActivity.class);
            //ReproduirMissatge(mp);
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
                ReproduirMissatge(mp);
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
                finish();
            }
        });
        mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
    }
}
