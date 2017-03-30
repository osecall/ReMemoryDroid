package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class EpisodiActivity extends AppCompatActivity {

    private TextView tvVersioSelected, tvEpisodiSelected;
    private ListView lista, listaVersio;
    private Button btNextEpisode;
    private ImageView ivDrawableLlarga, ivDrawableCurta;
    private StorageReference mStorageRef;
    private Intent parentIntent;
    private String episodiSeleccionat;
    private Gson gson;
    private long i=0;
    private long j=0;
    private ArrayAdapter<String> adaptadorEpisodis;
    String ID_pacient = new String();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("pacients");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodi);

        //Recuperem pacient
        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        String pacient_json = prefs.getString("pacient",null);
        Gson temp = new Gson();
        PacientUsuari pacient = temp.fromJson(pacient_json, PacientUsuari.class);


        ID_pacient = pacient.getID().toString(); //ID del pacient per recuperar episodis i posar-los a la llista
        //Ara ja tenim l'objecte PacientUsuari

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot node: dataSnapshot.getChildren()){
                    if (node.child("id").getValue(String.class).equals(ID_pacient)){
                        i= node.child("episodis").getChildrenCount(); //Guardem el número d'episodis

                        while(j<i){
                            String valor_de_j_string = String.valueOf(j).toString();
                            adaptadorEpisodis.add(String.valueOf(j)+"\t\t\t"+node.child("episodis").child(valor_de_j_string).child("Name").getValue(String.class) +" ("+node.child("episodis").child(valor_de_j_string).child("Fecha").getValue(String.class)+")");
                            j++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EpisodiActivity.this,"Error en connectar base de dades" ,
                        Toast.LENGTH_LONG).show();

            }
        });



        tvVersioSelected = (TextView) findViewById(R.id.tvVersioSelected);
        tvEpisodiSelected = (TextView) findViewById(R.id.tvEpisodiSelected);
        btNextEpisode = (Button) findViewById(R.id.btNextEpisode);

        ivDrawableLlarga = (ImageView) findViewById(R.id.ivDrawableLlarga);
        ivDrawableCurta = (ImageView) findViewById(R.id.ivDrawableCurta);

        ArrayAdapter<String> adaptadorVersio;

        lista = (ListView) findViewById(R.id.lvEpisodis);
        listaVersio = (ListView) findViewById(R.id.lvVersio);


        adaptadorVersio = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);

        adaptadorVersio.add(getString(R.string.LongVersion));
        adaptadorVersio.add(getString(R.string.ShortVersion));

        adaptadorEpisodis = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);


        ColorGenerator generator = ColorGenerator.DEFAULT;
        TextDrawable drawableL = TextDrawable.builder().beginConfig().width(75).height(75).endConfig().buildRound("L",generator.getRandomColor());
        TextDrawable drawableS = TextDrawable.builder().beginConfig().width(75).height(75).endConfig().buildRound("S",generator.getRandomColor());

        ivDrawableLlarga.setImageDrawable(drawableS);
        ivDrawableCurta.setImageDrawable(drawableL);



        lista.setAdapter(adaptadorEpisodis);
        listaVersio.setAdapter(adaptadorVersio);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (String) lista.getItemAtPosition(i);

                tvEpisodiSelected.setText(getString(R.string.EpisodeSelected,selectedFromList));
                episodiSeleccionat=String.valueOf(i);


                Toast.makeText(EpisodiActivity.this,selectedFromList ,
                        Toast.LENGTH_SHORT).show();
            }
        });


        listaVersio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (String) listaVersio.getItemAtPosition(i);

                tvVersioSelected.setText(getString(R.string.VersionSelected,selectedFromList));

                Toast.makeText(EpisodiActivity.this,selectedFromList ,
                        Toast.LENGTH_SHORT).show();
            }
        });


        //Agregar passar informació episodi i versio quan s'apreta botó


        btNextEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tvVersioSelected.getText().toString().isEmpty() || tvEpisodiSelected.getText().toString().isEmpty()){
                    new AlertDialog.Builder(EpisodiActivity.this)
                            .setTitle(getString(R.string.Attention))
                            .setMessage(getString(R.string.SelectionVersionEpisode))
                            .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }

                            })
                            .show();
                }
                else{
                    Intent TractamentIntent = new Intent(EpisodiActivity.this, TractamentsActivity.class);
                    //Passem versió per intent ja que només s'usarà a la pròxima activity una vegada
                    TractamentIntent.putExtra("versio",tvVersioSelected.getText().toString());

                    SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("episodi", episodiSeleccionat);
                    editor.commit();

                    startActivity(TractamentIntent);



                }
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
            Toast.makeText(EpisodiActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EpisodiActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(EpisodiActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EpisodiActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
