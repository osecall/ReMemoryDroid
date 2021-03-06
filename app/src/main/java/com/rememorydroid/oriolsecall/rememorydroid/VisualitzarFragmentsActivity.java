package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class VisualitzarFragmentsActivity extends BaseActivity {

    private MediaPlayer mp;
    private VideoView vv;
    private ImageView ibPlay, ibStop;
    private Button btNext;
    private Intent intent;
    private PacientUsuari pacient;
    private File video;
    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private String episodi;
    private MediaPlayer mpBackVideo;
    private int audioBack;
    private boolean audioFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualitzar_fragments);

        mp = null;
        mpBackVideo = null;
        audioFinish = false;

        WriteStoragePermissos();
        ReadStoragePermissos();

        pacient= ObtenirPacient();
        episodi = ObtenirEpisodi();

        myRef = myRef.child(pacient.getID()).child(episodi).child("video").child("video.mp4");

        vv = (VideoView) findViewById(R.id.vvVisualitzar2);
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

        if (getIntent().hasExtra("Segon")) {

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada1aturada_ca);
                audioBack = R.raw.visualitzacioguiada1playing_ca;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada1playing_ca);

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada1aturada_es);
                audioBack = R.raw.visualitzacioguiada1playing_es;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada1playing_es);


            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada1aturada_en);
                audioBack = R.raw.visualitzacioguiada1playing_en;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada1playing_en);

            } else {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada1aturada_es);
                audioBack = R.raw.visualitzacioguiada1playing_es;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada1playing_es);
            }

            intent = new Intent(VisualitzarFragmentsActivity.this, PreguntesActivity.class);
        }

        else if (getIntent().hasExtra("Tercer")) {

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada_ca);
                audioBack = R.raw.visualitzacioguiada2playing_ca;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing_ca);

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada_es);
                audioBack = R.raw.visualitzacioguiada2playing_es;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing_es);


            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada_en);
                audioBack = R.raw.visualitzacioguiada2playing_en;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing_en);

            } else {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada_es);
                audioBack = R.raw.visualitzacioguiada2playing_es;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing_es);
            }

            intent = new Intent(VisualitzarFragmentsActivity.this, Preguntes2Activity.class);
            //Aquí el video no té audio i es reproduirà un audio mentres video reprodueix
        }

        else if (getIntent().hasExtra("Quarta")) {

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada3aturada_ca);
                audioBack = R.raw.visualitzacioguiada3playing_ca;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada3playing_ca);

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada3aturada_es);
                audioBack = R.raw.visualitzacioguiada3playing_es;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada3playing_es);

            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada3aturada_en);
                audioBack = R.raw.visualitzacioguiada3playing_en;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada3playing_en);

            } else {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada3aturada_es);
                audioBack = R.raw.visualitzacioguiada3playing_es;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada3playing_es);
            }

            intent = new Intent(VisualitzarFragmentsActivity.this, EvocarActivity.class);
            intent.putExtra("Quarta", "Quarta");
        }

        else if (getIntent().hasExtra("Curta")) {

            if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada_ca);
                audioBack = R.raw.visualitzacioguiada2playing_ca;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing_ca);

            } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada_es);
                audioBack = R.raw.visualitzacioguiada2playing_es;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing_es);

            } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada_en);
                audioBack = R.raw.visualitzacioguiada2playing_en;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing_en);

            } else {
                mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada_es);
                audioBack = R.raw.visualitzacioguiada2playing_es;
                mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing_es);
            }

            intent = new Intent(VisualitzarFragmentsActivity.this, EvocarActivity.class);
            intent.putExtra("EvocarD", "EvocarD");
        }

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.setVolume(0, 0);
                vv.start();vv.pause();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ibPlay.setVisibility(View.VISIBLE);
                        ibStop.setVisibility(View.VISIBLE);
                        ibPlay.setEnabled(true);
                        ibStop.setEnabled(true);
                    }
                });
                mp.start();
            }
        });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btNext.setVisibility(View.VISIBLE);
                btNext.setEnabled(true);
                ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));
            }
        });

        // New
        mpBackVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                audioFinish = true;
            }
        });
        //

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vv.isPlaying()) {
                    vv.pause();
                    mpBackVideo.pause();
                    ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));
                } else {
                    ibPlay.setImageDrawable(getDrawable(R.drawable.pausewhite));
                    vv.start();
                    if (!audioFinish) mpBackVideo.start();
                }
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));
                vv.pause();
                mpBackVideo.pause();
                vv.seekTo(0);
                mpBackVideo.seekTo(0);
                audioFinish = false;
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                vv.stopPlayback();
                mp.release();
                mpBackVideo.release();
            }
        });

    }
}