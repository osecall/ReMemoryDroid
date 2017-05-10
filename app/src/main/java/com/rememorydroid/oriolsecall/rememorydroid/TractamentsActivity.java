package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class TractamentsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView idCuUserTreatment, NomCuUserTreatment, CognomCuUserTreatment;
    private Button btPelicula, btAlbum, btGuia;
    private Intent IntentToTreatment;

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void enviarEmailCSV(String pathToFile,String[] emailTo, String[] emailCC, String Pacient){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTo);
        emailIntent.putExtra(Intent.EXTRA_CC, emailCC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ResultatsCSV_Pacient_"+Pacient);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "En aquest correu et pots descarregar el fitxer CSV. Gràcies.");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(pathToFile)));
        emailIntent.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email amb resultats:"));
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
}
