package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

public class PeliculaActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula2);

        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        String respostes_json = prefs.getString("respostes",null);
        Gson temp = new Gson();
        TestAnswers respostes = temp.fromJson(respostes_json, TestAnswers.class);


        Toast.makeText(PeliculaActivity2.this,String.valueOf(respostes.getTest1Pregunta1()) ,
                Toast.LENGTH_LONG).show();

    }
}
