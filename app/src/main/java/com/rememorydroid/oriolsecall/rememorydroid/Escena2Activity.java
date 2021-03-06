package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Escena2Activity extends BaseActivity {

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

        A = getIntent().getStringExtra("A").toLowerCase();
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

        episodi = ObtenirEpisodi();
        pacient = ObtenirPacient();

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
                showToastError();

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
                final AlertDialog.Builder Dialeg1 = new AlertDialog.Builder(Escena2Activity.this);
                LayoutInflater factory = LayoutInflater.from(Escena2Activity.this);
                final View textEntryView = factory.inflate(R.layout.dialegs, null);
                TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
                Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);
                tv.setText(getString(R.string.Fantastic,pacient.getName()));
                Dialeg1
                        .setTitle(getString(R.string.Congratulations))
                        .setCancelable(false)
                        .setView(textEntryView);

                final AlertDialog alerta1 = Dialeg1.create();

                alerta1.show();

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alerta1.dismiss();

                        final AlertDialog.Builder Dialeg2 = new AlertDialog.Builder(Escena2Activity.this);
                        LayoutInflater factory = LayoutInflater.from(Escena2Activity.this);
                        final View textEntryView = factory.inflate(R.layout.dialegs, null);
                        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
                        Button bt2 = (Button) textEntryView.findViewById(R.id.btDiaelgOK);
                        tv.setText(R.string.ToEnd);
                        Dialeg2
                                .setCancelable(false)
                                .setView(textEntryView);


                        final AlertDialog alerta2 = Dialeg2.create();

                        alerta2.show();

                        bt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Escena2Activity.this, TestActivity.class).putExtra("SegonTest","SegonTest"));
                                alerta2.dismiss();
                            }
                        });


                    }
                });


            }
        });


    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient)+" ("+pacient.getID()+")");
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
            showToast(getString(R.string.signed_out),true);
            Intent areaAvaluador = new Intent(Escena2Activity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'
            BorrarPacient();
            showToast(getString(R.string.MenuChangePacient),true);
            Intent areaAvaluador = new Intent(Escena2Activity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
