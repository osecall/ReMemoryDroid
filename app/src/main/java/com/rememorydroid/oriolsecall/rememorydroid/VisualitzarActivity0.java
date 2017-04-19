package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VisualitzarActivity0 extends AppCompatActivity {

    private MediaPlayer mp;
    private VideoView vv;
    private ImageView ibPlay, ibStop;
    private Button btBack, btNext;
    private Intent intent;
    private int duration;
    private ProgressBar ProgressBarVideo;
    private boolean noAudio=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualitzar0);

        vv = (VideoView) findViewById(R.id.vvVisualitzar1);
        ProgressBarVideo = (ProgressBar) findViewById(R.id.progressBarVideo);
        ibPlay = (ImageView) findViewById(R.id.ibPlay);
        ibStop = (ImageView) findViewById(R.id.ibStop);
        btBack = (Button) findViewById(R.id.btBackWeather);
        btNext = (Button) findViewById(R.id.btNextWeather);

        //Quan acabi les instruccions per veu s'habilitaran els botons de reproducció
        btNext.setVisibility(View.INVISIBLE);
        ibPlay.setEnabled(false);
        ibStop.setEnabled(false);
        ibPlay.setVisibility(View.INVISIBLE);
        ibStop.setVisibility(View.INVISIBLE);

        //Per les instruccions
        mp = MediaPlayer.create(this, R.raw.visualitzar1);
        DialogInstruccionsVisualitzar(mp);
        //Vídeo
        vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
        intent=new Intent(VisualitzarActivity0.this,EvocarActivity.class);

        if(getIntent().hasExtra("Primer")){
            //Versió llarga primera visualització...
        }

        if(getIntent().hasExtra("Curta")){
            noAudio=true;
        }
        else{
            intent.putExtra("Primer","Primer");
        }

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                duration = vv.getDuration();
                vv.setMediaController(new MediaController(VisualitzarActivity0.this));
                if(noAudio){
                    mediaPlayer.setVolume(0,0);
                }
                vv.start();
                vv.pause();
                ProgressBarVideo.setMax(duration);
                ProgressBarVideo.setProgress(0);

                ibPlay.setEnabled(true);
                ibStop.setEnabled(true);
            }
        });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
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


        //Aquí el video no es fragmenta en 3 parts
        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vv.isPlaying()){
                    vv.pause();
                    ibPlay.setImageDrawable(getDrawable(R.drawable.play));
                }
                else{
                    vv.start();
                    ibPlay.setImageDrawable(getDrawable(R.drawable.pause));

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
                ibPlay.setImageDrawable(getDrawable(R.drawable.play));
            }
        });
    }

    private void DialogInstruccionsVisualitzar(final MediaPlayer mp) {
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(VisualitzarActivity0.this);
        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setCancelable(false)
                .setMessage(R.string.DialogVideo1)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            mp.prepare();
                        } catch (Exception e) {

                        }
                        mp.start();
                        while (mp.isPlaying()) {
                        }
                        mp.stop();
                        mp.release();
                        ibPlay.setVisibility(View.VISIBLE);
                        ibStop.setVisibility(View.VISIBLE);
                        ibPlay.setEnabled(true);
                        ibStop.setEnabled(true);
                        arg0.cancel();
                        arg0.dismiss();

                    }
                })
                .show();
    }
}
