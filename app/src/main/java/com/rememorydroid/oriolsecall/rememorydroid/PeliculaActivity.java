package com.rememorydroid.oriolsecall.rememorydroid;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PeliculaActivity extends AppCompatActivity {

    private TextDrawable FromPage, ToPage, NumeroSeleccionat;
    private ImageView ivFromPage, ivToPage, ivNumSeleccionat;
    private Button btBack, btNext;
    private Intent intentPel1;
    private RadioGroup rbGroup;
    protected TestAnswers respostes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        ColorGenerator generator = ColorGenerator.DEFAULT;
        FromPage = TextDrawable.builder().beginConfig().width(65).height(65).endConfig().buildRound("1",generator.getRandomColor());
        ToPage = TextDrawable.builder().beginConfig().width(65).height(65).endConfig().buildRound("30",generator.getRandomColor());

        ivFromPage = (ImageView) findViewById(R.id.ivFromPage1);
        ivToPage = (ImageView) findViewById(R.id.ivToPage1);
        ivNumSeleccionat= (ImageView) findViewById(R.id.ivNumSeleccionat);

        ivFromPage.setImageDrawable(FromPage);
        ivToPage.setImageDrawable(ToPage);

        btBack = (Button) findViewById(R.id.btBackPel1);
        btNext = (Button) findViewById(R.id.btNextPel1);

        btNext.setEnabled(false);

        rbGroup = (RadioGroup) findViewById(R.id.rbGroup1Pel1);

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                //Busquem quin radioButton s'ha seleccionat
                int radioButtonID = rbGroup.getCheckedRadioButtonId();
                //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                RadioButton hola = (RadioButton) findViewById(radioButtonID);
                String RadioSelected= hola.getText().toString();



                btNext.setEnabled(true);



                NumeroSeleccionat = TextDrawable.builder().beginConfig().width(150).height(150).endConfig().buildRound(RadioSelected,ColorGenerator.DEFAULT.getRandomColor());
                ivNumSeleccionat.setImageDrawable(NumeroSeleccionat);
                ivNumSeleccionat.setVisibility(View.VISIBLE);
            }
        });


        //Enrere
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentPel1 = new Intent (PeliculaActivity.this, TractamentsActivity.class);
                startActivity(intentPel1);
            }
        });

        //Endevant
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Guardem dada sel·leccionada a la classe TestAnswers
                //respostes.setValorRadius...
                rbGroup.getCheckedRadioButtonId();

                SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String respostes_json = gson.toJson(respostes,TestAnswers.class);
                editor.putString("resposta",respostes_json);
                editor.commit();

                intentPel1 = new Intent (PeliculaActivity.this, PeliculaActivity2.class);
                startActivity(intentPel1);

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
            Toast.makeText(PeliculaActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(PeliculaActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(PeliculaActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(PeliculaActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }




}
