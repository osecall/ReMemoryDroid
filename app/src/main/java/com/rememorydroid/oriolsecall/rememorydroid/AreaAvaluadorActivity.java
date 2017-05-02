package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;

public class AreaAvaluadorActivity extends BaseActivity {

    private static final String TAG = "AreaAvaluador";
    private TextView emailAvaluador, tvCUid, tvCUname, tvCUsurName, tvtest;
    private FloatingActionButton fabSignUp;
    private String MessageDialogFinal, IDuserDelete;
    private PacientUsuari pacient;
    private ListView lvPacients;
    private PacientListAdapter pacientListAdapter;
    private ArrayList<PacientUsuari> PacientList = new ArrayList<PacientUsuari>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("pacients");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_avaluador);

        pacient=null;

        emailAvaluador = (TextView) findViewById(R.id.tvAssessersSessionEmail);
        tvCUid = (TextView) findViewById(R.id.tvCUid);
        tvCUname = (TextView) findViewById(R.id.tvCUname);
        tvCUsurName = (TextView) findViewById(R.id.tvCUlastName);

        fabSignUp = (FloatingActionButton) findViewById(R.id.fabSignUp);

        //Col·loquem actual usuari avaluador com a textview
        emailAvaluador.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());

        //Col·loquem usuari pacient actual a la pantalla amb textview si n'hi ha

        //Fer consulta de l'usuari si n'hi ha a preferedsharing
        tvCUid.setText("ID: ");
        tvCUname.setText(R.string.NonSelected);
        tvCUsurName.setText(R.string.CULastName);

        lvPacients = (ListView) findViewById(R.id.lvPacients);

        showProgressDialog();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    PacientUsuari pacientUser = new PacientUsuari();

                    pacientUser.setID(String.valueOf(i++));
                    pacientUser.setName(data.child("name").getValue(String.class));
                    pacientUser.setLastName(data.child("lastName").getValue(String.class));
                    pacientUser.setSurName(data.child("surName").getValue(String.class));

                    PacientList.add(pacientUser);
                }
                pacientListAdapter = new PacientListAdapter(AreaAvaluadorActivity.this,PacientList);
                lvPacients.setAdapter(pacientListAdapter);
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast("Error!",false);
                Log.e(TAG,databaseError.getMessage().toString());
            }
        });

        lvPacients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                Snackbar.make(view, getString(R.string.Attention), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.PressToDeleteUser), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LayoutInflater factory = LayoutInflater.from(AreaAvaluadorActivity.this);
                                final View textEntryView = factory.inflate(R.layout.dialeg_deleteid, null);
                                final EditText input1 = (EditText) textEntryView.findViewById(R.id.etiddelteUser);
                                final AlertDialog.Builder Borrar = new AlertDialog.Builder(AreaAvaluadorActivity.this);
                                Borrar
                                        .setIcon(R.drawable.warningdialogdeleteuser)
                                        .setTitle(getString(R.string.Attention))
                                        .setMessage(getString(R.string.DeletePacient)+" "+String.valueOf(i)+"\n"+getString(R.string.PacientIDSecurity))
                                        .setView(textEntryView)
                                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                IDuserDelete=input1.getText().toString();
                                                MessageDialogFinal = getString(R.string.UserDeleteMessage,IDuserDelete);

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
                                                                AlertDialog.Builder dialegPassword = new AlertDialog.Builder(AreaAvaluadorActivity.this);
                                                                LayoutInflater factory = LayoutInflater.from(AreaAvaluadorActivity.this);
                                                                View textEntryView = factory.inflate(R.layout.dialeg_delete_user, null);
                                                                //Instanciem els elements del diàleg per poder obtenir el que ha escrit l'usuari
                                                                final EditText input = (EditText) textEntryView.findViewById(R.id.etPasswordDelete);
                                                                dialegPassword
                                                                        .setView(textEntryView)
                                                                        .setIcon(R.drawable.passwordicon)
                                                                        .setTitle(R.string.PasswordDialog)
                                                                        .setMessage(R.string.IntroducePassword)
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

                                                                                                if (snapshot.child(IDuserDelete).exists()){
                                                                                                    snapshot.child(IDuserDelete).getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                            showToast(getString(R.string.UserDeleted)+IDuserDelete,true);

                                                                                                            //Eliminem l'usuari de la memòria si és el mateix que està a la sessió
                                                                                                            SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                                                                                                            String pacient_json = prefs.getString("pacient",null);
                                                                                                            Gson temp = new Gson();
                                                                                                            PacientUsuari pacient = temp.fromJson(pacient_json, PacientUsuari.class);
                                                                                                            if(pacient.getID().equalsIgnoreCase(IDuserDelete)){
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
                                                                                                else{
                                                                                                    showToast(getString(R.string.UserDoesNotExist),true);
                                                                                                    Log.d(TAG, "Usuari per eliminar no existeix");
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError E) {
                                                                                                showToastError();
                                                                                                Log.e(TAG,"AreaAvaluador: "+E.getMessage().toString());

                                                                                            }
                                                                                        });
                                                                                    }


                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        showToast(getString(R.string.WrongPassword),true);
                                                                                        Log.e(TAG,"AreaAvaluador: "+e.getStackTrace().toString());
                                                                                    }
                                                                                });
                                                                                arg0.cancel();
                                                                                arg0.dismiss();
                                                                            }
                                                                        })
                                                                        .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                                                arg0.dismiss();
                                                                                arg0.cancel();
                                                                            }
                                                                        })
                                                                        .show();

                                                            }
                                                        })
                                                        .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                                arg0.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        })
                                        .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .show();
                            }
                        }).setActionTextColor(getResources().getColor(R.color.red))
                        .show();
                return true;
            }
        });



        lvPacients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                pacient = new PacientUsuari();
                lvPacients.setItemChecked(i,true);
                TextView NomPacient = (TextView) view.findViewById(R.id.tvNomPacient);
                TextView Cognom = (TextView) view.findViewById(R.id.tvCognomPacient);
                TextView SegCognom = (TextView) view.findViewById(R.id.tvCognom2Pacient);

                tvCUid.setText("ID: "+String.valueOf(i));
                tvCUname.setText(getString(R.string.CUName,NomPacient.getText().toString()));
                tvCUsurName.setText(getString(R.string.CULastName,Cognom.getText().toString()));

                tvCUid.setVisibility(View.VISIBLE);
                tvCUname.setVisibility(View.VISIBLE);
                tvCUsurName.setVisibility(View.VISIBLE);

                pacient.setID(String.valueOf(i));
                pacient.setName(NomPacient.getText().toString());
                pacient.setSurName(Cognom.getText().toString());
                pacient.setLastName(SegCognom.getText().toString());

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
                pacient=null;

                //Anem a la pantalla tractaments
                Intent EpisodiIntent = new Intent(AreaAvaluadorActivity.this, EpisodiActivity.class);
                startActivity(EpisodiIntent);

                showToast(NomPacient.getText().toString(),true);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        //Botó Crear nou usuari pacient

        fabSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.PressMoreThanOne, Snackbar.LENGTH_LONG)
                        .setAction(R.string.Attention, null).show();

            }
        });

        fabSignUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent SignUpUserIntent = new Intent(AreaAvaluadorActivity.this, PacientUserSignUpActivity.class);
                startActivity(SignUpUserIntent);
                return true;
            }
        });

    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuavaluadors, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out,FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
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
            Log.d(TAG,"Usuari ha acabat la sessió");

            Intent areaAvaluador = new Intent(AreaAvaluadorActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);
        }

        if (id == R.id.btSelectUser) {

            LayoutInflater factory = LayoutInflater.from(AreaAvaluadorActivity.this);
            View textEntryView = factory.inflate(R.layout.dialeg_deleteid, null);
            final EditText user_selected = (EditText) textEntryView.findViewById(R.id.etiddelteUser);

                    new AlertDialog.Builder(AreaAvaluadorActivity.this)
                            .setTitle(getString(R.string.Attention))
                            .setView(textEntryView)
                            .setMessage(MessageDialogFinal)
                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    showProgressDialog();
                                    MessageDialogFinal = getString(R.string.UserSelectionDialago,user_selected.getText().toString());

                                    myRef.addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if(snapshot.hasChild(user_selected.getText().toString())){
                                                //Creem un pacient que s'usarà com a variable per els tractaments per tal de guardar la informació
                                                pacient = snapshot.child(user_selected.getText().toString()).getValue(PacientUsuari.class);
                                                pacient.setID(snapshot.child(user_selected.getText().toString()).getKey());

                                                showToast(user_selected.getText().toString(),true);

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
                                                pacient=null;

                                                //Anem a la pantalla tractaments
                                                Intent EpisodiIntent = new Intent(AreaAvaluadorActivity.this, EpisodiActivity.class);
                                                startActivity(EpisodiIntent);
                                            }
                                            else{
                                                showToast(getString(R.string.UserDoesNotExist) ,true);
                                                Log.d(TAG,"Usuari no existeix a la base de dades");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError E) {
                                            hideProgressDialog();
                                            showToastError();
                                            Log.e(TAG,E.getMessage().toString());

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

                    }
        return super.onOptionsItemSelected(item);
    }
}
