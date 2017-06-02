package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;


public class PacientAnswersActivity extends BaseActivity {

    private static final String TAG = "PacientAnswrsActivity";
    private Button EvocarA, EvocarB, EvocarC, EvocarD, btER, btDR, btAF, btGraellaA, btGraellaB, btGraellaC, btGraellaD;
    private ImageView imatge;
    private String pacientID;
    private MediaPlayer mpA, mpB, mpC, mpD;
    private ProgressBar pbAnswers;
    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefA = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefB = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefC = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefD = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefEnviar = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefDescarregar = FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("pacients");
    private TextView tvTotal0, tvCurrentPunct0, tvCurrentPunct1, tvCurrentPunct2, tvCurrentPunct3;

    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_answers);

        WriteStoragePermissos();

        total = 0;

        EvocarA = (Button) findViewById(R.id.btEvocarA);
        EvocarB = (Button) findViewById(R.id.btEvocarB);
        EvocarC = (Button) findViewById(R.id.btEvocarC);
        EvocarD = (Button) findViewById(R.id.btEvocarD);
        btER = (Button) findViewById(R.id.btER);
        btDR = (Button) findViewById(R.id.btDR);
        btAF = (Button) findViewById(R.id.btAF);
        btGraellaA = (Button) findViewById(R.id.btGraellaA);
        btGraellaB = (Button) findViewById(R.id.btGraellaB);
        btGraellaC = (Button) findViewById(R.id.btGraellaC);
        btGraellaD = (Button) findViewById(R.id.btGraellaD);

        tvCurrentPunct0 = (TextView) findViewById(R.id.tvCurrentPunct0);
        tvCurrentPunct1 = (TextView) findViewById(R.id.tvCurrentPunct1);
        tvCurrentPunct2 = (TextView) findViewById(R.id.tvCurrentPunct2);
        tvCurrentPunct3 = (TextView) findViewById(R.id.tvCurrentPunct3);

        pbAnswers = (ProgressBar) findViewById(R.id.pbAnswers);

        DatabaseReference punctRef = dbRef.child(ObtenirPacient().getID()).child("episodis").child(ObtenirEpisodi().toString());
        punctRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvCurrentPunct0.setText(dataSnapshot.child("Total_graella_evocar_A").getValue(String.class));
                tvCurrentPunct1.setText(dataSnapshot.child("Total_graella_evocar_B").getValue(String.class));
                tvCurrentPunct2.setText(dataSnapshot.child("Total_graella_evocar_C").getValue(String.class));
                tvCurrentPunct3.setText(dataSnapshot.child("Total_graella_evocar_D").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToastError();
            }
        });


        imatge = (ImageView) findViewById(R.id.ivFavorita);

        final PacientUsuari pacient = ObtenirPacient();
        pacientID = pacient.getID();
        final String episodi = ObtenirEpisodi();

        showProgressDialog();
        myRef.child(pacient.getID()).child(episodi).child("favorita").child("favorita.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(PacientAnswersActivity.this).load(uri).into(imatge);
                hideProgressDialog();
            }
        });

        myRefA.child(pacient.getID()).child(episodi).child("sons").child("_EvocarA.3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final Uri pathA = uri;
                EvocarA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpA = MediaPlayer.create(PacientAnswersActivity.this, pathA);
                        pbAnswers.setMax(mpA.getDuration());
                        if (!mpA.isPlaying()) {
                            EvocarB.setEnabled(false);
                            EvocarC.setEnabled(false);
                            EvocarD.setEnabled(false);
                            mpA.start();
                            new Thread(new Runnable() {
                                public void run() {
                                    while (mpA.isPlaying()) {
                                        pbAnswers.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                pbAnswers.setProgress(mpA.getCurrentPosition());
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                        mpA.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                EvocarB.setEnabled(true);
                                EvocarC.setEnabled(true);
                                EvocarD.setEnabled(true);
                                mpA.release();
                                mpA = null;
                            }
                        });


                    }
                });
            }
        });
        myRefB.child(pacient.getID()).child(episodi).child("sons").child("_EvocarB.3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final Uri pathB = uri;
                EvocarB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpB = MediaPlayer.create(PacientAnswersActivity.this, pathB);
                        pbAnswers.setMax(mpB.getDuration());
                        if (!mpB.isPlaying()) {
                            EvocarA.setEnabled(false);
                            EvocarC.setEnabled(false);
                            EvocarD.setEnabled(false);
                            mpB.start();
                            new Thread(new Runnable() {
                                public void run() {
                                    while (mpB.isPlaying()) {
                                        pbAnswers.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                pbAnswers.setProgress(mpB.getCurrentPosition());
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                        mpB.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                EvocarA.setEnabled(true);
                                EvocarC.setEnabled(true);
                                EvocarD.setEnabled(true);
                                mpB.release();
                                mpB = null;
                            }
                        });
                    }
                });
            }
        });
        myRefC.child(pacient.getID()).child(episodi).child("sons").child("_EvocarC.3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final Uri pathC = uri;
                EvocarC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpC = MediaPlayer.create(PacientAnswersActivity.this, pathC);
                        pbAnswers.setMax(mpC.getDuration());
                        if (!mpC.isPlaying()) {
                            EvocarA.setEnabled(false);
                            EvocarB.setEnabled(false);
                            EvocarD.setEnabled(false);
                            mpC.start();
                            new Thread(new Runnable() {
                                public void run() {
                                    while (mpC.isPlaying()) {
                                        pbAnswers.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                pbAnswers.setProgress(mpC.getCurrentPosition());
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                        mpC.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                EvocarA.setEnabled(true);
                                EvocarB.setEnabled(true);
                                EvocarD.setEnabled(true);
                                mpC.release();
                                mpC = null;
                            }
                        });
                    }
                });
            }
        });
        myRefD.child(pacient.getID()).child(episodi).child("sons").child("_EvocarD.3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final Uri pathD = uri;
                EvocarD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpD = MediaPlayer.create(PacientAnswersActivity.this, pathD);
                        pbAnswers.setMax(mpD.getDuration());
                        if (!mpD.isPlaying()) {
                            EvocarA.setEnabled(false);
                            EvocarB.setEnabled(false);
                            EvocarC.setEnabled(false);
                            mpD.start();
                            new Thread(new Runnable() {
                                public void run() {
                                    while (mpD.isPlaying()) {
                                        pbAnswers.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                pbAnswers.setProgress(mpD.getCurrentPosition());
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                        mpD.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                EvocarA.setEnabled(true);
                                EvocarB.setEnabled(true);
                                EvocarC.setEnabled(true);
                                mpD.release();
                                mpD = null;
                            }
                        });
                    }
                });
            }
        });

        btER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Uri urifile = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "ResultatVersioLlarga_" + pacient.getID() + "_episodi_" + episodi + ".csv"));
                myRefDescarregar.child(pacient.getID()).child(episodi.toString()).child("respostes").child("ResultatVersioLlarga_" + pacient.getID() + ".csv").getFile(urifile).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(e.getMessage().toString(), false);
                        Log.e(TAG, "Error al descargar fitxer resultats!");
                    }
                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        enviarEmailCSV(urifile, pacient.getName());
                    }
                });
            }
        });

        btDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri urifile = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "ResultatVersioLlarga_" + pacient.getID() + "_episodi_" + episodi + ".csv"));
                myRefDescarregar.child(pacient.getID()).child(episodi.toString()).child("respostes").child("ResultatVersioLlarga_" + pacient.getID() + ".csv").getFile(urifile).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(e.getMessage().toString(), false);
                        Log.e(TAG, "Error al descargar fitxer resultats!");
                    }
                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        showToast("Descàrga correcte", false);
                    }
                });
            }
        });

        btAF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((Intent.ACTION_GET_CONTENT));
                intent.setType("file/csv");
                startActivity(intent);
            }
        });

        btGraellaA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraellaEvocar("A");
            }
        });
        btGraellaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraellaEvocar("B");

            }
        });
        btGraellaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraellaEvocar("C");

            }
        });
        btGraellaD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraellaEvocar("D");

            }
        });

    }

    private void enviarEmailCSV(Uri pathToFile, String Pacient) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ResultatsCSV_Pacient_" + Pacient);
        emailIntent.putExtra(Intent.EXTRA_STREAM, pathToFile);
        emailIntent.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email amb resultats:"));
        } catch (android.content.ActivityNotFoundException ex) {
            showToast(ex.getMessage().toString(), false);
        }
    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuanswers, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient) + " (" + pacientID + ")");

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
            showToast(getString(R.string.signed_out), true);
            Intent areaAvaluador = new Intent(PacientAnswersActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {
            //Confirmar eliminació per contrasenya
            //-------------------------------------------------------------------
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(PacientAnswersActivity.this);
            LayoutInflater factory = LayoutInflater.from(PacientAnswersActivity.this);
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
                                        showToast(getString(R.string.MenuChangePacient), true);
                                        Intent areaAvaluador = new Intent(PacientAnswersActivity.this, AreaAvaluadorActivity.class);
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

        if (id == R.id.btEpisodis) {
            //Retorna a la pantalla 'Episodis'
            Intent intent = new Intent(PacientAnswersActivity.this, EpisodiActivity.class);
            startActivity(intent);

        }

        if (id == R.id.btTractaments) {
            //Retorna a la pantalla 'Episodis'
            startActivity(new Intent(PacientAnswersActivity.this, TractamentsActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }

    public void onCkeckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.cb01:
                if (checked) {
                    total++;
                    updateTotal();
                } else {
                    total--;
                    updateTotal();
                }
                break;
            case R.id.cb02:
                if (checked) {
                    total = total + 2;
                    updateTotal();
                } else {
                    total = total - 2;
                    updateTotal();
                }
                break;
            case R.id.cb03:
                if (checked) {
                    total = total + 3;
                    updateTotal();
                } else {
                    total = total - 3;
                    updateTotal();
                }
                break;


            case R.id.cb11:
                if (checked) {
                    total++;
                    updateTotal();
                } else {
                    total--;
                    updateTotal();
                }
                break;
            case R.id.cb12:
                if (checked) {
                    total = total + 2;
                    updateTotal();
                } else {
                    total = total - 2;
                    updateTotal();
                }
                break;
            case R.id.cb13:
                if (checked) {
                    total = total + 3;
                    updateTotal();
                } else {
                    total = total - 3;
                    updateTotal();
                }
                break;
            case R.id.cb21:
                if (checked) {
                    total = total + 1;
                    updateTotal();
                } else {
                    total = total - 1;
                    updateTotal();
                }
                break;
            case R.id.cb22:
                if (checked) {
                    total = total + 2;
                    updateTotal();
                } else {
                    total = total - 2;
                    updateTotal();
                }
                break;
            case R.id.cb23:
                if (checked) {
                    total = total + 3;
                    updateTotal();
                } else {
                    total = total - 3;
                    updateTotal();
                }
                break;
            case R.id.cb31:
                if (checked) {
                    total = total + 1;
                    updateTotal();
                } else {
                    total = total - 1;
                    updateTotal();
                }
                break;
            case R.id.cb32:
                if (checked) {
                    total = total + 2;
                    updateTotal();
                } else {
                    total = total - 2;
                    updateTotal();
                }
                break;
            case R.id.cb33:
                if (checked) {
                    total = total + 3;
                    updateTotal();
                } else {
                    total = total - 3;
                    updateTotal();
                }
                break;
            case R.id.cb41:
                if (checked) {
                    total = total + 1;
                    updateTotal();
                } else {
                    total = total - 1;
                    updateTotal();
                }
                break;
            case R.id.cb42:
                if (checked) {
                    total = total + 2;
                    updateTotal();
                } else {
                    total = total - 2;
                    updateTotal();
                }
                break;
            case R.id.cb43:
                if (checked) {
                    total = total + 3;
                    updateTotal();
                } else {
                    total = total - 3;
                    updateTotal();
                }
                break;
        }
    }

    private void updateTotal() {
        if (total > 15) {
            tvTotal0.setTextColor(Color.parseColor("#ffff4444"));
            tvTotal0.setError(getString(R.string.Valor15));
            showToast(getString(R.string.Valor15), false);
        } else {
            tvTotal0.setTextColor(Color.parseColor("#3788e4"));
            tvTotal0.setError(null);
            tvTotal0.setText(String.valueOf(total));
        }
    }


    private void GraellaEvocar(final String graella) {
        total = 0;
        AlertDialog.Builder DialegGraella = new AlertDialog.Builder(PacientAnswersActivity.this);
        LayoutInflater factory = LayoutInflater.from(PacientAnswersActivity.this);
        View viewgraella = factory.inflate(R.layout.dialeggraella, null);
        DialegGraella.setView(viewgraella);
        final Button btOKGrid = (Button) viewgraella.findViewById(R.id.btOKGrid);
        final Button btKO = (Button) viewgraella.findViewById(R.id.btKOGrid);
        tvTotal0 = (TextView) viewgraella.findViewById(R.id.tvTotal0);


        final AlertDialog alertaGraella = DialegGraella.create();
        alertaGraella.show();

        btOKGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total <= 15) {
                    dbRef.child(ObtenirPacient().getID()).child("episodis").child(ObtenirEpisodi().toString()).child("Total_graella_evocar_" + graella).setValue(String.valueOf(total));
                    alertaGraella.dismiss();
                    showToast(getString(R.string.PunctuationUploaded), false);
                } else {
                    showToast(getString(R.string.Valor15), false);
                }
            }
        });

        btKO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertaGraella.dismiss();
            }
        });
    }
}
