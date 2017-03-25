package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AreaAvaluadorActivity extends AppCompatActivity {

    private TextView emailAvaluador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_avaluador);



        emailAvaluador = (TextView) findViewById(R.id.tvAssessersSessionEmail);
        emailAvaluador.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());



        //Diàleg

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Attention))
                .setMessage("some message")
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Some stuff to do when ok got clicked
                    }
                })
                .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Some stuff to do when cancel got clicked
                    }
                })
                .show();


    }


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
            Toast.makeText(AreaAvaluadorActivity.this, R.string.signed_out,
                  Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(AreaAvaluadorActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
