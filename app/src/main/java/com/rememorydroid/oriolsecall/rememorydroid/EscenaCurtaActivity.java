package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EscenaCurtaActivity extends BaseActivity {

    private ImageView ivPicturePreferred;

    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference myRefFavour;
    private String Episodi;
    private Button btNextEscenaCurta;
    private Spinner snEmocions;
    private PacientUsuari pacient;
    private TextView tvCurta22, tvCurta33, tvCurta44, tvEscenaInstruccions3;
    private EditText etCurta44;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("pacients");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escena_curta);

        Episodi = ObtenirEpisodi();
        pacient = ObtenirPacient();

        tvEscenaInstruccions3 = (TextView) findViewById(R.id.tvEscenaInstruccions3);
        tvCurta22 = (TextView) findViewById(R.id.tvCurta22);
        tvCurta33 = (TextView) findViewById(R.id.tvCurta33);
        tvCurta44 = (TextView) findViewById(R.id.tvCurta44);
        etCurta44 = (EditText) findViewById(R.id.etCurta44);
        snEmocions = (Spinner) findViewById(R.id.snEmocions);


        dbRef.child(ObtenirPacient().getID()).child("episodis").child(ObtenirEpisodi().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvEscenaInstruccions3.setText(dataSnapshot.child("Name").getValue(String.class));
                tvCurta22.setText(dataSnapshot.child("Text_B").getValue(String.class));
                tvCurta33.setText(" " + dataSnapshot.child("Text_C").getValue(String.class) + ",");
                tvCurta44.setText(getString(R.string.InTheSub, dataSnapshot.child("Text_D").getValue(String.class)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToastError();
            }
        });

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
                if (!etCurta44.getText().toString().isEmpty()) {
                    Intent intent = new Intent(EscenaCurtaActivity.this, RespirarActivity.class);
                    intent.putExtra("Curta1","Curta1");
                    startActivity(intent);
                } else {
                    if (etCurta44.getText().toString().isEmpty())
                        etCurta44.setError(getString(R.string.FieldEmpty));

                    showToast(getString(R.string.Misstextfields), true);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.emocions, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snEmocions.setAdapter(adapter2);

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
            BorrarPacient();
            showToast(getString(R.string.MenuChangePacient),true);
            Intent areaAvaluador = new Intent(EscenaCurtaActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
    }
}
