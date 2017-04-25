package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    private Button btBack, btNext;
    private Intent intent;
    private int duration;
    private ProgressBar ProgressBarVideo;
    private boolean noAudio=false;
    private PacientUsuari pacient;
    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private SharedPreferences prefs;
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

        myRef = myRef.child(pacient.getID()).child(episodi).child("video.mp4");

        vv = (VideoView) findViewById(R.id.vvVisualitzar1);
        ProgressBarVideo = (ProgressBar) findViewById(R.id.progressBarVideo);
        ibPlay = (ImageView) findViewById(R.id.ibPlay);
        ibStop = (ImageView) findViewById(R.id.ibStop);
        btBack = (Button) findViewById(R.id.btBackWeather);
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
        mp = MediaPlayer.create(this, R.raw.visualitzar1);
        DialogInstruccionsVisualitzar(mp);
        //Vídeo
        //vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
        intent=new Intent(VisualitzarActivity.this,EvocarActivity.class);

        if(getIntent().hasExtra("Primer")){
            //Versió llarga primera visualització...
        }

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
                duration = vv.getDuration();

                //vv.setMediaController(new MediaController(VisualitzarActivity.this));
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
                ibPlay.setImageDrawable(getDrawable(R.drawable.play));
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
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(VisualitzarActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        tv.setText(R.string.DialogVideo1);
        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setView(textEntryView)
                .setCancelable(false)
                //.setMessage(R.string.DialogVideo1)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        mp.start();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mp.stop();
                                mp.release();
                                ibPlay.setVisibility(View.VISIBLE);
                                ibStop.setVisibility(View.VISIBLE);
                                ibPlay.setEnabled(true);
                                ibStop.setEnabled(true);
                                vv.setEnabled(true);

                            }
                        });
                        arg0.cancel();
                        arg0.dismiss();
                    }
                })
                .show();
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
}
