package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class EvocarActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton ibRecordEvocar, ibStopRecordEvocar, ibPlayEvocar, ibStopPlayEvocar;
    private MediaPlayer mp;
    private MediaRecorder mr;
    private Intent intent;
    private String outputFile = null;
    private String ID_usuari, NomFitxerCloud, episodi;
    private Button btBack, btNext;
    private TextView tvRecording;
    private Chronometer chronometer;
    private FirebaseStorage reference = FirebaseStorage.getInstance();
    private boolean curta=false;

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
        if (i==R.id.btBackWeather){
            startActivity(new Intent(EvocarActivity.this,VisualitzarActivity1.class));
        }
        if (i==R.id.btNextWeather){
            //Enviar fitxer so a FireBase
            showProgressDialog();

            Uri file = Uri.fromFile(new File(outputFile));
            //Col·locar-ho en l'episodi corresponent
            StorageReference soRef = reference.getReferenceFromUrl("gs://rememorydroid.appspot.com").child(ID_usuari).child(episodi).child(NomFitxerCloud);
            soRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideProgressDialog();
                    new AlertDialog.Builder(EvocarActivity.this)
                            .setMessage(R.string.DoingGreat)
                            .setTitle(R.string.Congratulations)
                            .setNeutralButton(R.string.ThankYou, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EvocarActivity.this,"Error!",
                            Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void grabar(String outputFile){
        mr=new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        mr.setOutputFile(outputFile);

        try {
            mr.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mr.start();
        ibRecordEvocar.setEnabled(false);
        ibPlayEvocar.setEnabled(false);
        ibPlayEvocar.setImageResource(R.drawable.audiof);
        Toast.makeText(EvocarActivity.this,R.string.Recording ,
                Toast.LENGTH_SHORT).show();
        tvRecording.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.VISIBLE);
        chronometer.start();

    }
    private void pararGrabar(){
        if(mr!=null){
            mr.stop();
            mr.release();
            mr=null;
        }
        tvRecording.setVisibility(View.INVISIBLE);
        chronometer.stop();
        chronometer.setVisibility(View.INVISIBLE);
        chronometer.setBase(0);

        ibPlayEvocar.setEnabled(true);
        ibRecordEvocar.setEnabled(true);
        ibPlayEvocar.setImageResource(R.drawable.audio);
        Toast.makeText(EvocarActivity.this,R.string.StopRecording ,
                Toast.LENGTH_SHORT).show();
        btNext.setEnabled(true);
        btNext.setVisibility(View.VISIBLE);
    }
    private void reproduir(){
        mp = new MediaPlayer();
        ibRecordEvocar.setEnabled(false);
        ibRecordEvocar.setImageResource(R.drawable.microf);
        try {
            mp.setDataSource(outputFile);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        ibRecordEvocar.setEnabled(true);

    }
    private void pararReproduccio(){
        if(mp.isPlaying()){
            mp.stop();
        }
        ibRecordEvocar.setEnabled(true);
        ibRecordEvocar.setImageResource(R.drawable.microphone);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evocar);

        //Extreure dades pacient
        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String pacient = prefs.getString("pacient",null);
        PacientUsuari pacientusuari = gson.fromJson(pacient, PacientUsuari.class);
        ID_usuari = pacientusuari.getID();
        episodi = prefs.getString("episodi",null);
        //Comprobar si és versió curta
        if (prefs.getString("Versio",null).matches("Short")) curta = true;

        //Evocar C
        if(curta){
            DialegPrimerCurta();
            outputFile = new Environment().getExternalStorageDirectory().getAbsolutePath()+"/"+pacientusuari.getID()+pacientusuari.getName()+"_EvocarC.3gp";
            NomFitxerCloud = "_EvocarC.3gp";
            intent = new Intent (EvocarActivity.this, EscenaCurtaActivity.class);
        }

        if(curta && getIntent().hasExtra("EvocarD")){
            DialegPrimerCurta();
            outputFile = new Environment().getExternalStorageDirectory().getAbsolutePath()+"/"+pacientusuari.getID()+pacientusuari.getName()+"_EvocarD.3gp";
            NomFitxerCloud = "_EvocarD.3gp";
            intent = new Intent (EvocarActivity.this, RespirarActivity1.class);
            intent.putExtra("Curta2","Curta2");
        }


        if(getIntent().hasExtra("Quarta")){
            DialegCongrats(); //Felicitem a l'usuari
            DialegSegon(); //Instruccions diferents a si es la primera vegada
            outputFile = new Environment().getExternalStorageDirectory().getAbsolutePath()+"/"+pacientusuari.getID()+pacientusuari.getName()+"_EvocarB.3gp";
            NomFitxerCloud = "_EvocarB.3gp";
            intent = new Intent (EvocarActivity.this, EmocionsActivity.class);
        }
        if(getIntent().hasExtra("Primer")){
            DialegPrimer();
            outputFile = new Environment().getExternalStorageDirectory().getAbsolutePath()+"/"+pacientusuari.getID()+pacientusuari.getName()+"_EvocarA.3gp";
            intent = new Intent(EvocarActivity.this,RespirarActivity1.class);
            NomFitxerCloud = "_EvocarA.3gp";
            intent.putExtra("Segon","Segon");
        }

        ibRecordEvocar = (ImageButton) findViewById(R.id.ibRecordEvocar);
        ibStopPlayEvocar = (ImageButton) findViewById(R.id.ibStopPlayEvocar);
        ibPlayEvocar = (ImageButton) findViewById(R.id.ibPlayEvocar);
        ibStopRecordEvocar = (ImageButton) findViewById(R.id.ibStopRecordEvocar);

        tvRecording = (TextView) findViewById(R.id.tvRecordingEvocar);
        tvRecording.setVisibility(View.INVISIBLE);


        btBack = (Button) findViewById(R.id.btBackWeather);
        btNext = (Button) findViewById(R.id.btNextWeather);

        chronometer = (Chronometer) findViewById(R.id.chronometerEvocar);
        chronometer.setBase(0);
        chronometer.setVisibility(View.INVISIBLE);

        ibRecordEvocar.setOnClickListener(this);
        ibStopPlayEvocar.setOnClickListener(this);
        ibPlayEvocar.setOnClickListener(this);
        ibStopRecordEvocar.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btBack.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mr.release();
        mp.release();
    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btSignOutMenu) {

            //Retorna a la pantalla inicial
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(EvocarActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EvocarActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(EvocarActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EvocarActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }

    private void DialegPrimer(){
        AlertDialog.Builder dialeg =new AlertDialog.Builder(EvocarActivity.this);
        dialeg
                .setTitle(getString(R.string.Attention))
                .setMessage(getString(R.string.EvocarAdiaelg))
                .setPositiveButton(R.string.Listen, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        reproduirMissatgeDialeg();
                        arg0.dismiss();
                        arg0.cancel();
                    }
                })
                .setNegativeButton(R.string.NoListen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void DialegPrimerCurta(){
        AlertDialog.Builder dialeg =new AlertDialog.Builder(EvocarActivity.this);
        dialeg
                .setTitle(getString(R.string.Attention))
                .setMessage(getString(R.string.Evocarccurta))
                .setPositiveButton(R.string.Listen, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        reproduirMissatgeDialeg();
                        arg0.dismiss();
                        arg0.cancel();
                    }
                })
                .setNegativeButton(R.string.NoListen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void DialegSegon(){
        AlertDialog.Builder dialeg =new AlertDialog.Builder(EvocarActivity.this);
        dialeg
                .setTitle(getString(R.string.Attention))
                .setMessage(getString(R.string.EvocarB))
                .setPositiveButton(R.string.Listen, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        reproduirMissatgeDialeg();
                        arg0.dismiss();
                        arg0.cancel();
                    }
                })
                .setNegativeButton(R.string.NoListen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void DialegCongrats(){
        AlertDialog.Builder dialeg =new AlertDialog.Builder(EvocarActivity.this);
        dialeg
                .setTitle(getString(R.string.Congratulations))
                .setMessage(getString(R.string.DoingGreat))
                .setPositiveButton(R.string.ThankYou, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        arg0.cancel();
                    }
                })
                .show();
    }



    private void reproduirMissatgeDialeg(){
        MediaPlayer mp = MediaPlayer.create(this,R.raw.evocara);
        try{
            mp.prepare();
        }catch (Exception e){
            Toast.makeText(EvocarActivity.this, e.toString(),
                    Toast.LENGTH_LONG).show();e.toString();
        }
        mp.start();
        while(mp.isPlaying()){
        }
        mp.stop();
        mp.release();
    }
}
