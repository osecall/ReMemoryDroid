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
    private int stopPosition, stopPositionAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualitzar_fragments);

        if(savedInstanceState != null){
            stopPosition = savedInstanceState.getInt("position");
            stopPositionAudio = savedInstanceState.getInt("positionAudio");
        }

        WriteStoragePermissos();
        ReadStoragePermissos();

        pacient= ObtenirPacient();
        episodi = ObtenirEpisodi();

        myRef = myRef.child(pacient.getID()).child(episodi).child("video").child("video.mp4");

        vv = (VideoView) findViewById(R.id.vvVisualitzar1);
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
            mp = MediaPlayer.create(this, R.raw.visualitzacioguiada1aturada);
            intent = new Intent(VisualitzarFragmentsActivity.this, PreguntesActivity.class);
            mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada1playing);
        }

        else if (getIntent().hasExtra("Tercer")) {
            mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada);
            intent = new Intent(VisualitzarFragmentsActivity.this, Preguntes2Activity.class);
            //Aquí el video no té audio i es reproduirà un audio mentres video reprodueix
            mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada2playing);
        }

        else if (getIntent().hasExtra("Quarta")) {
            mp = MediaPlayer.create(this, R.raw.visualitzacioguiada3aturada);
            intent = new Intent(VisualitzarFragmentsActivity.this, EvocarActivity.class);
            mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.visualitzacioguiada3playing);
            intent.putExtra("Quarta", "Quarta");
        }

        else if (getIntent().hasExtra("Curta")) {
            mp = MediaPlayer.create(this, R.raw.visualitzacioguiada2aturada);
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
                        mp.release();
                        mp = null;
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
                    mpBackVideo.start();
                }
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));
                vv.pause();
                vv.seekTo(0);
                mpBackVideo.seekTo(0);
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        mp = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        vv.pause();
        mpBackVideo.pause();
        ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv.pause();
        mpBackVideo.pause();
        ibPlay.setImageDrawable(getDrawable(R.drawable.playwhite));

    }

    @Override
    protected void onResume() {
        super.onResume();
        vv.seekTo(stopPosition);
        mpBackVideo.seekTo(stopPositionAudio);
        vv.start();
        mpBackVideo.start();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        vv.seekTo(stopPosition);
        mpBackVideo.seekTo(stopPositionAudio);
        vv.start();
        mpBackVideo.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        stopPosition = vv.getCurrentPosition();
        stopPositionAudio = mpBackVideo.getCurrentPosition();
        vv.pause();
        mpBackVideo.pause();
        outState.putInt("position", stopPosition);
        outState.putInt("positionAudio", stopPositionAudio);
    }
}
