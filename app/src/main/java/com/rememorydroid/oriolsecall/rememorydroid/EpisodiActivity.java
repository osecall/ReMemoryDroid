package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EpisodiActivity extends BaseActivity {

    private static final String TAG = "EpisodiActivity";
    private ListView lista, listaVersio;
    private Button btNextEpisode;
    private FloatingActionButton fabEpisodi;
    private long i;
    private long j;
    private String Versio;
    private String episodiSeleccionat;
    private EpisodiListAdapter adaptadorPersonalitzat;
    private VersioListAdapter adaptadorVersio;
    private ArrayList<VersioList> versions;
    private ArrayList<EpisodiList> episodis;
    private String ID_pacient;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("pacients");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodi);

        ID_pacient= new String();

        i=0;
        j=1;

        Versio=new String();

        btNextEpisode = (Button) findViewById(R.id.btNextEpisode);

        lista = (ListView) findViewById(R.id.lvEpisodis);
        listaVersio = (ListView) findViewById(R.id.lvVersio);
        fabEpisodi = (FloatingActionButton) findViewById(R.id.fabEpisodi);


        episodis = new ArrayList<EpisodiList>();
        versions = new ArrayList<VersioList>();

        episodiSeleccionat = new String();

        //Recuperem pacient
        PacientUsuari pacient = ObtenirPacient();

        ID_pacient = pacient.getID().toString(); //ID del pacient per recuperar episodis i posar-los a la llista
        //Ara ja tenim l'objecte PacientUsuari

        showProgressDialog();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                        i= dataSnapshot.child(ID_pacient).child("episodis").getChildrenCount()+1;//Guardem el número d'episodis

                        for(DataSnapshot data : dataSnapshot.child(ID_pacient).child("episodis").getChildren()){
                            //while(j<i){
                            EpisodiList episodi=new EpisodiList("","","");
                            //String valor_de_j_string = String.valueOf(j).toString();

                            episodi.setName(data.child("Name").getValue(String.class));
                            episodi.setFecha(data.child("Fecha").getValue(String.class));
                            episodi.setNumero(data.getKey().toString());
                            //episodi.setNumero(String.valueOf(j));
                            episodis.add(episodi);
                            //j++;
                            //}
                        }


                        hideProgressDialog();
                adaptadorPersonalitzat = new EpisodiListAdapter(EpisodiActivity.this,episodis);
                lista.setAdapter(adaptadorPersonalitzat);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Log.e(TAG,databaseError.getMessage().toString());
                showToastError();
            }
        });

        VersioList versioLong = new VersioList(getString(R.string.Monday),getString(R.string.LongVersion));
        VersioList versioShort = new VersioList(getString(R.string.Wednesday),getString(R.string.ShortVersion));

        versions.add(versioLong);
        versions.add(versioShort);

        adaptadorVersio = new VersioListAdapter(this, versions);

        listaVersio.setAdapter(adaptadorVersio);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lista.setItemChecked(i,true);

                showToast(episodis.get(i).getNumero().toString(),false);

                episodiSeleccionat= episodis.get(i).getNumero().toString();

                TextView episodiTmp = (TextView) view.findViewById(R.id.tvLayOutEpisodi);

                showToast(episodiTmp.getText().toString(),false);
           }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                EliminarEpisodi(); //Afegim 1 ja que la llista no comença a 0

                return true;
            }
        });


        listaVersio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==1){
                    Versio="Curta";
                }
                if(i==0){
                    Versio="Llarga";
                }
                listaVersio.setItemChecked(i,true);
                TextView versioSeleccionada = (TextView) view.findViewById(R.id.tvVersio);

                showToast(versioSeleccionada.getText().toString(),false);
            }
        });

        //Agregar passar informació episodi i versio quan s'apreta botó


        btNextEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent TractamentIntent = new Intent(EpisodiActivity.this, TractamentsActivity.class);
                //Passem versió per intent ja que només s'usarà a la pròxima activity una vegada
                //Com que hi ha 3 idiomes passem alguna dada per indica que s'ha escollit la versió llarga a la següent activity

                if(Versio.matches("Llarga") && episodiSeleccionat.isEmpty()){
                    new AlertDialog.Builder(EpisodiActivity.this)
                            .setTitle(getString(R.string.Attention))
                            .setMessage(getString(R.string.SelectionVersionEpisode))
                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }

                            })
                            .show();
                }
                else if(Versio.isEmpty() && !episodiSeleccionat.isEmpty()){
                    new AlertDialog.Builder(EpisodiActivity.this)
                            .setTitle(getString(R.string.Attention))
                            .setMessage(getString(R.string.SelectionVersion))
                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }

                            })
                            .show();
                }
                else if(Versio.isEmpty() && episodiSeleccionat.isEmpty()){
                    new AlertDialog.Builder(EpisodiActivity.this)
                            .setTitle(getString(R.string.Attention))
                            .setMessage(getString(R.string.SelectionVersionEpisode))
                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }

                            })
                            .show();
                }
                else if(Versio.matches("Curta")){
                    TractamentIntent.putExtra("versio","Short");
                    GravarVersio("Short");

                    startActivity(TractamentIntent);
                }
                else if(Versio.matches("Llarga")){
                     TractamentIntent.putExtra("versio","Long");
                     GravarVersio("Long");
                     startActivity(TractamentIntent);
                }
                GravarEpisodi(episodiSeleccionat);

            }
        });

        fabEpisodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDialeg();
            }
        });

    }

    @Override
    public void onBackPressed() {

            //Confirmar eliminació per contrasenya
            //-------------------------------------------------------------------
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(EpisodiActivity.this);
            LayoutInflater factory = LayoutInflater.from(EpisodiActivity.this);
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
                            String email_user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                            String pass_user = input.getText().toString();
                            if (!pass_user.isEmpty()) {
                                //Reautentiquem al avaluador per seguretat
                                AuthCredential credential = EmailAuthProvider.getCredential(email_user, pass_user);
                                FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        EpisodiActivity.super.onBackPressed();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast(getString(R.string.IncorrecPassword), false);
                                    }
                                });
                            }
                        }
                    }).show();
    }


    private void EliminarEpisodi() {
        LayoutInflater factory = LayoutInflater.from(EpisodiActivity.this);
        final View textEntryView = factory.inflate(R.layout.dialeg_deleteepisodi, null);
        final EditText input1 = (EditText) textEntryView.findViewById(R.id.etEpisode);
        final AlertDialog.Builder Borrar = new AlertDialog.Builder(EpisodiActivity.this);
        Borrar
                .setIcon(R.drawable.warningdialogdeleteuser)
                .setTitle(getString(R.string.Attention))
                .setMessage(getString(R.string.EpisodeToDelete))
                .setView(textEntryView)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                            //Confirmar eliminació per contrasenya
                            //-------------------------------------------------------------------
                            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(EpisodiActivity.this);
                            LayoutInflater factory = LayoutInflater.from(EpisodiActivity.this);
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

                                                            if (snapshot.child(ID_pacient).exists()){
                                                                snapshot.child(ID_pacient).child("episodis").child(String.valueOf(input1.getText().toString())).getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        showToast(getString(R.string.EpisodeDeleted,Integer.parseInt(input1.getText().toString())),true);
                                                                        Log.d(TAG, "Usuari pacient borrat");
                                                                        startActivity(getIntent());
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        showToastError();
                                                                        Log.d(TAG, "Usuari pacient no borrat per un error base de dades");
                                                                    }
                                                                });
                                                            }
                                                            else{
                                                                showToast(getString(R.string.EpisodeNotExist),true);
                                                                Log.d(TAG,"L'episodi no existeix!");
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError E) {
                                                            showToastError();
                                                            Log.e(TAG, E.getMessage().toString());
                                                        }
                                                    });
                                                }


                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    showToast(getString(R.string.WrongPassword),true);
                                                    Log.d(TAG,"Password incorrecte");
                                                }
                                            });
                                            arg0.dismiss();

                                        }
                                    })
                                    .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            arg0.dismiss();
                                        }
                                    })
                                    .show();
                    }}).show();
    }


    private void testDialeg(){
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialeg_episodis, null);
        //Instanciem els elements del diàleg per poder obtenir el que ha escrit l'usuari
        final EditText NameEpisode = (EditText) textEntryView.findViewById(R.id.etDialegEpisodeName);
        final EditText FechaEpisode = (EditText) textEntryView.findViewById(R.id.etDialegEpisodeFecha);
        final EditText HoraEpisode = (EditText) textEntryView.findViewById(R.id.etDialegEpisodeHora);

        final AlertDialog.Builder adName =new AlertDialog.Builder(EpisodiActivity.this);
        adName

                .setTitle(getString(R.string.Attention))
                .setView(textEntryView)
                .setMessage(R.string.AddNewEpisode)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Afegim episodi a la base de dades

                        showProgressDialog();
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                        //Analitzem quants episodis hi ha
                                        String numero_episodi= String.valueOf(dataSnapshot.child(ID_pacient).child("episodis").getChildrenCount()+1);

                                        dataSnapshot.child(ID_pacient).child("episodis").child(numero_episodi).child("Name").getRef().setValue(NameEpisode.getText().toString());
                                        dataSnapshot.child(ID_pacient).child("episodis").child(numero_episodi).child("Fecha").getRef().setValue(FechaEpisode.getText().toString());
                                        dataSnapshot.child(ID_pacient).child("episodis").child(numero_episodi).child("Hora").getRef().setValue(HoraEpisode.getText().toString());

                                        showToast(getString(R.string.EpisodeAdded) + " " + numero_episodi,true);
                                        Log.d(TAG,getString(R.string.EpisodeAdded) + " " + numero_episodi);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                showToastError();
                                Log.e(TAG,databaseError.getMessage().toString());
                            }
                        });
                        hideProgressDialog();
                        arg0.cancel();
                        startActivity(getIntent());
                    }})
                .show();

    }


    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuepisodis, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient)+" ("+ID_pacient+")");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.btSignOutMenu) {

            //Confirmar eliminació per contrasenya
            //-------------------------------------------------------------------
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(EpisodiActivity.this);
            LayoutInflater factory = LayoutInflater.from(EpisodiActivity.this);
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
                            String email_user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                            String pass_user = input.getText().toString();
                            if (!pass_user.isEmpty()) {
                                //Reautentiquem al avaluador per seguretat
                                AuthCredential credential = EmailAuthProvider.getCredential(email_user, pass_user);
                                FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Retorna a la pantalla inicial
                                        FirebaseAuth.getInstance().signOut();
                                        showToast(getString(R.string.signed_out), true);
                                        Intent areaAvaluador = new Intent(EpisodiActivity.this, SignInActivity.class);
                                        startActivity(areaAvaluador);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast(getString(R.string.IncorrecPassword), false);

                                    }
                                });
                            }
                        }
                    }).show();
        }

        if (id == R.id.btSignOutPacient) {

            //Confirmar eliminació per contrasenya
            //-------------------------------------------------------------------
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(EpisodiActivity.this);
            LayoutInflater factory = LayoutInflater.from(EpisodiActivity.this);
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
                            String email_user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                            String pass_user = input.getText().toString();
                            if (!pass_user.isEmpty()) {
                                //Reautentiquem al avaluador per seguretat
                                AuthCredential credential = EmailAuthProvider.getCredential(email_user, pass_user);
                                FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Retorna a la pantalla 'Area Avaluador'
                                        BorrarPacient();
                                        showToast(getString(R.string.MenuChangePacient),true);
                                        Intent areaAvaluador = new Intent(EpisodiActivity.this, AreaAvaluadorActivity.class);
                                        startActivity(areaAvaluador);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast(getString(R.string.IncorrecPassword), false);
                                    }
                                });
                            }
                        }
                    }).show();




        }

        if(id==R.id.btAfegirEpisodi){
            testDialeg();

        }

        return super.onOptionsItemSelected(item);
    }
}
