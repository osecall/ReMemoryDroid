package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class PacientUserSignUpActivity extends BaseActivity{


    private Button btSavePacientSignUp;
    private EditText etIDPacientSignUp, etNamePacientSignUp, etSurNamePacientSignUp, etLastNamePacientSignUp;
    private ImageView ivIDerror, ivNameError, ivSurError, ivLastError;
    private PacientUsuari pacient;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("pacients");



    private boolean controlFormulariSignUp(String ID, String Nom, String Cognom, String SeCognom){

        //Diàleg que apareixerà si troba error
        final AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(PacientUserSignUpActivity.this);
        DialegFormControl
                .setIcon(R.drawable.warningdialogdeleteuser)
                .setTitle(getString(R.string.Attention))
                .setMessage(" ")
                .setCancelable(true)
                .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Fer res
                    }
                });


            //Controls

            //ID
            if(ID.isEmpty()){
                DialegFormControl.setMessage(R.string.IDFieldEmpty);
                DialegFormControl.show();
                ivIDerror.setVisibility(View.VISIBLE);
                return false;

            }
            if(!android.text.TextUtils.isDigitsOnly(ID)){
                DialegFormControl.setMessage(R.string.IDonlyDigits);
                DialegFormControl.show();
                ivIDerror.setVisibility(View.VISIBLE);
                return false;
            }
            if(ID.length()>4){
                DialegFormControl.setMessage(R.string.IDlength4);
                DialegFormControl.show();
                ivIDerror.setVisibility(View.VISIBLE);
                return false;

            }
            if(ID.length()<1){
                DialegFormControl.setMessage(R.string.IDlength1);
                DialegFormControl.show();
                ivIDerror.setVisibility(View.VISIBLE);
                return false;

            }
            //Nom
            if(Nom.isEmpty()){
                DialegFormControl.setMessage(R.string.NameFieldEmpty);
                DialegFormControl.show();
                ivNameError.setVisibility(View.VISIBLE);
                return false;
            }
            if(Nom.length()>30 || Nom.length()<2){
                DialegFormControl.setMessage(R.string.NameLength);
                DialegFormControl.show();
                ivNameError.setVisibility(View.VISIBLE);
                return false;
            }

            if(!Nom.matches("^[a-z A-Z]+$")){
                DialegFormControl.setMessage(R.string.NameNotDigits);
                DialegFormControl.show();
                ivNameError.setVisibility(View.VISIBLE);
                return false;
            }
            if(Cognom.isEmpty()){
                DialegFormControl.setMessage(R.string.SurnameEmpty);
                DialegFormControl.show();
                ivSurError.setVisibility(View.VISIBLE);
                return false;
            }
            if(!Cognom.matches("^[a-z A-Z]+$")){
                DialegFormControl.setMessage(R.string.SurNameNotDigits);
                DialegFormControl.show();
                ivSurError.setVisibility(View.VISIBLE);
                return false;
            }
            if(Cognom.length()>30 || Cognom.length()<2){
                DialegFormControl.setMessage(R.string.SurnameLength);
                DialegFormControl.show();
                ivSurError.setVisibility(View.VISIBLE);
                return false;
            }
            if(SeCognom.isEmpty()) {
                DialegFormControl.setMessage(R.string.LastNameEmpty);
                DialegFormControl.show();
                ivLastError.setVisibility(View.VISIBLE);
                return false;
            }
            if(!SeCognom.matches("^[a-z A-Z]+$")){
                DialegFormControl.setMessage(R.string.LastNameNotDigits);
                DialegFormControl.show();
                ivLastError.setVisibility(View.VISIBLE);
                return false;
            }
            if(SeCognom.length()>30 || SeCognom.length()<2){
                DialegFormControl.setMessage(R.string.LastNameLength);
                DialegFormControl.show();
                ivLastError.setVisibility(View.VISIBLE);
                return false;
            }


        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_user_sign_up);

        //Instància butó Guardar
        btSavePacientSignUp = (Button) findViewById(R.id.btSavePacientSignUp);
        //Instàncies dels edittexts del formulari
        etIDPacientSignUp = (EditText) findViewById(R.id.etIDPacientSignUp);
        etNamePacientSignUp = (EditText) findViewById(R.id.etNamePacientSignUp);
        etSurNamePacientSignUp = (EditText) findViewById(R.id.etSurNamePacientSignUp);
        etLastNamePacientSignUp = (EditText) findViewById(R.id.etLastNamePacientSignUp);

        ivIDerror = (ImageView) findViewById(R.id.ivIDerror);
        ivNameError = (ImageView) findViewById(R.id.ivNameError);
        ivSurError = (ImageView) findViewById(R.id.ivSurError);
        ivLastError = (ImageView) findViewById(R.id.ivLastError);



        btSavePacientSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passem el contingut dels edittext a Strings per controlar-lo si es correcte
                String ID = etIDPacientSignUp.getText().toString();
                String Nom = etNamePacientSignUp.getText().toString();
                String Cognom = etSurNamePacientSignUp.getText().toString();
                String SegCognom = etLastNamePacientSignUp.getText().toString();

                if(controlFormulariSignUp(ID, Nom, Cognom, SegCognom)){
                    //Guardar a FireBase i passar a 'Tractaments'

                    pacient = new PacientUsuari(ID,Nom,Cognom, SegCognom);
                    showProgressDialog();
                    myRef.push().setValue(pacient).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override

                        public void onComplete(@NonNull Task<Void> task) {
                            Intent PacientUserSUintent = new Intent(PacientUserSignUpActivity.this, EpisodiActivity.class);

                            // Grabar a SharedPreferences user
                            // Col·locar objecte pacient amb llibreria GSON PacientUserSUintent.set

                            SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear();
                            editor.apply();
                            Gson gson = new Gson();
                            String pacient_json = gson.toJson(pacient,PacientUsuari.class);
                            editor.putString("pacient",pacient_json);
                            editor.commit();
                            hideProgressDialog();
                            startActivity(PacientUserSUintent);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressDialog();
                            Toast.makeText(PacientUserSignUpActivity.this, "Error!",
                                    Toast.LENGTH_LONG).show();

                        }
                    });

                }
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
            Toast.makeText(PacientUserSignUpActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(PacientUserSignUpActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(PacientUserSignUpActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(PacientUserSignUpActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}