package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

public class VisualitzarFragmentsActivity extends BaseActivity {

    private MediaPlayer mp;
    private VideoView vv;
    private ImageView ibPlay, ibStop;
    private Button btNext;
    private Intent intent;
    private int duration, PrimeraFraccio,SegonaFraccio;
    private PacientUsuari pacient;
    private File video;
    private boolean noAudio=false;
    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private String episodi;
    private MediaPlayer mpBackVideo;
    private Thread myThread, myThread2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualitzar_fragments);

        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        String pacient_json = prefs.getString("pacient",null);
        Gson temp = new Gson();
        pacient = temp.fromJson(pacient_json, PacientUsuari.class);
        episodi = prefs.getString("episodi",null);
        myRef = myRef.child(pacient.getID()).child(episodi).child("video.mp4");

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

        if(getIntent().hasExtra("Segon")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2_1);
            //vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarFragmentsActivity.this,PreguntesActivity.class);
        }
        else if(getIntent().hasExtra("Tercer")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2);
            //vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarFragmentsActivity.this,Preguntes2Activity.class);
            //Aquí el video no té audio i es reproduirà un audio mentres video reprodueix
            noAudio=true;
            mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this,R.raw.vozoffnoaudiovideo);
        }
        else if(getIntent().hasExtra("Quarta")){
            mp = MediaPlayer.create(this, R.raw.visualitzar3);
            //vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarFragmentsActivity.this,EvocarActivity.class);
            noAudio=true;
            mpBackVideo = MediaPlayer.create(VisualitzarFragmentsActivity.this,R.raw.vozoffnoaudiovideo);
            intent.putExtra("Quarta","Quarta");
        }
        else if(getIntent().hasExtra("Curta")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2);
            //vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarFragmentsActivity.this,EvocarActivity.class);
            intent.putExtra("EvocarD","EvocarD");
        }

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                ibPlay.setVisibility(View.INVISIBLE);
                ibStop.setVisibility(View.INVISIBLE);
                mp=MediaPlayer.create(VisualitzarFragmentsActivity.this,R.raw.tercerfragment);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mp.stop();
                        mp.release();
                        ibPlay.setVisibility(View.VISIBLE);
                        ibStop.setVisibility(View.VISIBLE);
                        btNext.setVisibility(View.VISIBLE);
                        btNext.setEnabled(true);
                        ibPlay.setImageDrawable(getDrawable(R.drawable.play));
                    }
                });


            }
        });

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                if(noAudio){
                    mediaPlayer.setVolume(0,0);
                }
                vv.start();
                vv.pause();
                vv.setMediaController(new MediaController(VisualitzarFragmentsActivity.this));
                duration = vv.getDuration();
                PrimeraFraccio = duration /3;
                SegonaFraccio = (duration*2)/3;


                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mp.release();
                        mp=null;
                        ibPlay.setVisibility(View.VISIBLE);
                        ibStop.setVisibility(View.VISIBLE);
                        ibPlay.setEnabled(true);
                        ibStop.setEnabled(true);
                    }
                });
                mp.start();

            }
        });


        Runnable Fragments2 = new Runnable() {
            @Override
            public void run() {
                while(vv.isPlaying()){
                    if (vv.getCurrentPosition() == SegonaFraccio) {

                        vv.post(new Runnable() {
                            @Override
                            public void run() {
                                vv.pause();
                            }
                        });

                        final MediaPlayer mp = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.segonfragment);

                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                            }
                        });

                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mp.stop();
                                mp.release();
                                vv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        vv.start();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        };


        Runnable Fragments = new Runnable() {
            @Override
            public void run() {
                while (vv.isPlaying()) {

                    if (vv.getCurrentPosition() == PrimeraFraccio) {

                        vv.post(new Runnable() {
                            @Override
                            public void run() {
                                vv.pause();
                            }
                        });

                        final MediaPlayer mp = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.firstfragment);

                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                            }
                        });

                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mp.stop();
                                mp.release();
                                vv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        vv.start();
                                        myThread2.start();
                                    }
                                });
                            }
                        });
                    }

                }
            }
        };

        myThread = new Thread(Fragments);
        myThread.setPriority(Thread.MAX_PRIORITY);
        myThread2 = new Thread(Fragments2);
        myThread2.setPriority(Thread.MAX_PRIORITY);

        ibPlay.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (vv.isPlaying()) {
                                              vv.pause();
                                              ibPlay.setImageDrawable(getDrawable(R.drawable.play));
                                          } else {
                                              ibPlay.setImageDrawable(getDrawable(R.drawable.pause));
                                              if(noAudio) mpBackVideo.start();
                                              vv.start();
                                              myThread.start();
                                          }
                                      }
                                  });


        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibPlay.setImageDrawable(getDrawable(R.drawable.play));
                vv.pause();
                vv.seekTo(0);

            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        mp=null;
    }

}
