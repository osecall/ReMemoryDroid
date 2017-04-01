package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class RespirarActivity1 extends AppCompatActivity {

    private MediaPlayer mp;
    private Intent intentRespirar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respirar1);
        intentRespirar = new Intent(RespirarActivity1.this, VisualitzarActivity1.class);

        MediaPlayer mp = MediaPlayer.create(this,R.raw.test);
        DialogInstruccionsRespirar(mp);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mp.release();
    }

    private void DialogInstruccionsRespirar(final MediaPlayer mp){
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(RespirarActivity1.this);
        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setMessage(R.string.InstructonsBreathing)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        try{
                            mp.prepare();
                        }catch (Exception e){
                            Toast.makeText(RespirarActivity1.this, e.toString(),
                                    Toast.LENGTH_LONG).show();e.toString();
                        }
                        mp.start();

                        while(mp.isPlaying()){
                        }
                        mp.stop();
                        startActivity(intentRespirar);
                    }
                })
                .show();
    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuavaluadors, menu);
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
            Toast.makeText(RespirarActivity1.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(RespirarActivity1.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(RespirarActivity1.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(RespirarActivity1.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
