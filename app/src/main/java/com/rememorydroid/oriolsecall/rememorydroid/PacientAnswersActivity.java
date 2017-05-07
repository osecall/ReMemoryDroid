package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;


public class PacientAnswersActivity extends BaseActivity {

    private static final String TAG = "PacientAnswrsActivity";
    private Button EvocarA, EvocarB, EvocarC, EvocarD, btER, btDR;
    private ImageView imatge;
    private String pacientID;
    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefA = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefB = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefC = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefD = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefEnviar = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefDescarregar = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_answers);

        EvocarA = (Button) findViewById(R.id.btEvocarA);
        EvocarB = (Button) findViewById(R.id.btEvocarB);
        EvocarC = (Button) findViewById(R.id.btEvocarC);
        EvocarD = (Button) findViewById(R.id.btEvocarD);
        btER = (Button) findViewById(R.id.btER);
        btDR = (Button) findViewById(R.id.btDR);

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
                        final MediaPlayer mp = MediaPlayer.create(PacientAnswersActivity.this, pathA);
                        if (!mp.isPlaying()) mp.start();
                        else mp.pause();
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
                        final MediaPlayer mp = MediaPlayer.create(PacientAnswersActivity.this, pathB);
                        if (!mp.isPlaying()) mp.start();
                        else mp.pause();
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
                        final MediaPlayer mp = MediaPlayer.create(PacientAnswersActivity.this, pathC);
                        if (!mp.isPlaying()) mp.start();
                        else mp.pause();
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
                        final MediaPlayer mp = MediaPlayer.create(PacientAnswersActivity.this, pathD);
                        if (!mp.isPlaying()) mp.start();
                        else mp.pause();
                    }
                });
            }
        });

        btER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRefEnviar.child(pacient.getID()).child(episodi).child("respostes").child("ResultatVersioLlarga_" + pacient.getID() + ".csv").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final Uri fitxer = uri;
                        enviarEmailCSV(fitxer, pacient.getName());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToastError();
                        Log.e(TAG, "Error al enviar fitxer respostes desde FireBase");
                    }
                });
            }
        });

        btDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri urifile = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "ResultatVersioLlarga_" + pacient.getID() + "_episodi_" + episodi + ".csv"));
                myRefDescarregar.child(pacient.getID()).child(episodi).child("respostes").child("ResultatVersioLlarga_" + pacient.getID() + ".csv").getFile(urifile).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(e.getMessage().toString(), false);
                        Log.e(TAG, "Error al descargar fitxer resultats!");
                    }
                }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        showToast("Descàrga correcte", false);
                    }
                });
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
            //Retorna a la pantalla 'Area Avaluador'
            showToast(getString(R.string.MenuChangePacient), true);
            Intent areaAvaluador = new Intent(PacientAnswersActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

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
}
