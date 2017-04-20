package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
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
            intentRespirar.putExtra("Segon","Segon");
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Tercer")){
            ivRespirar.setImageResource(R.drawable.abstracte3);
            Reproduccio = R.raw.respirar3;
            intentRespirar.putExtra("Tercer","Tercer");
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Quarta")){
            ivRespirar.setImageResource(R.drawable.abstracte4);
            Reproduccio = R.raw.respirar4;
            intentRespirar.putExtra("Quarta","Quarta");
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Curta1")){
            ivRespirar.setImageResource(R.drawable.abstracte5);
            Reproduccio = R.raw.respirar5;
            intentRespirar = new Intent(RespirarActivity.this,VisualitzarActivity.class);
            intentRespirar.putExtra("Curta1","Curta1");
            DialogInstruccionsRespirar();
        }
        else if(getIntent().hasExtra("Curta2")){
            ivRespirar.setImageResource(R.drawable.abstracte6);
            Reproduccio = R.raw.respirar5;
            intentRespirar = new Intent(RespirarActivity.this,TestActivity.class);
            intentRespirar.putExtra("SegonTest","SegonTest");
            DialogInstruccionsRespirar();
        }
        else{
            Reproduccio = R.raw.respirar1;
            intentRespirar = new Intent(RespirarActivity.this, VisualitzarActivity.class);
            intentRespirar.putExtra("Primer","Primer");
            DialogInstruccionsRespirar();
        }
    }

    private void DialogInstruccionsRespirar(){
        final AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(RespirarActivity.this);
        DialegFormControl
                .setCancelable(false)
                .setTitle(getString(R.string.Attention))
                .setMessage(R.string.InstructonsBreathing)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        ReproduirMissatge(Reproduccio);
                    }
                })
                .show();
    }

    private void ReproduirMissatge(int Reproduccio){
        MediaPlayer mp = MediaPlayer.create(this,Reproduccio);
        try{
            mp.prepare();
        }catch (Exception e){
            Toast.makeText(RespirarActivity.this, e.toString(),
                    Toast.LENGTH_LONG).show();e.toString();
        }
        mp.start();
        while(mp.isPlaying()){
        }
        mp.stop();
        mp.release();
        startActivity(intentRespirar);
    }
}
