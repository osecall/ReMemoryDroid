package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AreaAvaluadorActivity extends AppCompatActivity {

    private TextView emailAvaluador, tvCUid, tvCUname, tvCUlastName;
    private EditText IDuserSelected, IduserDelete;
    private Button btSelectUser, btCreateUser, btDeleteUser;
    private String MessageDialogFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_avaluador);


        emailAvaluador = (TextView) findViewById(R.id.tvAssessersSessionEmail);
        tvCUid = (TextView) findViewById(R.id.tvCUid);
        tvCUname = (TextView) findViewById(R.id.tvCUname);
        tvCUlastName = (TextView) findViewById(R.id.tvCUlastName);


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
        tvCUlastName.setText(R.string.CULastName);

        //Botó sel·leccionar pacient usuari

        btSelectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Diàleg per sel·leccionar usuari pacient

                if(IDuserSelected.getText().toString().matches("")){
                    MessageDialogFinal = getString(R.string.NoNuserIDSelected);
                }
                else{
                    MessageDialogFinal = getString(R.string.UserSelectionDialago,IDuserSelected.getText());

                }

                new AlertDialog.Builder(AreaAvaluadorActivity.this)
                        .setTitle(getString(R.string.Attention))
                        .setMessage(MessageDialogFinal)
                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Some stuff to do when ok got clicked

                                //Fer sel·lecció usuari a FireBase i anar a la pantalla 'Tractaments'



                            }
                        })
                        .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Some stuff to do when cancel got clicked

                                //Fer res
                            }
                        })
                        .show();



            }
        });




        //Botó delete user

        btDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Diàleg per eliminar usuari pacient

                if(IduserDelete.getText().toString().matches("")){
                    MessageDialogFinal = getString(R.string.NoNuserIDSelected);
                }
                else{
                    MessageDialogFinal = getString(R.string.UserDeleteMessage,IduserDelete.getText());

                }

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
                                EditText input = new EditText(getBaseContext());
                                AlertDialog.Builder dialegPassword = new AlertDialog.Builder(AreaAvaluadorActivity.this);
                                        dialegPassword
                                        .setIcon(R.drawable.passwordicon)
                                        .setTitle(R.string.PasswordDialog)
                                        .setMessage(R.string.IntroducePassword)
                                        .setView(input)
                                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                // Some stuff to do when ok got clicked

                                                //Confirmar eliminació per contrasenya

                                                //if(input.getText().toString()==Firebase password)

                                               // else{
                                                   // dialegPassword.setMessage(getString(R.string.IncorrectPassword))
                                                //}



                                            }
                                        })
                                        .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                // Some stuff to do when cancel got clicked
                                                //Cancelar diàleg



                                            }
                                        })
                                        .show();



















                                //------------------------------------
                                //Eliminar usuari a FireBase i quedar-se a la mateixa pantalla



                            }
                        })
                        .setNegativeButton(getString(R.string.KO), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Some stuff to do when cancel got clicked



                            }
                        })
                        .show();



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
