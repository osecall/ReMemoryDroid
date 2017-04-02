package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class AreaAvaluadorActivity extends BaseActivity {

    private TextView emailAvaluador, tvCUid, tvCUname, tvCUsurName, tvtest;
    private EditText IDuserSelected, IduserDelete;
    private Button btSelectUser, btCreateUser, btDeleteUser;
    private String MessageDialogFinal;
    private ImageView ivIDerrorSelect, ivIDerrorDelete;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("pacients");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_avaluador);

        emailAvaluador = (TextView) findViewById(R.id.tvAssessersSessionEmail);
        tvCUid = (TextView) findViewById(R.id.tvCUid);
        tvCUname = (TextView) findViewById(R.id.tvCUname);
        tvCUsurName = (TextView) findViewById(R.id.tvCUlastName);

        ivIDerrorSelect = (ImageView) findViewById(R.id.ivIDerrorSelect);
        ivIDerrorDelete = (ImageView) findViewById(R.id.ivIDerrorDelete);

        IDuserSelected = (EditText) findViewById(R.id.etIDuserSelected);
        IduserDelete = (EditText) findViewById(R.id.etIDuserDelete);


        btSelectUser = (Button) findViewById(R.id.btSelectUser);
        btCreateUser = (Button) findViewById(R.id.btCreateUser);
        btDeleteUser = (Button) findViewById(R.id.btDeleteUser);


        //Col·loquem actual usuari avaluador com a textview
        emailAvaluador.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());


        //Col·loquem usuari pacient actual a la pantalla amb textview si n'hi ha

        //Fer consulta de l'usuari si n'hi ha a preferedsharing
        tvCUid.setText("ID: ");
        tvCUname.setText(R.string.CUName);
        tvCUsurName.setText(R.string.CULastName);

        //Botó sel·leccionar pacient usuari

        btSelectUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            if(CheckSelectUserField()){

                MessageDialogFinal = getString(R.string.UserSelectionDialago,IDuserSelected.getText());

                final String user_selected = IDuserSelected.getText().toString();

                new AlertDialog.Builder(AreaAvaluadorActivity.this)
                        .setTitle(getString(R.string.Attention))
                        .setMessage(MessageDialogFinal)
                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                showProgressDialog();
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        for (DataSnapshot node : snapshot.getChildren()) {

                                            if (node.child("id").getValue().equals(user_selected)) {

                                                //Creem un pacient que s'usarà com a variable per els tractaments per tal de guardar la informació

                                                PacientUsuari pacient = node.getValue(PacientUsuari.class);

                                                    Toast.makeText(AreaAvaluadorActivity.this,user_selected ,
                                                            Toast.LENGTH_LONG).show();

                                                    //Agreguem la informació a la pantalla
                                                tvCUid.setText("ID "+ pacient.getID());
                                                tvCUname.setText(pacient.getName());
                                                tvCUsurName.setText(pacient.getSurName());
                                                tvCUid.setVisibility(View.VISIBLE);
                                                tvCUname.setVisibility(View.VISIBLE);
                                                tvCUsurName.setVisibility(View.VISIBLE);

                                                //Guardem la informació del pacient a la memòria "pacient"

                                                SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = prefs.edit();
                                                editor.clear();
                                                editor.apply();
                                                //Passem objecte pacient a JSON
                                                Gson gson = new Gson();
                                                String pacient_json = gson.toJson(pacient,PacientUsuari.class);
                                                editor.putString("pacient", pacient_json);
                                                editor.commit();

                                                //Anem a la pantalla tractaments
                                                Intent EpisodiIntent = new Intent(AreaAvaluadorActivity.this, EpisodiActivity.class);
                                                startActivity(EpisodiIntent);
                                            }
                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError E) {
                                        hideProgressDialog();
                                        Toast.makeText(AreaAvaluadorActivity.this, "Error database",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                });

                                hideProgressDialog();

                            }
                        })
                        .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.cancel();
                            }
                        })
                        .show();

            }}
        });

        //Botó delete user

        btDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(CheckDeleteUserField()){
                    MessageDialogFinal = getString(R.string.UserDeleteMessage,IduserDelete.getText());

                    final AlertDialog.Builder Dialeg = new AlertDialog.Builder(AreaAvaluadorActivity.this);
                    Dialeg
                            .setIcon(R.drawable.warningdialogdeleteuser)
                            .setTitle(getString(R.string.Attention))
                            .setMessage(MessageDialogFinal)
                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // Some stuff to do when ok got clicked

                                    //Confirmar eliminació per contrasenya
                                    //-------------------------------------------------------------------
                                    final EditText input = new EditText(getBaseContext());
                                    AlertDialog.Builder dialegPassword = new AlertDialog.Builder(AreaAvaluadorActivity.this);
                                    dialegPassword
                                            .setIcon(R.drawable.passwordicon)
                                            .setTitle(R.string.PasswordDialog)
                                            .setMessage(R.string.IntroducePassword)
                                            .setView(input)
                                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    // Recuperem el email del avaluador i el reautentiquem
                                                    String email_user= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                                                    String pass_user = input.getText().toString();
                                                    //Reautentiquem al avaluador per seguretat
                                                    AuthCredential credential = EmailAuthProvider.getCredential(email_user,pass_user);

                                                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {
                                                                    for (DataSnapshot node : snapshot.getChildren()) {

                                                                        if (node.child("id").getValue().equals(IduserDelete.getText().toString())) {
                                                                            node.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    Toast.makeText(AreaAvaluadorActivity.this, R.string.UserDeleted+IduserDelete.getText().toString(),
                                                                                            Toast.LENGTH_LONG).show();

                                                                                    //Eliminem l'usuari de la memòria si és el mateix que està a la sessió
                                                                                    SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                                                                                    String pacient_json = prefs.getString("pacient",null);
                                                                                    Gson temp = new Gson();
                                                                                    PacientUsuari pacient = temp.fromJson(pacient_json, PacientUsuari.class);
                                                                                    if(pacient.getID().equalsIgnoreCase(IduserDelete.getText().toString())){
                                                                                        SharedPreferences.Editor editor = prefs.edit();
                                                                                        editor.remove("pacient");
                                                                                        editor.commit();
                                                                                        editor.clear();
                                                                                        editor.apply();
                                                                                        tvCUid.setVisibility(View.GONE);
                                                                                        tvCUname.setVisibility(View.GONE);
                                                                                        tvCUsurName.setVisibility(View.GONE);

                                                                                    }
                                                                                }
                                                                            });


                                                                        }
                                                                    }


                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError E) {
                                                                    Toast.makeText(AreaAvaluadorActivity.this,"Database Error",
                                                                            Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }


                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(AreaAvaluadorActivity.this,getString(R.string.WrongPassword),
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    });




                                                }
                                            })
                                            .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            })
                                            .show();

                                }
                            })
                            .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            })
                            .show();

                }

            }
        });


        //Botó Crear nou usuari pacient

        btCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUpUserIntent = new Intent(AreaAvaluadorActivity.this, PacientUserSignUpActivity.class);
                startActivity(SignUpUserIntent);
            }
        });


    }



    //Mètode per controlar el camp de delete user
    private boolean CheckDeleteUserField(){
        final AlertDialog.Builder DialegErrorDelete = new AlertDialog.Builder(AreaAvaluadorActivity.this);
        DialegErrorDelete.setIcon(R.drawable.warningdialogdeleteuser).setTitle(getString(R.string.Attention));

        //Treiem la imatge d'exclamació si es torna a premer el botó, es podria posar un listener onfocus a edittext també
        if(ivIDerrorDelete.getVisibility()==View.VISIBLE){
            ivIDerrorDelete.setVisibility(View.INVISIBLE);
        }

        //Diàleg per eliminar usuari pacient

        if(IduserDelete.getText().toString().isEmpty()){
            MessageDialogFinal = getString(R.string.NoNuserIDSelected);
            ivIDerrorDelete.setVisibility(View.VISIBLE);
            DialegErrorDelete.setMessage(MessageDialogFinal);
            DialegErrorDelete.setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //
                }
            });
            DialegErrorDelete.show();
            return false;

        }
        if(!TextUtils.isDigitsOnly(IduserDelete.getText())){
            MessageDialogFinal = getString(R.string.IDonlyDigits);
            ivIDerrorDelete.setVisibility(View.VISIBLE);
            DialegErrorDelete.setMessage(MessageDialogFinal);
            DialegErrorDelete.setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            DialegErrorDelete.show();
            return false;
        }
        return true;
    }

    //Mètode per controlar el camp de select user
    private boolean CheckSelectUserField(){
        final AlertDialog.Builder DialegErrorDelete = new AlertDialog.Builder(AreaAvaluadorActivity.this);
        DialegErrorDelete.setIcon(R.drawable.warningdialogdeleteuser).setTitle(getString(R.string.Attention));

        //Treiem la imatge d'exclamació si es torna a premer el botó, es podria posar un listener onfocus a edittext també
        if(ivIDerrorSelect.getVisibility()==View.VISIBLE){
            ivIDerrorSelect.setVisibility(View.INVISIBLE);
        }

        //Diàleg per eliminar usuari pacient

        if(IDuserSelected.getText().toString().isEmpty()){
            MessageDialogFinal = getString(R.string.NoNuserIDSelected);
            ivIDerrorSelect.setVisibility(View.VISIBLE);
            DialegErrorDelete.setMessage(MessageDialogFinal);
            DialegErrorDelete.setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //
                }
            });
            DialegErrorDelete.show();
            return false;

        }
        if(!TextUtils.isDigitsOnly(IDuserSelected.getText())){
            MessageDialogFinal = getString(R.string.IDonlyDigits);
            ivIDerrorSelect.setVisibility(View.VISIBLE);
            DialegErrorDelete.setMessage(MessageDialogFinal);
            DialegErrorDelete.setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            DialegErrorDelete.show();
            return false;
        }
        return true;
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
            Toast.makeText(AreaAvaluadorActivity.this, R.string.signed_out,
                  Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(AreaAvaluadorActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(AreaAvaluadorActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(AreaAvaluadorActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
