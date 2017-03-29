package com.rememorydroid.oriolsecall.rememorydroid;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class PeliculaActivity extends AppCompatActivity {

    private TextDrawable FromPage, ToPage;
    private ImageView ivFromPage, ivToPage, ivOpcio1,ivOpcio2,ivOpcio3,ivOpcio4;
    private Button btBack, btNext;
    private Intent intentPel1;
    private TestAnswers respostes = new TestAnswers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        ColorGenerator generator = ColorGenerator.DEFAULT;
        FromPage = TextDrawable.builder().beginConfig().width(70).height(70).endConfig().buildRound("1",generator.getRandomColor());
        ToPage = TextDrawable.builder().beginConfig().width(70).height(70).endConfig().buildRound("30",generator.getRandomColor());

        ivFromPage = (ImageView) findViewById(R.id.ivFromPage1);
        ivToPage = (ImageView) findViewById(R.id.ivToPage1);
        ivOpcio1 = (ImageView) findViewById(R.id.ivOpcio1Pel1);

        ivFromPage.setImageDrawable(FromPage);
        ivToPage.setImageDrawable(ToPage);

        btBack = (Button) findViewById(R.id.btBackPel1);
        btNext = (Button) findViewById(R.id.btNextPel1);


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


                /* Alternativa
                SharedPreferences prefs = getSharedPreferences("respostes", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("resposta","resposta");

                editor.commit();
                */
                Gson gson = new Gson();
                //Passem objecte amb respostas a JSON "String"
                String objJson = gson.toJson(respostes);

                intentPel1 = new Intent (PeliculaActivity.this, PeliculaActivity2.class);

                intentPel1.putExtra("TestAnswer",objJson);
                startActivity(intentPel1);

            }
        });






        //Part particular ^^^^^^^********************^^^^^^^^^^^^^^^^^^^^^^^^


        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getBaseContext(),
                R.animator.selecciobototest);
        set.setTarget(ivOpcio1);
        set.start();

        /*Animation test = AnimationUtils.loadAnimation(this,R.animator.selecciobototest);
        ivOpcio1.startAnimation(test);*/



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
