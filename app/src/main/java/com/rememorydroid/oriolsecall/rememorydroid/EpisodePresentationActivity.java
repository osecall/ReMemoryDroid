package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class EpisodePresentationActivity extends BaseActivity {

    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("pacients");
    private String ID_pacient, episodi;
    private Intent parentIntent;
    private TextView tvEpisodePresenName, tvEpisodePresenDate,tvEpisodePresenTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_presentation);

        tvEpisodePresenName = (TextView) findViewById(R.id.tvEpisodePresenName);
        tvEpisodePresenDate = (TextView) findViewById(R.id.tvEpisodePresenDate);
        tvEpisodePresenTime = (TextView) findViewById(R.id.tvEpisodePresenTime);

        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        String pacient_json = prefs.getString("pacient",null);
        episodi = prefs.getString("episodi",null);
        Gson temp = new Gson();
        PacientUsuari pacient = temp.fromJson(pacient_json, PacientUsuari.class);


        ID_pacient=pacient.getID();

        showProgressDialog();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot node: dataSnapshot.getChildren()){
                    if (node.child("id").getValue(String.class).equals(ID_pacient)){
                        tvEpisodePresenName.setText(node.child("episodis").child(episodi).child("Name").getValue(String.class));
                        tvEpisodePresenTime.setText(node.child("episodis").child(episodi).child("Hora").getValue(String.class));
                        tvEpisodePresenDate.setText(node.child("episodis").child(episodi).child("Fecha").getValue(String.class));
                  }
                }
            hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(EpisodePresentationActivity.this,"Error en connectar base de dades" ,
                        Toast.LENGTH_LONG).show();

            }
        });

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(EpisodePresentationActivity.this, PeliculaActivity.class);
                startActivity(intent);
                finish();
            };
        }, 5000);


    }
}
