package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
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
    private TextView tvEpisodePresenName, tvEpisodePresenDate,tvEpisodePresenTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_presentation);

        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(2500);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);

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
                        tvEpisodePresenName.setText(dataSnapshot.child(ID_pacient).child("episodis").child(episodi).child("Name").getValue(String.class));
                        tvEpisodePresenTime.setText(dataSnapshot.child(ID_pacient).child("episodis").child(episodi).child("Hora").getValue(String.class));
                        tvEpisodePresenDate.setText(dataSnapshot.child(ID_pacient).child("episodis").child(episodi).child("Fecha").getValue(String.class));
            hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(EpisodePresentationActivity.this,"Error en connectar base de dades" ,
                        Toast.LENGTH_LONG).show();

            }
        });

        tvEpisodePresenName.setAnimation(animation);
        tvEpisodePresenDate.setAnimation(animation);
        tvEpisodePresenTime.setAnimation(animation);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                startActivity(new Intent(EpisodePresentationActivity.this, TestActivity.class));
                finish();
            }
        }, 8000);






    }
}
