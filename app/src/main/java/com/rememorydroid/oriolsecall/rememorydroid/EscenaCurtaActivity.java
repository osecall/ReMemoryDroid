package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static android.view.KeyEvent.ACTION_UP;

public class EscenaCurtaActivity extends BaseActivity {

    private ImageView ivPicturePreferred;

    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefFavour;
    private String Episodi;
    private Button btNextEscenaCurta;
    private PacientUsuari pacient;
    private EditText etCurta11, etCurta22, etCurta33,etCurta44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escena_curta);


        Episodi = ObtenirEpisodi();
        pacient = ObtenirPacient();

        etCurta11 = (EditText) findViewById(R.id.etCurta11);
        etCurta22 = (EditText) findViewById(R.id.etCurta22);
        etCurta33 = (EditText) findViewById(R.id.etCurta33);
        etCurta44 = (EditText) findViewById(R.id.etCurta44);

        ivPicturePreferred = (ImageView) findViewById(R.id.ivPicturePreferredCurta);
        btNextEscenaCurta = (Button) findViewById(R.id.btNextEscenaCurta);

        myRefFavour = myRef.child(pacient.getID()).child(Episodi).child("favorita").child("favorita.jpg");

        showProgressDialog();

        myRefFavour.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(EscenaCurtaActivity.this).load(uri).into(ivPicturePreferred);
                hideProgressDialog();
            }
        });

        btNextEscenaCurta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etCurta11.getText().toString().isEmpty() && !etCurta22.getText().toString().isEmpty()
                        && !etCurta33.getText().toString().isEmpty() && !etCurta44.getText().toString().isEmpty()){
                    Intent intent = new Intent(EscenaCurtaActivity.this, RespirarActivity.class);
                    intent.putExtra("Curta1","Curta1");
                    startActivity(intent);
                }
                else showToast(getString(R.string.Misstextfields),true);
            }
        });

        etCurta11.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==66 && event.getAction()==ACTION_UP){
                    etCurta22.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etCurta22.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==66 && event.getAction()==ACTION_UP){
                    etCurta33.requestFocus();
                    return true;
                }
                return false;
            }
        });
        etCurta33.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==66 && event.getAction()==ACTION_UP){
                    etCurta44.requestFocus();
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
            showToast(getString(R.string.signed_out),true);
            Intent areaAvaluador = new Intent(EscenaCurtaActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            showToast(getString(R.string.MenuChangePacient),true);

            Intent areaAvaluador = new Intent(EscenaCurtaActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
