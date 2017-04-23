package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class VisualitzarFragmentsActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private VideoView vv;
    private ImageView ibPlay, ibStop;
    private Button btBack, btNext;
    private Intent intent;
    private int duration, PrimeraFraccio,SegonaFraccio;
    private ProgressBar ProgressBarVideo;
    private PacientUsuari pacient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualitzar_fragments);

        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        String pacient_json = prefs.getString("pacient",null);
        Gson temp = new Gson();
        pacient = temp.fromJson(pacient_json, PacientUsuari.class);


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

        if(getIntent().hasExtra("Segon")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2);
            vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarFragmentsActivity.this,PreguntesActivity.class);
        }
        else if(getIntent().hasExtra("Tercer")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2);
            vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarFragmentsActivity.this,Preguntes2Activity.class);
            //Aquí el video no té audio
        }
        else if(getIntent().hasExtra("Quarta")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2);
            vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarFragmentsActivity.this,EvocarActivity.class);
            intent.putExtra("Quarta","Quarta");
        }
        else if(getIntent().hasExtra("Curta")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2);
            vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarFragmentsActivity.this,EvocarActivity.class);
            intent.putExtra("EvocarD","EvocarD");
        }

        DialogInstruccionsVisualitzar(mp);

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp=MediaPlayer.create(VisualitzarFragmentsActivity.this,R.raw.evocara);

                try{
                    mp.prepare();
                }catch (Exception e){

                }
                mp.start();
                while(mp.isPlaying()){
                }
                mp.stop();
                mp.release();

                //DialegFraccions(MediaPlayer.create(VisualitzarFragmentsActivity.this,R.raw.respirar1),true);
                btNext.setVisibility(View.VISIBLE);
                ibPlay.setImageDrawable(getDrawable(R.drawable.play));

            }
        });

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                vv.start();
                vv.pause();
                duration = vv.getDuration();
                vv.setMediaController(new MediaController(VisualitzarFragmentsActivity.this));
                PrimeraFraccio = duration /3;
                SegonaFraccio = (duration*2)/3;

                ProgressBarVideo.setMax(duration);
                ProgressBarVideo.setProgress(0);

                ibPlay.setEnabled(true);
                ibStop.setEnabled(true);
            }
        });


        ibPlay.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (vv.isPlaying()) {
                                              vv.pause();
                                              ibPlay.setImageDrawable(getDrawable(R.drawable.play));
                                          } else {
                                              ibPlay.setImageDrawable(getDrawable(R.drawable.pause));
                                              vv.start();

                                              new Thread(new Runnable() {
                                                  public void run() {
                                                      while (vv.isPlaying()) {
                                                          ProgressBarVideo.post(new Runnable() {
                                                              @Override
                                                              public void run() {
                                                                  ProgressBarVideo.setProgress(vv.getCurrentPosition());
                                                              }
                                                          });
                                                          if (vv.getCurrentPosition() == PrimeraFraccio) {

                                                              vv.post(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      vv.pause();
                                                                  }
                                                              });

                                                              ibPlay.post(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      ibPlay.setImageDrawable(getDrawable(R.drawable.play));
                                                                      ibPlay.setVisibility(View.INVISIBLE);
                                                                      ibStop.setVisibility(View.INVISIBLE);
                                                                  }
                                                              });
                                                              runOnUiThread(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      mp.release();
                                                                      mp = null;
                                                                      mp = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.evocara);
                                                                      mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                                          @Override
                                                                          public void onCompletion(MediaPlayer mediaPlayer) {
                                                                              ibPlay.setVisibility(View.VISIBLE);
                                                                              ibStop.setVisibility(View.VISIBLE);
                                                                              //vv.start();
                                                                          }
                                                                      });
                                                                      mp.start();
                                                                  }
                                                              });
                                                          }if (vv.getCurrentPosition() == SegonaFraccio) {
                                                                      vv.post(new Runnable() {
                                                                          @Override
                                                                          public void run() {
                                                                              vv.pause();
                                                                              ibPlay.post(new Runnable() {
                                                                                  @Override
                                                                                  public void run() {
                                                                                      ibPlay.setImageDrawable(getDrawable(R.drawable.play));
                                                                                      ibPlay.setVisibility(View.INVISIBLE);
                                                                                      ibStop.setVisibility(View.INVISIBLE);
                                                                                  }
                                                                              });
                                                                              mp.release();
                                                                              mp = null;
                                                                              mp = MediaPlayer.create(VisualitzarFragmentsActivity.this, R.raw.evocara);
                                                                              mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                                                  @Override
                                                                                  public void onCompletion(MediaPlayer mediaPlayer) {
                                                                                      ibPlay.setVisibility(View.VISIBLE);
                                                                                      ibStop.setVisibility(View.VISIBLE);
                                                                                      //vv.start();
                                                                                  }
                                                                              });
                                                                              mp.start();
                                                                          }
                                                                      });
                                                          }
                                                      }
                                                  }
                                              }).start();
                                          }
                                      }
                                  });


        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibPlay.setImageDrawable(getDrawable(R.drawable.play));
                ProgressBarVideo.setProgress(0);
                vv.pause();
                vv.seekTo(1);

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (VisualitzarFragmentsActivity.this, RespirarActivity.class));
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


    private void DialogInstruccionsVisualitzar(final MediaPlayer mp){
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(VisualitzarFragmentsActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        tv.setText(R.string.DialogVideo1);
        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setView(textEntryView)
                .setCancelable(false)
                .setMessage(R.string.DialogVideo1)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

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
                        mp.start();
                        arg0.cancel();
                        arg0.dismiss();

                    }
                })
                .show();
    }
    private void DialegFraccions(final MediaPlayer mp,final boolean ultim){
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(VisualitzarFragmentsActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        tv.setText(R.string.DialogVideo1);
        DialegFormControl
                .setTitle(getString(R.string.Listen))
                .setView(textEntryView)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mp.stop();
                                mp.release();
                                ibPlay.setVisibility(View.VISIBLE);
                                ibStop.setVisibility(View.VISIBLE);
                                ibPlay.setEnabled(true);
                                ibStop.setEnabled(true);
                                if(!ultim){
                                    vv.start();
                                }
                            }
                        });
                        mp.start();
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
            Toast.makeText(VisualitzarFragmentsActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(VisualitzarFragmentsActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(VisualitzarFragmentsActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(VisualitzarFragmentsActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }

}
