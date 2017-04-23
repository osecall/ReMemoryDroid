package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RespirarActivity extends AppCompatActivity {

    private Intent intentRespirar;
    private int Reproduccio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respirar);

        ImageView ivRespirar = (ImageView) findViewById(R.id.ivRespirar);
        intentRespirar = new Intent(RespirarActivity.this, VisualitzarFragmentsActivity.class);

        //Configurem els elements que apareixeran i es reproduiran en funció si és la segona vegada que es crida la activity
        //d'aquesta forma ens evitem duplicar classes (activities)
        if(getIntent().hasExtra("Segon")){
            ivRespirar.setImageResource(R.drawable.abstracte2);
            Reproduccio = R.raw.respirar2;
            MediaPlayer mp2 = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Segon","Segon");
            ReproduirMissatge(mp2);
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Tercer")){
            ivRespirar.setImageResource(R.drawable.abstracte3);
            Reproduccio = R.raw.respirar3;
            MediaPlayer mp3 = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Tercer","Tercer");
            ReproduirMissatge(mp3);
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Quarta")){
            ivRespirar.setImageResource(R.drawable.abstracte4);
            Reproduccio = R.raw.respirar4;
            MediaPlayer mp4 = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar.putExtra("Quarta","Quarta");
            ReproduirMissatge(mp4);
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Curta1")){
            ivRespirar.setImageResource(R.drawable.abstracte5);
            Reproduccio = R.raw.respirar5;
            MediaPlayer mpc1 = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this,VisualitzarActivity.class);
            intentRespirar.putExtra("Curta1","Curta1");
            ReproduirMissatge(mpc1);
            DialogInstruccionsRespirar();
        }
        else if(getIntent().hasExtra("Curta2")){
            ivRespirar.setImageResource(R.drawable.abstracte6);
            Reproduccio = R.raw.respirar5;
            MediaPlayer mpc2 = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this,TestActivity.class);
            intentRespirar.putExtra("SegonTest","SegonTest");
            ReproduirMissatge(mpc2);
            DialogInstruccionsRespirar();
        }
        else{
            Reproduccio = R.raw.respirar1;
            MediaPlayer mp1 = MediaPlayer.create(RespirarActivity.this,Reproduccio);
            intentRespirar = new Intent(RespirarActivity.this, VisualitzarActivity.class);
            intentRespirar.putExtra("Primer","Primer");
            ReproduirMissatge(mp1);
            DialogInstruccionsRespirar();
        }
    }

    private void DialogInstruccionsRespirar(){
        final AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(RespirarActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        tv.setText(R.string.InstructonsBreathing);
        DialegFormControl
                .setCancelable(true)
                .setView(textEntryView)
                .setTitle(getString(R.string.Attention))
                //.setMessage(R.string.InstructonsBreathing)
                /*
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        arg0.dismiss();
                    }
                })*/
                .show();
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
        finish();
    }
}
