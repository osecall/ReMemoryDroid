package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class VisualitzarActivity extends BaseActivity {

    private MediaPlayer mp;
    private VideoView vv;
    private ImageView ibPlay, ibStop;
    private Button btNext;
    private Intent intent;
    private ProgressBar ProgressBarVideo;
    private boolean noAudio=false;
    private PacientUsuari pacient;
    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private String episodi;
    private File video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualitzar);

        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        String pacient_json = prefs.getString("pacient",null);
        Gson temp = new Gson();
        pacient = temp.fromJson(pacient_json, PacientUsuari.class);
        episodi = prefs.getString("episodi",null);

        myRef = myRef.child(pacient.getID()).child(episodi).child("video").child("video.mp4");

        vv = (VideoView) findViewById(R.id.vvVisualitzar1);
        ProgressBarVideo = (ProgressBar) findViewById(R.id.progressBarVideo);
        ibPlay = (ImageView) findViewById(R.id.ibPlay);
        ibStop = (ImageView) findViewById(R.id.ibStop);
        btNext = (Button) findViewById(R.id.btNextWeather);

        try {
            video = File.createTempFile("video", "mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        showProgressDialog();
        myRef.getFile(video).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                vv.setVideoURI(Uri.parse(video.getAbsolutePath().toString()));
                hideProgressDialog();
            }
        });

        //Quan acabi les instruccions per veu s'habilitaran els botons de reproducció
        btNext.setVisibility(View.INVISIBLE);
        ibPlay.setEnabled(false);
        ibStop.setEnabled(false);
        ibPlay.setVisibility(View.INVISIBLE);
        ibStop.setVisibility(View.INVISIBLE);
        vv.setEnabled(false);

        //Per les instruccions
        mp = MediaPlayer.create(this, R.raw.visualitzacio0);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.stop();
                mp.release();
                ibPlay.setVisibility(View.VISIBLE);
                ibStop.setVisibility(View.VISIBLE);
                ibPlay.setEnabled(true);
                ibStop.setEnabled(true);
            }
        });
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                DialogInstruccionsVisualitzar();
                mp.start();
            }
        });

        //Vídeo
        intent=new Intent(VisualitzarActivity.this,EvocarActivity.class);

        if(getIntent().hasExtra("Curta1")){
            noAudio=true;
            intent.putExtra("EvocarD","EvocarD");

        }
        else{
            intent.putExtra("Primer","Primer");
        }

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                vv.setMediaController(new MediaController(VisualitzarActivity.this));
                vv.start();
                vv.pause();
                vv.seekTo(0);
                ProgressBarVideo.setMax(vv.getDuration());
                ProgressBarVideo.setProgress(0);

                ibPlay.setEnabled(true);
                ibStop.setEnabled(true);
            }
        });



        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));
                btNext.setVisibility(View.VISIBLE);
                btNext.setEnabled(true);
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vv.isPlaying()){
                    vv.pause();
                    ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));
                }
                else{
                    vv.start();
                    ibPlay.setImageDrawable(getDrawable(R.drawable.pausewhite));

                    //Actualitzem la barra de progrés
                    new Thread(new Runnable() {
                        public void run() {
                            while (vv.isPlaying()) {
                                ProgressBarVideo.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressBarVideo.setProgress(vv.getCurrentPosition());
                                    }
                                });
                            }}}).start();
                }
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vv.pause();
                vv.seekTo(0);
                ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));
            }
        });
    }

    private void DialogInstruccionsVisualitzar() {
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(VisualitzarActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        tv.setText(R.string.DialogVideo1);
        Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);

        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setView(textEntryView)
                .setCancelable(false);

        final AlertDialog alerta = DialegFormControl.create();

        alerta.show();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });
    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient)+" ("+pacient.getID()+")");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.btSignOutMenu) {

            //Retorna a la pantalla inicial
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(VisualitzarActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(VisualitzarActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(VisualitzarActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(VisualitzarActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv.pause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        vv.start();
    }
}
