package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

public class VisualitzarActivity1 extends AppCompatActivity {

    private MediaPlayer mp,mp2;
    private VideoView vv;
    private ImageButton ibPlay, ibStop;
    private Button btBack, btNext;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualitzar1);

        vv = (VideoView) findViewById(R.id.vvVisualitzar1);
        ibPlay = (ImageButton) findViewById(R.id.ibPlay);
        ibStop = (ImageButton) findViewById(R.id.ibStop);
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
            //Es ficarà en conjunt al vídeo, com a so
            mp2 = MediaPlayer.create(this, R.raw.visualitzar2peliculabackground);

            intent=new Intent(VisualitzarActivity1.this,QuestionariActivity.class);
        }
        else if(getIntent().hasExtra("Tercer")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2);
            vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarActivity1.this,QuestionariActivity2.class);
        }
        else if(getIntent().hasExtra("Quarta")){
            mp = MediaPlayer.create(this, R.raw.visualitzar2);
            vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarActivity1.this,EvocarActivity.class);
            intent.putExtra("Quarta","Quarta");
        }
        else{
            //Per les instruccions
            mp = MediaPlayer.create(this, R.raw.visualitzar1);
            //Vídeo
            vv.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));
            intent=new Intent(VisualitzarActivity1.this,EvocarActivity.class);
        }
        DialogInstruccionsVisualitzar(mp);


        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(vv.isPlaying()){
                    vv.resume();
                }
                if(!vv.isPlaying()){
                    vv.start();
                }
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vv.pause();
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (VisualitzarActivity1.this, RespirarActivity1.class));
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
    }

    private void DialogInstruccionsVisualitzar(final MediaPlayer mp){
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(VisualitzarActivity1.this);
        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setCancelable(false)
                .setMessage(R.string.DialogVideo1)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        try{
                            mp.prepare();
                        }catch (Exception e){
                            Toast.makeText(VisualitzarActivity1.this, e.toString(),
                                    Toast.LENGTH_LONG).show();e.toString();
                        }
                        mp.start();
                        while(mp.isPlaying()){
                        }
                        mp.stop();
                        mp.release();
                        btNext.setVisibility(View.VISIBLE);
                        ibPlay.setVisibility(View.VISIBLE);
                        ibStop.setVisibility(View.VISIBLE);
                        ibPlay.setEnabled(true);
                        ibStop.setEnabled(true);

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
            Toast.makeText(VisualitzarActivity1.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(VisualitzarActivity1.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(VisualitzarActivity1.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(VisualitzarActivity1.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
