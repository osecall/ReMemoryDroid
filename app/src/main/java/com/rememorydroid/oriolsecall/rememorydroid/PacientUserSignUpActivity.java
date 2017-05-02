package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import static android.view.KeyEvent.ACTION_UP;

public class PacientUserSignUpActivity extends BaseActivity{


    private Button btSavePacientSignUp;
    private EditText etIDPacientSignUp, etNamePacientSignUp, etSurNamePacientSignUp, etLastNamePacientSignUp;
    private ImageView ivIDerror, ivNameError, ivSurError, ivLastError;
    private PacientUsuari pacient;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("pacients");
    private static final String TAG = "UserSignUpActivity";



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

        etIDPacientSignUp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==66 && keyEvent.getAction()==ACTION_UP){
                    etNamePacientSignUp.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etNamePacientSignUp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==66 && keyEvent.getAction()==ACTION_UP){
                    etSurNamePacientSignUp.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etSurNamePacientSignUp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==66 && keyEvent.getAction()==ACTION_UP){
                    etLastNamePacientSignUp.requestFocus();
                    return true;
                }
                return false;
            }
        });



        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long numeroMaximUser = dataSnapshot.getChildrenCount()+1;
                etIDPacientSignUp.setText(numeroMaximUser.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToastError();
                Log.e(TAG,"Numero de pacients: "+databaseError.getMessage().toString());
            }
        });



        btSavePacientSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passem el contingut dels edittext a Strings per controlar-lo si es correcte
                final String ID = etIDPacientSignUp.getText().toString();
                final String Nom = etNamePacientSignUp.getText().toString();
                final String Cognom = etSurNamePacientSignUp.getText().toString();
                final String SegCognom = etLastNamePacientSignUp.getText().toString();

                if(controlFormulariSignUp(ID, Nom, Cognom, SegCognom)){
                    //Guardar a FireBase i passar a 'Tractaments'
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //En cas que existeixi ja l'usuari
                            if(dataSnapshot.hasChild(ID)){
                                AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(PacientUserSignUpActivity.this);
                                LayoutInflater factory = LayoutInflater.from(PacientUserSignUpActivity.this);
                                View textEntryView = factory.inflate(R.layout.dialeg_pacient_existent, null);

                                //Instanciem els elements del diàleg per poder obtenir el que ha escrit l'usuari
                                final EditText IDnouDialeg = (EditText) textEntryView.findViewById(R.id.etIDnouDialeg);

                                DialegFormControl
                                        .setTitle(getString(R.string.Attention))
                                        .setView(textEntryView)
                                        .setMessage(R.string.IDalreadyExists)
                                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                pacient = new PacientUsuari(IDnouDialeg.getText().toString(), Nom, Cognom, SegCognom);
                                                if(!IDnouDialeg.getText().toString().equals(ID)) {

                                                    showProgressDialog();
                                                    myRef.child(IDnouDialeg.getText().toString()).setValue(pacient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override

                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            // Grabar a SharedPreferences user

                                                            SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = prefs.edit();
                                                            editor.clear();
                                                            editor.apply();
                                                            Gson gson = new Gson();
                                                            String pacient_json = gson.toJson(pacient, PacientUsuari.class);
                                                            editor.putString("pacient", pacient_json);
                                                            editor.commit();
                                                            hideProgressDialog();
                                                            startActivity(new Intent(PacientUserSignUpActivity.this, EpisodiActivity.class));

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            hideProgressDialog();
                                                            showToastError();
                                                            Log.e(TAG,e.getMessage().toString());

                                                        }

                                                    });
                                                }

                                            }
                                        })
                                        .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .show();

                            }
                            else{
                                showProgressDialog();

                                pacient = new PacientUsuari(ID, Nom, Cognom, SegCognom);

                                myRef.child(ID).setValue(pacient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override

                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Grabar a SharedPreferences user

                                        SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.clear();
                                        editor.apply();
                                        Gson gson = new Gson();
                                        String pacient_json = gson.toJson(pacient, PacientUsuari.class);
                                        editor.putString("pacient", pacient_json);
                                        editor.commit();
                                        hideProgressDialog();
                                        startActivity(new Intent(PacientUserSignUpActivity.this, EpisodiActivity.class));

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        hideProgressDialog();
                                        Log.e(TAG,e.getMessage().toString());
                                        showToastError();
                                    }

                                });
                            }
                         }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG,"Afegir usuari: "+databaseError.getMessage().toString());
                            showToastError();
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

            showToast(getString(R.string.signed_out),true);

            Intent areaAvaluador = new Intent(PacientUserSignUpActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSelectUser) {

            //Retorna a la pantalla 'Area Avaluador'

            showToast(getString(R.string.MenuChangePacient),true);

            Intent areaAvaluador = new Intent(PacientUserSignUpActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }

    //Control formulari per crear usuari pacient


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
                        arg0.dismiss();
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

        if(!Nom.matches("^[a-z ñ A-Z]+$")){
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
        if(!Cognom.matches("^[a-z ñ A-Z]+$")){
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
        if(!SeCognom.matches("^[a-z ñ A-Z]+$")){
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
}