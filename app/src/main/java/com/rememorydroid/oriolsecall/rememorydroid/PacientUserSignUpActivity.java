package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.KeyEvent.ACTION_UP;

public class PacientUserSignUpActivity extends BaseActivity{


    private static final String TAG = "UserSignUpActivity";
    private Button btSavePacientSignUp;
    private EditText etIDPacientSignUp, etNamePacientSignUp, etSurNamePacientSignUp, etLastNamePacientSignUp;
    private PacientUsuari pacient;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("pacients");

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
                String aux = "0";
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (Integer.parseInt(aux) - Integer.parseInt(data.getKey()) < -1) {
                        etIDPacientSignUp.setText(String.valueOf(Integer.parseInt(data.getKey()) - 1));
                        break;
                    }
                    aux = data.getKey();

                }
                /*
                Long numeroMaximUser = dataSnapshot.getChildrenCount()+1;
                etIDPacientSignUp.setText(numeroMaximUser.toString());*/
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
                                                            BorrarPacient();
                                                            GravarPacient(pacient);
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
                                        BorrarPacient();
                                        GravarPacient(pacient);
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
            BorrarPacient();
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
            etIDPacientSignUp.setError(getString(R.string.IDFieldEmpty));
            return false;

        }
        if(!android.text.TextUtils.isDigitsOnly(ID)){
            DialegFormControl.setMessage(R.string.IDonlyDigits);
            DialegFormControl.show();
            etIDPacientSignUp.setError(getString(R.string.IDonlyDigits));
            return false;
        }
        if(ID.length()>4){
            DialegFormControl.setMessage(R.string.IDlength4);
            DialegFormControl.show();
            etIDPacientSignUp.setError(getString(R.string.IDlength4));
            return false;

        }
        if(ID.length()<1){
            DialegFormControl.setMessage(R.string.IDlength1);
            DialegFormControl.show();
            etIDPacientSignUp.setError(getString(R.string.IDlength1));
            return false;

        }
        //Nom
        if(Nom.isEmpty()){
            DialegFormControl.setMessage(R.string.NameFieldEmpty);
            DialegFormControl.show();
            etNamePacientSignUp.setError(getString(R.string.NameFieldEmpty));
            return false;
        }
        if(Nom.length()>30 || Nom.length()<2){
            DialegFormControl.setMessage(R.string.NameLength);
            DialegFormControl.show();
            etNamePacientSignUp.setError(getString(R.string.NameLength));
            return false;
        }

        if(!Nom.matches("^[a-z ñ A-Z]+$")){
            DialegFormControl.setMessage(R.string.NameNotDigits);
            DialegFormControl.show();
            etNamePacientSignUp.setError(getString(R.string.NameNotDigits));
            return false;
        }
        if(Cognom.isEmpty()){
            DialegFormControl.setMessage(R.string.SurnameEmpty);
            DialegFormControl.show();
            etSurNamePacientSignUp.setError(getString(R.string.SurnameEmpty));
            return false;
        }
        if(!Cognom.matches("^[a-z ñ A-Z]+$")){
            DialegFormControl.setMessage(R.string.SurNameNotDigits);
            DialegFormControl.show();
            etSurNamePacientSignUp.setError(getString(R.string.SurNameNotDigits));
            return false;
        }
        if(Cognom.length()>30 || Cognom.length()<2){
            DialegFormControl.setMessage(R.string.SurnameLength);
            DialegFormControl.show();
            etSurNamePacientSignUp.setError(getString(R.string.SurnameLength));
            return false;
        }
        if(SeCognom.isEmpty()) {
            DialegFormControl.setMessage(R.string.LastNameEmpty);
            DialegFormControl.show();
            etLastNamePacientSignUp.setError(getString(R.string.LastNameEmpty));
            return false;
        }
        if(!SeCognom.matches("^[a-z ñ A-Z]+$")){
            DialegFormControl.setMessage(R.string.LastNameNotDigits);
            DialegFormControl.show();
            etLastNamePacientSignUp.setError(getString(R.string.LastNameNotDigits));
            return false;
        }
        if(SeCognom.length()>30 || SeCognom.length()<2){
            DialegFormControl.setMessage(R.string.LastNameLength);
            DialegFormControl.show();
            etLastNamePacientSignUp.setError(getString(R.string.LastNameLength));
            return false;
        }
        return true;
    }
}