package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class WhenItHappenedActivity extends AppCompatActivity {

    private Button btNext, btBack;
    private RadioGroup radioGroup;
    private TextView tvSubTitol;
    private String RadioSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_when_it_happened);

        btNext = (Button) findViewById(R.id.btNextWeather);
        btBack = (Button) findViewById(R.id.btBackWeather);

        tvSubTitol = (TextView) findViewById(R.id.tvWhenSubTime);

        radioGroup = (RadioGroup) findViewById(R.id.rgWhenTime);

        btNext.setVisibility(View.INVISIBLE);
        btNext.setEnabled(false);




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                //Busquem quin radioButton s'ha seleccionat
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                RadioButton rb = (RadioButton) findViewById(radioButtonID);
                RadioSelected= rb.getText().toString();

                btNext.setVisibility(View.VISIBLE);
                btNext.setEnabled(true);

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btNext.setVisibility(View.INVISIBLE);

                        tvSubTitol.setText(R.string.WhatWasTheWeather);
                        //Recuperem el radiobutton checked
                        int checked = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioChecked = (RadioButton) findViewById(checked);
                        Toast.makeText(WhenItHappenedActivity.this, radioChecked.getText().toString(),
                        Toast.LENGTH_LONG).show();

                        //Guardem a TestAnswers
                        Gson gson = new Gson();
                        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        String respostes_json = prefs.getString("respostes",null);
                        TestAnswers respostes_recuperades = gson.fromJson(respostes_json,TestAnswers.class);
                        respostes_recuperades.setPreguntesQuan_Temps(radioChecked.getText().toString());
                        //-----------------------------------------

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
            Toast.makeText(WhenItHappenedActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(WhenItHappenedActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(WhenItHappenedActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(WhenItHappenedActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
