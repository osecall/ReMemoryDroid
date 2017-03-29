package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EpisodePresentationActivity extends AppCompatActivity {

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

        parentIntent= getIntent();
        ID_pacient=parentIntent.getStringExtra("ID");
        episodi=parentIntent.getStringExtra("episodi");

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EpisodePresentationActivity.this,"Error en connectar base de dades" ,
                        Toast.LENGTH_LONG).show();

            }
        });




    }
}
