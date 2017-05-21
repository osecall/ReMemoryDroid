package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class TractamentsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int result = 1;
    static final int result2 = 2;
    private TextView idCuUserTreatment, NomCuUserTreatment, CognomCuUserTreatment;
    private Button btPelicula, btAlbum, btGuia;
    private Intent IntentToTreatment;
    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private String TAG = "TractamentsAtivity";
    private int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tractaments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //-----------------------
        idCuUserTreatment = (TextView) findViewById(R.id.tvIDTreatment);
        NomCuUserTreatment = (TextView) findViewById(R.id.tvNameTreatment);
        CognomCuUserTreatment = (TextView) findViewById(R.id.tvSurTreatment);


        btPelicula= (Button) findViewById(R.id.btPelicula);
        btAlbum = (Button) findViewById(R.id.btAlbum);
        btGuia = (Button) findViewById(R.id.btGuia);

        //Llegim informació de l'usuari

        PacientUsuari pacient = ObtenirPacient();

        idCuUserTreatment.setText("ID: (" + pacient.getID() + ")");
        NomCuUserTreatment.setText(pacient.getName());
        CognomCuUserTreatment.setText(pacient.getSurName());

        navigationView.getMenu().findItem(R.id.signOutAssessor).setTitle(getString(R.string.sign_out_menu, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        navigationView.getMenu().findItem(R.id.signoutpacient).setTitle(getString(R.string.sign_out_Pacient) + " (" + pacient.getID() + ")");
        navigationView.getMenu().findItem(R.id.answers).setTitle(getString(R.string.PacientAnswers) + " (" + pacient.getID() + ")");

        //Deshabilitar alguns botons si s'ha escollit versió llarga

        if(ObtenirVersio().matches("Long")){
            btAlbum.setEnabled(false);
            btGuia.setEnabled(false);
            btPelicula.setEnabled(true);

        }
        else if(ObtenirVersio().matches("Short")){
            btPelicula.setEnabled(true);
            btAlbum.setEnabled(true);
            btGuia.setEnabled(true);

            if(ObtenirEpisodi().isEmpty()){
                btPelicula.setEnabled(false);
            }

        }
        if(getIntent().hasExtra("final")){
            dialegEnviarEmail(getIntent().getStringExtra("file"));
        }


        btGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentToTreatment = new Intent(TractamentsActivity.this, GuiaActivity.class);
                startActivity(IntentToTreatment);
            }
        });

        btPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentToTreatment = new Intent(TractamentsActivity.this, EpisodePresentationActivity.class);
                startActivity(IntentToTreatment);


            }
        });
        //------------------------------------
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.signOutAssessor) {
            //Retorna a la pantalla inicial
            FirebaseAuth.getInstance().signOut();
            showToast(getString(R.string.signed_out),true);
            Intent areaAvaluador = new Intent(TractamentsActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        } else if (id == R.id.signoutpacient) {
            showToast(getString(R.string.MenuChangePacient),true);
            Intent areaAvaluador = new Intent(TractamentsActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        } else if (id == R.id.backtoepisodes) {
            startActivity(new Intent(TractamentsActivity.this, EpisodiActivity.class));
        } else if (id == R.id.answers) {
            startActivity(new Intent(TractamentsActivity.this, PacientAnswersActivity.class));
        } else if (id == R.id.videos) {
            Intent pickVideo = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            pickVideo.setType("video/*");
            startActivityForResult(Intent.createChooser(pickVideo, getString(R.string.ChooseSource)), result);
        } else if (id == R.id.pictures) {
            DialegPicture();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == result) {

                StorageReference videoRef = myRef.child(ObtenirPacient().getID()).child(ObtenirEpisodi().toString()).child("video").child("video.mp4");
                showProgressDialog();
                UploadTask uploadTask = videoRef.putFile(data.getData());

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        showToastError();
                        Log.e(TAG, exception.toString());
                        hideProgressDialog();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        showToast(getString(R.string.VideoUploadCorrect), true);
                        hideProgressDialog();

                    }
                });

            }
            if (requestCode == result2) {

                StorageReference videoRef = myRef.child(ObtenirPacient().getID()).child(ObtenirEpisodi().toString()).child("imatges").child("imatge" + (i)
                        + ".jpeg");
                showProgressDialog();
                UploadTask uploadTask = videoRef.putFile(data.getData());

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        showToastError();
                        Log.e(TAG, exception.toString());
                        hideProgressDialog();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        showToast(getString(R.string.ImagesUpload), true);
                        hideProgressDialog();

                    }
                });

            }
        }
    }



    private void enviarEmailCSV(String pathToFile,String[] emailTo, String[] emailCC, String Pacient){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTo);
        emailIntent.putExtra(Intent.EXTRA_CC, emailCC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.SubjectEmail) + Pacient);
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.Bodyemal));
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(pathToFile)));
        emailIntent.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.SendEmailResults)));
        } catch (android.content.ActivityNotFoundException ex) {
            showToast(ex.getMessage().toString(),false);
        }
    }

    private void dialegEnviarEmail(final String pathFile){
        AlertDialog.Builder DialegDespedida = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialegemailnotificacio, null);
        TextView missatge = (TextView) textEntryView.findViewById(R.id.tvDialeg);
        final EditText etTo = (EditText) textEntryView.findViewById(R.id.etTo);
        final EditText etCC = (EditText) textEntryView.findViewById(R.id.etCC);
        missatge.setText(getString(R.string.EnviarEmail));
        etTo.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
        etCC.setText("lifelogging.narrative@gmail.com"); //Aquesta és fixe
        Button btEnviar = (Button) textEntryView.findViewById(R.id.btEnviar);
        Button btNoEnviar = (Button) textEntryView.findViewById(R.id.btNoEnviar);

        DialegDespedida
                .setCancelable(false)
                .setView(textEntryView);

        final AlertDialog alerta = DialegDespedida.create();

        alerta.show();

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] emailTo = {etTo.getText().toString()};
                String[] emailCC = {etCC.getText().toString()};
                enviarEmailCSV(pathFile,emailTo,emailCC,NomCuUserTreatment.getText().toString());
                alerta.dismiss();
            }
        });

        btNoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });


    }

    private void DialegPicture() {
        AlertDialog.Builder Dialegpictures = new AlertDialog.Builder(TractamentsActivity.this);
        LayoutInflater factory = LayoutInflater.from(TractamentsActivity.this);
        View textEntryView = factory.inflate(R.layout.dialegpictures, null);

        final ImageView ivPic1 = (ImageView) textEntryView.findViewById(R.id.ivPic1);
        final ImageView ivPic2 = (ImageView) textEntryView.findViewById(R.id.ivPic2);
        final ImageView ivPic3 = (ImageView) textEntryView.findViewById(R.id.ivPic3);
        final ImageView ivPic4 = (ImageView) textEntryView.findViewById(R.id.ivPic4);
        final ImageView ivPic5 = (ImageView) textEntryView.findViewById(R.id.ivPic5);
        final ImageView ivPic6 = (ImageView) textEntryView.findViewById(R.id.ivPic6);

        StorageReference Ref0 = myRef.child(ObtenirPacient().getID()).child(ObtenirEpisodi()).child("imatges").child("0.jpg");
        StorageReference Ref1 = myRef.child(ObtenirPacient().getID()).child(ObtenirEpisodi()).child("imatges").child("1.jpg");
        StorageReference Ref2 = myRef.child(ObtenirPacient().getID()).child(ObtenirEpisodi()).child("imatges").child("2.jpg");
        StorageReference Ref3 = myRef.child(ObtenirPacient().getID()).child(ObtenirEpisodi()).child("imatges").child("3.jpg");
        StorageReference Ref4 = myRef.child(ObtenirPacient().getID()).child(ObtenirEpisodi()).child("imatges").child("4.jpg");
        StorageReference Ref5 = myRef.child(ObtenirPacient().getID()).child(ObtenirEpisodi()).child("imatges").child("5.jpg");

        Ref0.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivPic1.setVisibility(View.VISIBLE);
                Picasso.with(TractamentsActivity.this).load(uri).into(ivPic1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToastError();
            }
        });
        Ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivPic2.setVisibility(View.VISIBLE);
                Picasso.with(TractamentsActivity.this).load(uri).into(ivPic2);
            }
        });
        Ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivPic3.setVisibility(View.VISIBLE);
                Picasso.with(TractamentsActivity.this).load(uri).into(ivPic3);
            }
        });
        Ref3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivPic4.setVisibility(View.VISIBLE);
                Picasso.with(TractamentsActivity.this).load(uri).into(ivPic4);
            }
        });
        Ref4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivPic5.setVisibility(View.VISIBLE);
                Picasso.with(TractamentsActivity.this).load(uri).into(ivPic5);
            }
        });
        Ref5.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivPic6.setVisibility(View.VISIBLE);
                Picasso.with(TractamentsActivity.this).load(uri).into(ivPic6);

            }
        });

        Dialegpictures.setView(textEntryView);

        final AlertDialog alerta = Dialegpictures.create();

        alerta.show();

        ivPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;
                alerta.dismiss();
                SeleccionarFotoGaleria();
            }
        });
        ivPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1;
                alerta.dismiss();
                SeleccionarFotoGaleria();
            }
        });
        ivPic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 2;
                alerta.dismiss();
                SeleccionarFotoGaleria();
            }
        });
        ivPic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 3;
                alerta.dismiss();
                SeleccionarFotoGaleria();
            }
        });
        ivPic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 4;
                alerta.dismiss();
                SeleccionarFotoGaleria();
            }
        });
        ivPic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 5;
                alerta.dismiss();
                SeleccionarFotoGaleria();
            }
        });
    }

    private void SeleccionarFotoGaleria() {
        Intent pickVideo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickVideo.setType("image/*");
        startActivityForResult(Intent.createChooser(pickVideo, getString(R.string.ChooseSource)), result2);
    }


}
