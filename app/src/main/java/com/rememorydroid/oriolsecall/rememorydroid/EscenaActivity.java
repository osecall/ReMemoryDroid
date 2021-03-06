package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.KeyEvent.ACTION_UP;

public class EscenaActivity extends BaseActivity {

    private EditText etQuestion1Escena, etQuestion2Escena,etQuestion3Escena,etQuestion4Escena;
    private ImageView imatgeEscena, ivValorIntensity1;
    private Spinner eLvEmocions;
    private Button btNextEscena;
    private SeekBar seekBar2;
    private String A;
    private Intent intent;
    private PacientUsuari pacient;
    private TestAnswers respostes_recuperades;
    private boolean intensitatCambiada;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("pacients");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escena);


        respostes_recuperades= new TestAnswers();
        intensitatCambiada = false;

        pacient = ObtenirPacient();
        respostes_recuperades = ObtenirRespostesActuals();

        etQuestion1Escena = (EditText) findViewById(R.id.editText1);
        etQuestion2Escena = (EditText) findViewById(R.id.editText2);
        etQuestion3Escena = (EditText) findViewById(R.id.et3);
        etQuestion4Escena = (EditText) findViewById(R.id.editText4);

        ivValorIntensity1 = (ImageView) findViewById(R.id.ivValorIntensity1);


        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        btNextEscena = (Button) findViewById(R.id.btNextEscena);
        imatgeEscena = (ImageView) findViewById(R.id.ivPicturePreferredCurta);
        eLvEmocions = (Spinner) findViewById(R.id.eLvEmocions);


        showProgressDialog();
        final byte[] imatge_favorita = getIntent().getByteArrayExtra("favorita");
        Bitmap imatge_seleccionada = BitmapFactory.decodeByteArray(imatge_favorita, 0, imatge_favorita.length);
        imatgeEscena.setImageBitmap(imatge_seleccionada);
        hideProgressDialog();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.emocions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eLvEmocions.setAdapter(adapter);

        etQuestion1Escena.setSelection(etQuestion1Escena.getSelectionEnd());
        etQuestion2Escena.setSelection(etQuestion2Escena.getSelectionEnd());
        etQuestion4Escena.setSelection(etQuestion4Escena.getSelectionEnd());

        etQuestion1Escena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etQuestion1Escena.setSelection(etQuestion1Escena.getSelectionEnd());
            }
        });
        etQuestion2Escena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etQuestion2Escena.setSelection(etQuestion2Escena.getSelectionEnd());
            }
        });
        etQuestion4Escena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etQuestion4Escena.setSelection(etQuestion4Escena.getSelectionEnd());
            }
        });


        eLvEmocions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    A = eLvEmocions.getItemAtPosition(i).toString();
                    respostes_recuperades.setPreguntesEmocionsEscenaEmocio(A);
                    btNextEscena.setVisibility(View.VISIBLE);
                }
                else{
                    showToast(getString(R.string.ChooseSpinner), true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                showToast(getString(R.string.ChooseSpinner),true);
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ivValorIntensity1.setImageDrawable(TextDrawable.builder().beginConfig().height(60).width(60).bold().textColor(R.color.white).endConfig().buildRound(String.valueOf(i+1),ColorGenerator.DEFAULT.getColor(R.color.colorPrimaryDark)));
                respostes_recuperades.setPreguntesEmocionsEscenaIntentistat(String.valueOf(i+1));
                intensitatCambiada=true;
                if(eLvEmocions.getSelectedItemPosition()!=0){
                    btNextEscena.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                intensitatCambiada=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btNextEscena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressDialog();
                dbRef.child(ObtenirPacient().getID()).child("episodis").child(ObtenirEpisodi().toString()).child("Text_B").setValue(etQuestion1Escena.getText().toString());
                dbRef.child(ObtenirPacient().getID()).child("episodis").child(ObtenirEpisodi().toString()).child("Text_C").setValue(etQuestion2Escena.getText().toString());
                dbRef.child(ObtenirPacient().getID()).child("episodis").child(ObtenirEpisodi().toString()).child("Text_D").setValue(etQuestion3Escena.getText().toString());
                hideProgressDialog();

                intent = new Intent(EscenaActivity.this,Escena2Activity.class);
                intent.putExtra("A",A);
                intent.putExtra("B",etQuestion1Escena.getText().toString());
                intent.putExtra("C",etQuestion2Escena.getText().toString());
                intent.putExtra("D",etQuestion3Escena.getText().toString());
                intent.putExtra("E",etQuestion4Escena.getText().toString());
                intent.putExtra("favorita",imatge_favorita);

                GravarRespoestesActuals(respostes_recuperades);
                if(etQuestion1Escena.getText().toString().isEmpty() || etQuestion2Escena.getText().toString().isEmpty() ||
                        etQuestion3Escena.getText().toString().isEmpty() || etQuestion4Escena.getText().toString().isEmpty()){
                    showToast(getString(R.string.Misstextfields),true);
                    if (etQuestion1Escena.getText().toString().isEmpty())
                        etQuestion1Escena.setError(getString(R.string.FieldEmpty));
                    if (etQuestion2Escena.getText().toString().isEmpty())
                        etQuestion2Escena.setError(getString(R.string.FieldEmpty));
                    if (etQuestion3Escena.getText().toString().isEmpty())
                        etQuestion3Escena.setError(getString(R.string.FieldEmpty));
                    if (etQuestion4Escena.getText().toString().isEmpty())
                        etQuestion4Escena.setError(getString(R.string.FieldEmpty));
                }
                else if(eLvEmocions.getSelectedItemPosition()<=0){
                    showToast(getString(R.string.ChooseEmotion),true);

                }
                else if(seekBar2.getProgress()<1){
                    showToast(getString(R.string.ChooseIntensity),true);
                }
                else if(!intensitatCambiada){
                    showToast(getString(R.string.ChooseIntensity),true);
                }
                else{
                    startActivity(intent);
                }
            }
        });


        etQuestion1Escena.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==66 && event.getAction()==ACTION_UP){
                    etQuestion2Escena.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etQuestion2Escena.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==66 && event.getAction()==ACTION_UP){
                    etQuestion3Escena.requestFocus();
                    return true;
                }
                return false;
            }
        });
        etQuestion3Escena.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==66 && event.getAction()==ACTION_UP){
                    etQuestion4Escena.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient)+" ("+pacient.getID()+")");
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
            //Confirmar eliminació per contrasenya
            //-------------------------------------------------------------------
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(EscenaActivity.this);
            LayoutInflater factory = LayoutInflater.from(EscenaActivity.this);
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
                                        Intent areaAvaluador = new Intent(EscenaActivity.this, SignInActivity.class);
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
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(EscenaActivity.this);
            LayoutInflater factory = LayoutInflater.from(EscenaActivity.this);
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
                                        Intent areaAvaluador = new Intent(EscenaActivity.this, AreaAvaluadorActivity.class);
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

        return super.onOptionsItemSelected(item);
    }

}
