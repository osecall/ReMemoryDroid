package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class TractamentsActivity extends AppCompatActivity {

    private TextView idCuUserTreatment, NomCuUserTreatment, CognomCuUserTreatment;
    private Button btPelicula, btAlbum, btGuia, btJocs, btBackEpisodiActivity;
    private Intent intentParent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tractaments);

        idCuUserTreatment = (TextView) findViewById(R.id.tvIDTreatment);
        NomCuUserTreatment = (TextView) findViewById(R.id.tvNameTreatment);
        CognomCuUserTreatment = (TextView) findViewById(R.id.tvSurTreatment);


        btPelicula= (Button) findViewById(R.id.btPelicula);
        btAlbum = (Button) findViewById(R.id.btAlbum);
        btGuia = (Button) findViewById(R.id.btGuia);
        btJocs = (Button) findViewById(R.id.btJocs);
        btBackEpisodiActivity = (Button) findViewById(R.id.btBackEpisode);

        //Llegim informació de l'usuari
        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json= prefs.getString("pacient","");
        PacientUsuari obj = gson.fromJson(json, PacientUsuari.class);

        idCuUserTreatment.setText(obj.getID());
        NomCuUserTreatment.setText(obj.getName());
        CognomCuUserTreatment.setText(obj.getSurName());

        //Deshabilitar alguns botons si s'ha escollit versió llarga

        //intentParent = getIntent();
        //String version = intentParent.getStringExtra("versio").toString();

        String version = prefs.getString("versio",null);


        if(version.contains("Long version")){
            btAlbum.setEnabled(false);
            btGuia.setEnabled(false);
            btJocs.setEnabled(false);
        }

        btBackEpisodiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(TractamentsActivity.this, EpisodiActivity.class);
                startActivity(backIntent);
            }
        });



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
            Toast.makeText(TractamentsActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(TractamentsActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(TractamentsActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(TractamentsActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}