package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class EscenaActivity2 extends BaseActivity {

    private String A,B,C,D,E, episodi;
    private TextView tvNomEpisodiEscena2, tvAEscena2,tvBEscena2,tvCEscena2,tvDEscena2,tvEEscena2;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("pacients");
    private PacientUsuari pacient;
    private Uri uri;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escena2);

        A= getIntent().getStringExtra("A");
        B= getIntent().getStringExtra("B");
        C= getIntent().getStringExtra("C");
        D= getIntent().getStringExtra("D");
        E= getIntent().getStringExtra("E");
        uri= Uri.parse(getIntent().getStringExtra("favorita"));

        tvNomEpisodiEscena2 = (TextView) findViewById(R.id.tvNomEpisodiEscena2);
        tvAEscena2 = (TextView) findViewById(R.id.tvAEscena2);
        tvBEscena2 = (TextView) findViewById(R.id.tvBEscena2);
        tvCEscena2 = (TextView) findViewById(R.id.tvCEscena2);
        tvDEscena2 = (TextView) findViewById(R.id.tvDEscena2);
        tvEEscena2 = (TextView) findViewById(R.id.tvEEscena2);



        image = (ImageView) findViewById(R.id.ivEscena2);

        showProgressDialog();
        Picasso.with(EscenaActivity2.this).load(uri).into(image);
        hideProgressDialog();

        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        String pacient_json = prefs.getString("pacient",null);
        episodi = prefs.getString("episodi",null);
        Gson temp = new Gson();
        pacient = temp.fromJson(pacient_json, PacientUsuari.class);

        showProgressDialog();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvNomEpisodiEscena2.setText(dataSnapshot.child(pacient.getID()).child("episodis").child(episodi).child("Name").getValue(String.class));
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(EscenaActivity2.this,"Error en connectar base de dades" ,
                        Toast.LENGTH_LONG).show();

            }
        });

        tvAEscena2.setText(getString(R.string.ImFeeling,A));
        tvBEscena2.setText(getString(R.string.BeingWith,B));
        tvCEscena2.setText(getString(R.string.InSub,C));
        tvDEscena2.setText(getString(R.string.InTheSub,D));
        tvEEscena2.setText(getString(R.string.WeAreSub,E));


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
            Toast.makeText(EscenaActivity2.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EscenaActivity2.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(EscenaActivity2.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EscenaActivity2.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
