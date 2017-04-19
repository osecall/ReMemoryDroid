package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escena_curta);

        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String pacient_json= prefs.getString("pacient",null);
        Episodi = prefs.getString("episodi",null);

        PacientUsuari pacient = gson.fromJson(pacient_json, PacientUsuari.class);

        ivPicturePreferred = (ImageView) findViewById(R.id.ivPicturePreferredCurta);
        btNextEscenaCurta = (Button) findViewById(R.id.btNextEscenaCurta);

        myRefFavour = myRef.child(pacient.getID()).child(Episodi).child("Favorita").child("favorita.jpg");

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
                Intent intent = new Intent(EscenaCurtaActivity.this, RespirarActivity1.class);
                intent.putExtra("Curta1","Curta1");
                startActivity(intent);
            }
        });





    }
}
