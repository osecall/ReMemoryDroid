package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class EscenaCurtaActivity extends BaseActivity {

    private ImageView ivPicturePreferred;

    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefFavour;
    private String Episodi;
    private Button btNextEscenaCurta;
    private PacientUsuari pacient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escena_curta);

        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String pacient_json= prefs.getString("pacient",null);
        Episodi = prefs.getString("episodi",null);

        pacient = gson.fromJson(pacient_json, PacientUsuari.class);

        ivPicturePreferred = (ImageView) findViewById(R.id.ivPicturePreferredCurta);
        btNextEscenaCurta = (Button) findViewById(R.id.btNextEscenaCurta);

        myRefFavour = myRef.child(pacient.getID()).child(Episodi).child("favorita").child("favorita.jpg");

        showProgressDialog();

        myRefFavour.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(EscenaCurtaActivity.this).load(uri).into(ivPicturePreferred);
                hideProgressDialog();
            }
        });

        btNextEscenaCurta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EscenaCurtaActivity.this, RespirarActivity.class);
                intent.putExtra("Curta1","Curta1");
                startActivity(intent);
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
            Intent areaAvaluador = new Intent(EscenaCurtaActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            showToast(getString(R.string.MenuChangePacient),true);

            Intent areaAvaluador = new Intent(EscenaCurtaActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
