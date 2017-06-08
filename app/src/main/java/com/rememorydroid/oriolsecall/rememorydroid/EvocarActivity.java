package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class EvocarActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton ibRecordEvocar, ibStopRecordEvocar, ibPlayEvocar, ibStopPlayEvocar;
    private MediaPlayer mp;
    private MediaRecorder mr;
    private Intent intent;
    private String outputFile;
    private String ID_usuari, NomFitxerCloud, episodi;
    private Button btNext;
    private TextView tvRecording;
    private Chronometer chronometer;
    private FirebaseStorage reference = FirebaseStorage.getInstance();
    private boolean curta;
    private ProgressBar pbEvocar, pb2Evocar;
    private PacientUsuari pacientusuari;


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i==R.id.ibRecordEvocar){
            grabar(outputFile);
        }
        if (i==R.id.ibStopRecordEvocar){
            pararGrabar();
        }
        if (i==R.id.ibPlayEvocar){
            reproduir();
        }
        if (i==R.id.ibStopPlayEvocar){
            pararReproduccio();
        }
        if (i==R.id.btNextWeather){
            LayoutInflater factory = LayoutInflater.from(EvocarActivity.this);
            View textEntryView = factory.inflate(R.layout.dialegs, null);
            TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
            tv.setText(getString(R.string.DoingGreat,pacientusuari.getName()));
            Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);
            AlertDialog.Builder alertab = new AlertDialog.Builder(EvocarActivity.this);
            alertab
                    .setView(textEntryView)
                    .setCancelable(false)
                    .setTitle(R.string.Congratulations);

            final AlertDialog alerta = alertab.create();

            alerta.show();

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                    alerta.dismiss();
                }
            });
        }
    }

    private void grabar(String outputFile){
        ibRecordEvocar.setEnabled(false);
        ibPlayEvocar.setVisibility(View.INVISIBLE);
        mr=new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        mr.setOutputFile(outputFile);
        try {
            mr.prepare();
            mr.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvRecording.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        pbEvocar.setVisibility(View.VISIBLE);

        showToast(getString(R.string.Recording),false);

        //Com a mínim que es gabrin 3 segons
        new Handler().postDelayed(new Runnable(){
            public void run(){
                ibStopRecordEvocar.setEnabled(true);
                ibStopRecordEvocar.setVisibility(View.VISIBLE);
            }
        }, 3000);


    }
    private void pararGrabar(){
        if(mr!=null){
            mr.stop();
            mr.release();
            mr=new MediaRecorder();
            chronometer.stop();
            chronometer.setVisibility(View.INVISIBLE);
        }
        pbEvocar.setVisibility(View.INVISIBLE);
        tvRecording.setVisibility(View.INVISIBLE);
        ibStopRecordEvocar.setVisibility(View.INVISIBLE);
        ibPlayEvocar.setVisibility(View.VISIBLE);

        ibPlayEvocar.setEnabled(true);
        ibRecordEvocar.setEnabled(true);

        showToast(getString(R.string.StopRecording), false);

        Uri file = Uri.fromFile(new File(outputFile));

        if(file.getPath().isEmpty()){
            showToast(getString(R.string.Record),true);
        }

        else {
            ibPlayEvocar.setVisibility(View.VISIBLE);
            ibPlayEvocar.setEnabled(true);
            //Enviar fitxer so a FireBase
            showProgressDialog();
            //Col·locar-ho en l'episodi corresponent
            StorageReference soRef = reference.getReferenceFromUrl("gs://rememorydroid.appspot.com").child(ID_usuari).child(episodi).child("sons").child(NomFitxerCloud);

            // Create file metadata including the content type (3GP)
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("audio/3gpp")
                    .build();

            soRef.putFile(file,metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideProgressDialog();
                    btNext.setVisibility(View.VISIBLE);
                    btNext.setEnabled(true);

                }

            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressDialog();
                            showToast(e.getMessage().toString(),false);
                        }
                    });
        }
        ibRecordEvocar.setEnabled(true);
    }
    private void reproduir(){

            ibPlayEvocar.setEnabled(false);
            ibRecordEvocar.setEnabled(false);
            ibRecordEvocar.setVisibility(View.INVISIBLE);
            mp = MediaPlayer.create(this,Uri.parse(outputFile));

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    pb2Evocar.setMax(mp.getDuration());
                    mp.start();
                    new Thread(new Runnable() {
                        public void run() {
                            while (mp.isPlaying()) {
                                pb2Evocar.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb2Evocar.setProgress(mp.getCurrentPosition());
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    ibRecordEvocar.setEnabled(true);
                    mp.stop();
                    mp.release();
                    mp=new MediaPlayer();
                    ibStopPlayEvocar.setEnabled(false);
                    ibStopPlayEvocar.setVisibility(View.INVISIBLE);
                    ibRecordEvocar.setVisibility(View.VISIBLE);
                    ibPlayEvocar.setEnabled(true);

                }
            });
            ibStopPlayEvocar.setEnabled(true);
            ibStopPlayEvocar.setVisibility(View.VISIBLE);


    }
    private void pararReproduccio(){
        if(mp.isPlaying()){
            mp.pause();
            mp.release();
            mp=new MediaPlayer();
            ibRecordEvocar.setVisibility(View.VISIBLE);
        }
        ibStopPlayEvocar.setEnabled(false);
        ibStopPlayEvocar.setVisibility(View.INVISIBLE);
        ibRecordEvocar.setEnabled(true);
        ibRecordEvocar.setImageResource(R.drawable.microphone);
        ibPlayEvocar.setEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evocar);

        AudioRecordPermissos();
        WriteStoragePermissos();
        ReadStoragePermissos();

        curta=false;
        outputFile = null;

        AudioRecordPermissos();
        WriteStoragePermissos();
        ReadStoragePermissos();
        InternetPermissos();

        //Extreure dades pacient
        pacientusuari = ObtenirPacient();
        ID_usuari = pacientusuari.getID();
        episodi = ObtenirEpisodi();
        //Comprobar si és versió curta
        if (ObtenirVersio().matches("Short")) curta = true;

        //Evocar C
        if(curta && getIntent().hasExtra("EvocarC")){
            DialegPrimerCurta();
            outputFile = new Environment().getExternalStorageDirectory().getAbsolutePath()+"/"+pacientusuari.getID()+pacientusuari.getName()+"_EvocarC.3gp";
            NomFitxerCloud = "_EvocarC.3gp";
            intent = new Intent (EvocarActivity.this, EscenaCurtaActivity.class);
        }

        if(curta && getIntent().hasExtra("EvocarD")){
            DialegPrimerCurta();
            outputFile = new Environment().getExternalStorageDirectory().getAbsolutePath()+"/"+pacientusuari.getID()+pacientusuari.getName()+"_EvocarD.3gp";
            NomFitxerCloud = "_EvocarD.3gp";
            intent = new Intent (EvocarActivity.this, RespirarActivity.class);
            intent.putExtra("Curta2","Curta2");
        }


        if(getIntent().hasExtra("Quarta")){
            DialegCongrats(); //Felicitem a l'usuari
            DialegSegon(); //Instruccions diferents a si es la primera vegada
            outputFile = new Environment().getExternalStorageDirectory().getAbsolutePath()+"/"+pacientusuari.getID()+pacientusuari.getName()+"_EvocarB.3gp";
            NomFitxerCloud = "_EvocarB.3gp";
            intent = new Intent (EvocarActivity.this, AlbumActivity.class);
        }
        if(getIntent().hasExtra("Primer")){
            DialegPrimer();
            outputFile = new Environment().getExternalStorageDirectory().getAbsolutePath()+"/"+pacientusuari.getID()+pacientusuari.getName()+"_EvocarA.3gp";
            intent = new Intent(EvocarActivity.this,RespirarActivity.class);
            NomFitxerCloud = "_EvocarA.3gp";
            intent.putExtra("Segon","Segon");
        }

        ibRecordEvocar = (ImageButton) findViewById(R.id.ibRecordEvocar);
        ibStopPlayEvocar = (ImageButton) findViewById(R.id.ibStopPlayEvocar);
        ibPlayEvocar = (ImageButton) findViewById(R.id.ibPlayEvocar);
        ibStopRecordEvocar = (ImageButton) findViewById(R.id.ibStopRecordEvocar);
        pbEvocar = (ProgressBar) findViewById(R.id.pbEvocar);
        pb2Evocar = (ProgressBar) findViewById(R.id.pb2Evocar);

        ibStopRecordEvocar.setVisibility(View.INVISIBLE);
        ibStopPlayEvocar.setVisibility(View.INVISIBLE);
        ibStopRecordEvocar.setEnabled(false);
        ibStopPlayEvocar.setEnabled(false);
        ibPlayEvocar.setVisibility(View.INVISIBLE);
        ibPlayEvocar.setEnabled(false);
        ibRecordEvocar.setEnabled(false);


        tvRecording = (TextView) findViewById(R.id.tvRecordingEvocar);
        tvRecording.setVisibility(View.INVISIBLE);

        btNext = (Button) findViewById(R.id.btNextWeather);

        chronometer = (Chronometer) findViewById(R.id.chronometerEvocar);
        chronometer.setBase(0);
        chronometer.setVisibility(View.INVISIBLE);

        ibRecordEvocar.setOnClickListener(this);
        ibStopPlayEvocar.setOnClickListener(this);
        ibPlayEvocar.setOnClickListener(this);
        ibStopRecordEvocar.setOnClickListener(this);
        btNext.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mr.release();
        mp.release();
        mr=null;
        mp=null;
    }

        //Part del menú 'action bar'

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu, menu);
            menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
            menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient) + " (" + pacientusuari.getID() + ")");
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.btSignOutMenu) {
                //Confirmar eliminació per contrasenya
                //-------------------------------------------------------------------
                AlertDialog.Builder dialegPassword = new AlertDialog.Builder(EvocarActivity.this);
                LayoutInflater factory = LayoutInflater.from(EvocarActivity.this);
                View textEntryView = factory.inflate(R.layout.dialeg_delete_user, null);
                //Instanciem els elements del diàleg per poder obtenir el que ha escrit l'usuari
                final EditText input = (EditText) textEntryView.findViewById(R.id.etPasswordDelete);
                dialegPassword
                        .setView(textEntryView)
                        .setIcon(R.drawable.passwordicon)
                        .setTitle(R.string.PasswordDialog)
                        .setMessage(R.string.IntroducePassword)
                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Recuperem el email del avaluador i el reautentiquem
                                String email_user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                                String pass_user = input.getText().toString();
                                if (!pass_user.isEmpty()) {
                                    //Reautentiquem al avaluador per seguretat
                                    AuthCredential credential = EmailAuthProvider.getCredential(email_user, pass_user);
                                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Retorna a la pantalla inicial
                                            FirebaseAuth.getInstance().signOut();
                                            showToast(getString(R.string.signed_out), true);
                                            Intent areaAvaluador = new Intent(EvocarActivity.this, SignInActivity.class);
                                            startActivity(areaAvaluador);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast(getString(R.string.IncorrecPassword), false);

                                        }
                                    });
                                }
                            }
                        }).show();


            }

            if (id == R.id.btSignOutPacient) {

                //Confirmar eliminació per contrasenya
                //-------------------------------------------------------------------
                AlertDialog.Builder dialegPassword = new AlertDialog.Builder(EvocarActivity.this);
                LayoutInflater factory = LayoutInflater.from(EvocarActivity.this);
                View textEntryView = factory.inflate(R.layout.dialeg_delete_user, null);
                //Instanciem els elements del diàleg per poder obtenir el que ha escrit l'usuari
                final EditText input = (EditText) textEntryView.findViewById(R.id.etPasswordDelete);
                dialegPassword
                        .setView(textEntryView)
                        .setIcon(R.drawable.passwordicon)
                        .setTitle(R.string.PasswordDialog)
                        .setMessage(R.string.IntroducePassword)
                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Recuperem el email del avaluador i el reautentiquem
                                String email_user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                                String pass_user = input.getText().toString();
                                if (!pass_user.isEmpty()) {
                                    //Reautentiquem al avaluador per seguretat
                                    AuthCredential credential = EmailAuthProvider.getCredential(email_user, pass_user);
                                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Retorna a la pantalla 'Area Avaluador'
                                            BorrarPacient();
                                            showToast(getString(R.string.MenuChangePacient), true);
                                            Intent areaAvaluador = new Intent(EvocarActivity.this, AreaAvaluadorActivity.class);
                                            startActivity(areaAvaluador);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast(getString(R.string.IncorrecPassword), false);

                                        }
                                    });
                                }
                            }
                        }).show();

            }

            return super.onOptionsItemSelected(item);
        }


    private void DialegPrimer(){
        LayoutInflater factory = LayoutInflater.from(EvocarActivity.this);
        View textEntryView = factory.inflate(R.layout.dialegslisten, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        Button btListen = (Button) textEntryView.findViewById(R.id.btListen);
        Button btNoListen = (Button) textEntryView.findViewById(R.id.btNoListen);
        tv.setText(R.string.EvocarAdiaelg);

        AlertDialog.Builder dialeg =new AlertDialog.Builder(EvocarActivity.this);
        dialeg
                .setTitle(getString(R.string.Attention))
                .setCancelable(false)
                .setView(textEntryView);

        final AlertDialog alerta = dialeg.create();

        alerta.show();

        btListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reproduirMissatgeDialeg();
                alerta.dismiss();
            }
        });

        btNoListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibRecordEvocar.setEnabled(true);
                alerta.dismiss();
            }
        });
    }

    private void DialegPrimerCurta(){
        AlertDialog.Builder dialeg =new AlertDialog.Builder(EvocarActivity.this);
        LayoutInflater factory = LayoutInflater.from(EvocarActivity.this);
        View textEntryView = factory.inflate(R.layout.dialegslisten, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        Button btListen = (Button) textEntryView.findViewById(R.id.btListen);
        Button btNoListen = (Button) textEntryView.findViewById(R.id.btNoListen);
        tv.setText(R.string.Evocarccurta);

        dialeg
                .setTitle(getString(R.string.Attention))
                .setCancelable(false)
                .setView(textEntryView);

        final AlertDialog alerta = dialeg.create();

        alerta.show();

        btListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reproduirMissatgeDialeg();
                alerta.dismiss();
            }
        });

        btNoListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibRecordEvocar.setEnabled(true);
                alerta.dismiss();
            }
        });
    }

    private void DialegSegon(){
        LayoutInflater factory = LayoutInflater.from(EvocarActivity.this);
        View textEntryView = factory.inflate(R.layout.dialegslisten, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        Button btListen = (Button) textEntryView.findViewById(R.id.btListen);
        Button btNoListen = (Button) textEntryView.findViewById(R.id.btNoListen);
        tv.setText(R.string.EvocarB);

        AlertDialog.Builder dialeg =new AlertDialog.Builder(EvocarActivity.this);
        dialeg
                .setTitle(getString(R.string.Attention))
                .setCancelable(false)
                .setView(textEntryView);
        final AlertDialog alerta = dialeg.create();

        alerta.show();

        btListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reproduirMissatgeDialeg();
                alerta.dismiss();
            }
        });

        btNoListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibRecordEvocar.setEnabled(true);
                alerta.dismiss();
            }
        });
    }

    private void DialegCongrats(){
        LayoutInflater factory = LayoutInflater.from(EvocarActivity.this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);
        tv.setText(getString(R.string.DoingGreat,pacientusuari.getName()));

        AlertDialog.Builder dialeg =new AlertDialog.Builder(EvocarActivity.this);
        dialeg
                .setTitle(getString(R.string.Congratulations))
                .setCancelable(false)
                .setView(textEntryView);

        final AlertDialog alerta = dialeg.create();

        alerta.show();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });
    }


    private void reproduirMissatgeDialeg(){

        MediaPlayer mp = new MediaPlayer();

        if (getResources().getConfiguration().locale.getLanguage().matches("ca")) {
            mp = MediaPlayer.create(this, R.raw.evocar1);
        } else if (getResources().getConfiguration().locale.getLanguage().matches("es")) {
            mp = MediaPlayer.create(this, R.raw.evocar1_es);

        } else if (getResources().getConfiguration().locale.getLanguage().matches("en")) {
            mp = MediaPlayer.create(this, R.raw.evocar1_en);
        } else {
            mp = MediaPlayer.create(this, R.raw.evocar1);
        }

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.release();
                ibRecordEvocar.setEnabled(true);
            }
        });
        mp.start();
    }
}

