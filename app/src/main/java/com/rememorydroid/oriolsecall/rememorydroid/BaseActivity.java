package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by Oriol on 24/02/2017.
 */

public class BaseActivity extends AppCompatActivity {


    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2 ;
    public static final int MY_PERMISSIONS_REQUEST_INTERNET = 3 ;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 4 ;
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    public Gson gson;
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



    public void showToast(String message, boolean llarga){

        int duracio;

        if(llarga){
            duracio = Toast.LENGTH_LONG;
        }
        else duracio = Toast.LENGTH_SHORT;

        Toast.makeText(this, message,
                duracio).show();
    }

    public void showToastError(){
        Toast.makeText(this, "Error!",
                Toast.LENGTH_LONG).show();
    }


    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }


    //----------------Part SharedPreferences


    //Retorna respostes guardades
    public TestAnswers ObtenirRespostesActuals(){
        gson = new Gson();
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        return gson.fromJson(prefs.getString("respostes",null),TestAnswers.class);
    }

    //Guardar respostes
    public void GravarRespoestesActuals(TestAnswers respostes){
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        gson = new Gson();
        editor= prefs.edit();
        editor.putString("respostes",gson.toJson(respostes, TestAnswers.class));
        editor.commit();
    }

    //Retorna respostes guardades
    public PacientUsuari ObtenirPacient(){
        gson = new Gson();
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        return gson.fromJson(prefs.getString("pacient",null),PacientUsuari.class);
    }

    //Retorna si hi ha pacient guardat
    public String ObtenirPacientBoolean() {
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        return prefs.getString("pacient", null);
    }


    //Guardar respostes
    public void GravarPacient(PacientUsuari pacient){
        gson = new Gson();
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor= prefs.edit();
        editor.putString("pacient",gson.toJson(pacient, PacientUsuari.class));
        editor.commit();
    }

    //Guardar respostes
    public void BorrarPacient() {
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.remove("pacient").commit();
    }

    //Obtenir episodi
    public String ObtenirEpisodi(){
        return getSharedPreferences("pacient", Context.MODE_PRIVATE).getString("episodi",null);
    }

    //Guardar respostes
    public void GravarEpisodi(String episodi){
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor= prefs.edit();
        editor.putString("episodi",episodi);
        editor.commit();
    }

    //Obtenir versió
    public String ObtenirVersio(){
        return getSharedPreferences("pacient", Context.MODE_PRIVATE).getString("Versio",null);
    }

    //Guardar versió
    public void GravarVersio(String versio){
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor= prefs.edit();
        editor.putString("Versio",versio);
        editor.commit();
    }


    //Guardar llenguatge
    public void GuardarLlenguatge(String language) {
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString("Language", language).commit();
    }

    //Obtenir llenguatge
    public String ObtenirLlenguatge() {
        return getSharedPreferences("pacient", Context.MODE_PRIVATE).getString("Language", null);
    }

    //Netejar llengua preferida
    public void BorrarSharedPreferencesLlengua() {
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.remove("Language").commit();
    }

    // Permisos


    public void InternetPermissos(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);

            }
        }
    }

    public void WriteStoragePermissos(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
    }

    public void ReadStoragePermissos(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        }
    }


    public void AudioRecordPermissos(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.RECORD_AUDIO)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            } case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;

            } case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            } case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }
}




