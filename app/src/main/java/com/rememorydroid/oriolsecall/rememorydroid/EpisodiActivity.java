package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EpisodiActivity extends AppCompatActivity {

    private TextView tvVersioSelected, tvEpisodiSelected;
    private ListView lista, listaVersio;
    private Button btNextEpisode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodi);

        tvVersioSelected = (TextView) findViewById(R.id.tvVersioSelected);
        tvEpisodiSelected = (TextView) findViewById(R.id.tvEpisodiSelected);
        btNextEpisode = (Button) findViewById(R.id.btNextEpisode);


        ArrayAdapter<String> adaptador;
        ArrayAdapter<String> adaptadorVersio;

        lista = (ListView) findViewById(R.id.lvEpisodis);
        listaVersio = (ListView) findViewById(R.id.lvVersio);

        adaptador = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adaptadorVersio = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);

        adaptadorVersio.add(getString(R.string.LongVersion));
        adaptadorVersio.add(getString(R.string.ShortVersion));


        adaptador.add("holaaaaa");
        adaptador.add("asd");
        adaptador.add("dsfsdfsdf");
        adaptador.add("dddddd");        adaptador.add("dddddd");
        adaptador.add("dddddd");
        adaptador.add("eee");
        adaptador.add("ddd445ddd");
        adaptador.add("ddd45454322ddd");
        adaptador.add("ddd32423234ddd");
        adaptador.add("dddddd");

        adaptador.add("reter");

        adaptador.add("hghfh");

        adaptador.add("7878");




        lista.setAdapter(adaptador);
        listaVersio.setAdapter(adaptadorVersio);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (String) lista.getItemAtPosition(i);

                tvEpisodiSelected.setText(selectedFromList);


                Toast.makeText(EpisodiActivity.this,selectedFromList ,
                        Toast.LENGTH_LONG).show();
            }
        });


        listaVersio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (String) listaVersio.getItemAtPosition(i);

                tvVersioSelected.setText(selectedFromList);


                Toast.makeText(EpisodiActivity.this,selectedFromList ,
                        Toast.LENGTH_LONG).show();
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
                    TractamentIntent.putExtra("versio",tvVersioSelected.getText().toString());
                    TractamentIntent.putExtra("episodi",tvEpisodiSelected.getText().toString());

                    SharedPreferences prefs = getSharedPreferences("pacient_cu", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("versio",tvVersioSelected.getText().toString());
                    editor.putString("episodi", tvEpisodiSelected.getText().toString());

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
