package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class RespirarActivity1 extends AppCompatActivity {

    private Intent intentRespirar;
    private ImageView ivRespirar;
    private int Reproduccio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respirar1);

        ImageView ivRespirar = (ImageView) findViewById(R.id.ivRespirar);
        intentRespirar = new Intent(RespirarActivity1.this, VisualitzarActivity1.class);

        //Configurem els elements que apareixeran i es reproduiran en funció si és la segona vegada que es crida la activity
        //d'aquesta forma ens evitem duplicar classes (activities)
        if(getIntent().hasExtra("Segon")){
            ivRespirar.setImageResource(R.drawable.abstracte2);
            Reproduccio = R.raw.test2;
            intentRespirar.putExtra("Segon","Segon");
            DialogInstruccionsRespirar();

        }
        else if(getIntent().hasExtra("Tercer")){
            ivRespirar.setImageResource(R.drawable.abstracte2);
            Reproduccio = R.raw.test2;
            intentRespirar.putExtra("Tercer","Tercer");
            DialogInstruccionsRespirar();

        }
        else{
            Reproduccio = R.raw.test1;
            DialogInstruccionsRespirar();
        }


    }


    private void DialogInstruccionsRespirar(){
        final AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(RespirarActivity1.this);
        DialegFormControl
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
            Toast.makeText(RespirarActivity1.this, e.toString(),
                    Toast.LENGTH_LONG).show();e.toString();
        }
        mp.start();
        while(mp.isPlaying()){
        }
        mp.stop();
        mp.release();
        startActivity(intentRespirar);
    };
}
