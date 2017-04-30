package com.rememorydroid.oriolsecall.rememorydroid;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import static android.view.KeyEvent.ACTION_UP;

public class EscenaActivity extends BaseActivity {

    private EditText etQuestion1Escena, etQuestion2Escena,etQuestion3Escena,etQuestion4Escena;
    private ImageView imatgeEscena;
    private Spinner eLvEmocions;
    private Button btNextEscena;
    private SeekBar seekBar2;
    private String A;
    private Intent intent;
    private TextView tvValorIntensity1;
    private PacientUsuari pacient;
    private TestAnswers respostes_recuperades = new TestAnswers();
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private Gson gson = new Gson();
    private boolean intensitatCambiada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escena);

        intensitatCambiada = false;

        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor = prefs.edit();
        pacient = gson.fromJson(prefs.getString("pacient",null), PacientUsuari.class);
        respostes_recuperades = gson.fromJson(prefs.getString("respostes",null),TestAnswers.class);

        etQuestion1Escena = (EditText) findViewById(R.id.editText1);
        etQuestion2Escena = (EditText) findViewById(R.id.editText2);
        etQuestion3Escena = (EditText) findViewById(R.id.et3);
        etQuestion4Escena = (EditText) findViewById(R.id.editText4);

        tvValorIntensity1 = (TextView) findViewById(R.id.tvValorIntensity1);


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
                    Toast.makeText(getApplicationContext(),
                            R.string.ChooseSpinner, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),
                        R.string.ChooseSpinner, Toast.LENGTH_LONG).show();
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvValorIntensity1.setText(String.valueOf(i+1));
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
                intent = new Intent(EscenaActivity.this,Escena2Activity.class);
                intent.putExtra("A",A);
                intent.putExtra("B",etQuestion1Escena.getText().toString());
                intent.putExtra("C",etQuestion2Escena.getText().toString());
                intent.putExtra("D",etQuestion3Escena.getText().toString());
                intent.putExtra("E",etQuestion4Escena.getText().toString());
                intent.putExtra("favorita",imatge_favorita);
                editor.putString("respostes",gson.toJson(respostes_recuperades,TestAnswers.class));
                editor.commit();
                if(etQuestion1Escena.getText().toString().isEmpty() || etQuestion2Escena.getText().toString().isEmpty() ||
                        etQuestion3Escena.getText().toString().isEmpty() || etQuestion4Escena.getText().toString().isEmpty()){
                    Toast.makeText(EscenaActivity.this, R.string.Misstextfields,
                            Toast.LENGTH_LONG).show();
                }
                else if(eLvEmocions.getSelectedItemPosition()<=0){
                    Toast.makeText(EscenaActivity.this, R.string.ChooseEmotion,
                            Toast.LENGTH_LONG).show();
                }
                else if(seekBar2.getProgress()<1){
                    Toast.makeText(EscenaActivity.this, R.string.ChooseIntensity,
                            Toast.LENGTH_LONG).show();
                }
                else if(!intensitatCambiada){
                    Toast.makeText(EscenaActivity.this, R.string.ChooseIntensity,
                            Toast.LENGTH_LONG).show();
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

            //Retorna a la pantalla inicial
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(EscenaActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EscenaActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(EscenaActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EscenaActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }

}
