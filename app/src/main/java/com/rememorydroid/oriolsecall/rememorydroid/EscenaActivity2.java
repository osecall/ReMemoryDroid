package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class EscenaActivity2 extends BaseActivity {

    private String A,B,C,D,E, episodi;
    private TextView tvNomEpisodiEscena2, tvAEscena2,tvBEscena2,tvCEscena2,tvDEscena2,tvEEscena2;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("pacients");
    private PacientUsuari pacient;
    private ImageView image;
    private Button btNextEscena2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escena2);

        A= getIntent().getStringExtra("A");
        B= getIntent().getStringExtra("B");
        C= getIntent().getStringExtra("C");
        D= getIntent().getStringExtra("D");
        E= getIntent().getStringExtra("E");


        tvNomEpisodiEscena2 = (TextView) findViewById(R.id.tvNomEpisodiEscena2);
        tvAEscena2 = (TextView) findViewById(R.id.tvAEscena2);
        tvBEscena2 = (TextView) findViewById(R.id.tvBEscena2);
        tvCEscena2 = (TextView) findViewById(R.id.tvCEscena2);
        tvDEscena2 = (TextView) findViewById(R.id.tvDEscena2);
        tvEEscena2 = (TextView) findViewById(R.id.tvEEscena2);
        btNextEscena2 = (Button) findViewById(R.id.btNextEscena2);
        image = (ImageView) findViewById(R.id.ivEscena2);

        showProgressDialog();
        byte[] imatge_favorita = getIntent().getByteArrayExtra("favorita");
        Bitmap imatge_seleccionada = BitmapFactory.decodeByteArray(imatge_favorita, 0, imatge_favorita.length);
        image.setImageBitmap(imatge_seleccionada);
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


        btNextEscena2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(EscenaActivity2.this);
                DialegFormControl
                        .setTitle(getString(R.string.Congratulations))
                        .setCancelable(false)
                        .setMessage(R.string.Fantastic)
                        .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(EscenaActivity2.this);
                                DialegFormControl
                                        .setCancelable(false)
                                        .setMessage(R.string.ToEnd)
                                        .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                startActivity(new Intent(EscenaActivity2.this, PeliculaActivity.class).putExtra("SegonTest","SegonTest"));
                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();


            }
        });


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
