package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class TractamentsActivity extends BaseActivity {

    private TextView idCuUserTreatment, NomCuUserTreatment, CognomCuUserTreatment;
    private Button btPelicula, btAlbum, btGuia;
    private Intent IntentToTreatment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tractaments);

        idCuUserTreatment = (TextView) findViewById(R.id.tvIDTreatment);
        NomCuUserTreatment = (TextView) findViewById(R.id.tvNameTreatment);
        CognomCuUserTreatment = (TextView) findViewById(R.id.tvSurTreatment);

        btPelicula= (Button) findViewById(R.id.btPelicula);
        btAlbum = (Button) findViewById(R.id.btAlbum);
        btGuia = (Button) findViewById(R.id.btGuia);

        //Llegim informació de l'usuari

        PacientUsuari pacient = ObtenirPacient();


        idCuUserTreatment.setText(pacient.getID());
        NomCuUserTreatment.setText(pacient.getName());
        CognomCuUserTreatment.setText(pacient.getSurName());

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

    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menutractaments, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient)+" ("+idCuUserTreatment.getText().toString()+")");
        menu.getItem(3).setTitle(getString(R.string.Answers) + " (" + idCuUserTreatment.getText().toString() + ")");

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
            Intent areaAvaluador = new Intent(TractamentsActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {
            //Retorna a la pantalla 'Area Avaluador'
            showToast(getString(R.string.MenuChangePacient),true);
            Intent areaAvaluador = new Intent(TractamentsActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btEpisodis) {
            //Retorna a la pantalla 'Episodis'
            Intent intent = new Intent(TractamentsActivity.this, EpisodiActivity.class);
            startActivity(intent);

        }

        if (id == R.id.btAnswers) {
            //Retorna a la pantalla 'Episodis'
            startActivity(new Intent(TractamentsActivity.this, PacientAnswersActivity.class));
        }

        return super.onOptionsItemSelected(item);
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
