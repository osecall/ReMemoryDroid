package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class PeliculaActivity6 extends BaseActivity {

    private TextDrawable FromPage, ToPage, NumeroSeleccionat;
    private ImageView ivFromPage, ivToPage, ivNumSeleccionat;
    private Button btBack, btNext;
    private Intent intentPel1;
    private RadioGroup rbGroup;
    private String RadioSelected;
    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference PacientRef;
    private DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference("pacients");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula6);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(R.string.SixSix);

        if(getIntent().hasExtra("SegonTest")) actionBar.setTitle("Test2");

        ColorGenerator generator = ColorGenerator.DEFAULT;
        FromPage = TextDrawable.builder().beginConfig().width(65).height(65).endConfig().buildRound("6",generator.getRandomColor());
        ToPage = TextDrawable.builder().beginConfig().width(65).height(65).endConfig().buildRound("6",generator.getRandomColor());

        ivFromPage = (ImageView) findViewById(R.id.ivFromPage1);
        ivToPage = (ImageView) findViewById(R.id.ivToPage1);
        ivNumSeleccionat= (ImageView) findViewById(R.id.ivNumSeleccionat);

        ivFromPage.setImageDrawable(FromPage);
        ivToPage.setImageDrawable(ToPage);

        btBack = (Button) findViewById(R.id.btBackPel1);
        btNext = (Button) findViewById(R.id.btNextPel1);

        btNext.setEnabled(false);
        btNext.setVisibility(View.INVISIBLE);

        rbGroup = (RadioGroup) findViewById(R.id.rbGroup1Pel1);

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                //Busquem quin radioButton s'ha seleccionat
                int radioButtonID = rbGroup.getCheckedRadioButtonId();
                //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                RadioButton rb = (RadioButton) findViewById(radioButtonID);
                RadioSelected= rb.getText().toString();

                btNext.setVisibility(View.VISIBLE);
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
                intentPel1 = new Intent (PeliculaActivity6.this, PeliculaActivity.class);
                startActivity(intentPel1);
            }
        });

        //Endevant
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intentPel1 = new Intent (PeliculaActivity6.this, RespirarActivity1.class);

                //Per controlar si es segona vegada el test
                if(getIntent().hasExtra("SegonTest")){
                    mostrarAlertaDescans();
                    Gson gson = new Gson();
                    SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    String respostes_json = prefs.getString("respostes",null);
                    String episodi = prefs.getString("episodi",null);
                    PacientUsuari pacient = gson.fromJson(prefs.getString("pacient",null),PacientUsuari.class);
                    TestAnswers respostes_recuperades = gson.fromJson(respostes_json,TestAnswers.class);

                    //Passem valor sel·leccionat com Integer
                    respostes_recuperades.setTest2Pregunta6(Integer.parseInt(RadioSelected));
                    respostes_recuperades.setTest2Sumatori();
                    respostes_json = gson.toJson(respostes_recuperades,TestAnswers.class);
                    editor.putString("respostes",respostes_json);
                    editor.commit();
                    //Aqui enviem el fitxer CSV i JSON a FireBase i retornem a 'Tractaments'
                    ArrayList<String> rutes = respostes_recuperades.ConvertToCVS(PeliculaActivity6.this);
                    //Ara tenim la ruta del fitxer CSV[0] a la memoria de la tauleta i el JSON[1]
                    PacientRef = myRef.child(pacient.getID()).child(episodi).child("Resultat.csv");
                    Uri file = Uri.fromFile(new File(rutes.get(0)));

                    //Pujem el JSON a la base de dades
                    Gson gsonFile = new Gson();
                    TestAnswers respostesJSON = gsonFile.fromJson(rutes.get(1), TestAnswers.class);
                    DBRef.child(pacient.getID()).child("episodis").child(episodi).child("repostes").setValue(respostes_json);


                    // Create file metadata including the content type (CSV)
                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("text/csv")
                            .build();


                    showProgressDialog();
                    PacientRef.putFile(file,metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            while(!task.isSuccessful()){
                                showProgressDialog();
                            }
                            if(task.isComplete()){
                                hideProgressDialog();
                                Toast.makeText(PeliculaActivity6.this, R.string.UploadCSVSuccessful,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    final AlertDialog.Builder DialegDespedida = new AlertDialog.Builder(PeliculaActivity6.this);
                    DialegDespedida
                            .setCancelable(false)
                            .setMessage(R.string.ThankYouVeryMuch+"\n"+R.string.HopeEnjoy)
                            .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    startActivity(new Intent(PeliculaActivity6.this, TractamentsActivity.class));
                                    arg0.dismiss();
                                    finish();
                                }
                            }).show();



                }
                else{
                    mostrarAlertaDescans();
                    //Guardem dada sel·leccionada a la classe TestAnswers

                    Gson gson = new Gson();
                    SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    String respostes_json = prefs.getString("respostes",null);
                    TestAnswers respostes_recuperades = gson.fromJson(respostes_json,TestAnswers.class);

                    //Passem valor sel·leccionat com Integer
                    respostes_recuperades.setTest1Pregunta6(Integer.parseInt(RadioSelected));
                    respostes_recuperades.setTest1Sumatori();
                    respostes_json = gson.toJson(respostes_recuperades,TestAnswers.class);
                    editor.putString("respostes",respostes_json);
                    editor.commit();
                }
            }
        });
    }


    private void mostrarAlertaDescans(){
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(PeliculaActivity6.this);
        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setMessage(R.string.DialogResting)
                .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        startActivity(intentPel1);

                    }
                })
                .show();

    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
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
            Toast.makeText(PeliculaActivity6.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(PeliculaActivity6.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(PeliculaActivity6.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(PeliculaActivity6.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
