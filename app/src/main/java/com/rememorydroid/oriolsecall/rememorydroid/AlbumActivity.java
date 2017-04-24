package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AlbumActivity extends BaseActivity {

    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference RefFavour;
    private ImageView ivAlbum0, ivAlbum1,ivAlbum2,ivAlbum3,ivAlbum4,ivAlbum5;
    private SharedPreferences prefs;
    private String ID, episodi;
    private Animation translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        ID = new String();
        episodi = new String();

        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);

        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String pacient_json = prefs.getString("pacient",null);
        PacientUsuari pacient = gson.fromJson(pacient_json,PacientUsuari.class);
        episodi = prefs.getString("episodi",null);

        ID = pacient.getID();

        ivAlbum0 = (ImageView) findViewById(R.id.ivAlbum0);
        ivAlbum1 = (ImageView) findViewById(R.id.ivAlbum1);
        ivAlbum2 = (ImageView) findViewById(R.id.ivAlbum2);
        ivAlbum3 = (ImageView) findViewById(R.id.ivAlbum3);
        ivAlbum4 = (ImageView) findViewById(R.id.ivAlbum4);
        ivAlbum5 = (ImageView) findViewById(R.id.ivAlbum5);

        //Per animar les imatges al clicar a sobre
        translate = AnimationUtils.loadAnimation(getBaseContext(), R.anim.translate);
        translate.reset();

        RefFavour = myRef.child(ID).child(episodi).child("Favorita").child("favorita.jpg");

        AlertDialog.Builder Dialeg = new AlertDialog.Builder(AlbumActivity.this);
        TextView Missatge = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        Missatge.setText(getString(R.string.HereShowingImages));

        Dialeg
                .setTitle(getString(R.string.Attention))
                .setView(textEntryView)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        descargarImatges();
                        arg0.dismiss();
                    }
                })
                .show();

        ivAlbum0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum0.setDrawingCacheEnabled(true);
                ivAlbum0.buildDrawingCache();
                Bitmap bitmap = ivAlbum0.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data0 = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data0);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()){
                            showProgressDialog();
                           }
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                            R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            ivAlbum0.setAnimation(translate);
                            ivAlbum0.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data0);
                                    startActivity(intent);
                                }
                            }, 3000);

                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        ivAlbum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum1.setDrawingCacheEnabled(true);
                ivAlbum1.buildDrawingCache();
                Bitmap bitmap = ivAlbum1.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data1 = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data1);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            ivAlbum1.setAnimation(translate);
                            ivAlbum1.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data1);
                                    startActivity(intent);
                                }
                            }, 3000);

                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        ivAlbum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum2.setDrawingCacheEnabled(true);
                ivAlbum2.buildDrawingCache();
                Bitmap bitmap = ivAlbum2.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data2 = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data2);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            ivAlbum2.setAnimation(translate);
                            ivAlbum2.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data2);
                                    startActivity(intent);
                                }
                            }, 3000);

                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        ivAlbum3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum3.setDrawingCacheEnabled(true);
                ivAlbum3.buildDrawingCache();
                Bitmap bitmap = ivAlbum3.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data3 = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data3);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            ivAlbum3.setAnimation(translate);
                            ivAlbum3.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data3);
                                    startActivity(intent);
                                }
                            }, 3000);

                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        ivAlbum4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum4.setDrawingCacheEnabled(true);
                ivAlbum4.buildDrawingCache();
                Bitmap bitmap = ivAlbum4.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data4 = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data4);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            ivAlbum4.setAnimation(translate);
                            ivAlbum4.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data4);
                                    startActivity(intent);
                                }
                            }, 3000);

                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        ivAlbum5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum5.setDrawingCacheEnabled(true);
                ivAlbum5.buildDrawingCache();
                Bitmap bitmap = ivAlbum5.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data5 = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data5);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            ivAlbum5.setAnimation(translate);
                            ivAlbum5.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data5);
                                    startActivity(intent);
                                }
                            }, 3000);

                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void descargarImatges(){
        StorageReference Ref0 = myRef.child(ID).child(episodi).child("0.jpg");
        StorageReference Ref1 = myRef.child(ID).child(episodi).child("1.jpg");
        StorageReference Ref2 = myRef.child(ID).child(episodi).child("2.jpg");
        StorageReference Ref3 = myRef.child(ID).child(episodi).child("3.jpg");
        StorageReference Ref4 = myRef.child(ID).child(episodi).child("4.jpg");
        StorageReference Ref5 = myRef.child(ID).child(episodi).child("5.jpg");


        showProgressDialog();
        Ref0.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum0);
                hideProgressDialog();
            }
        });
        showProgressDialog();
        Ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum1);
                hideProgressDialog();
            }
        });
        showProgressDialog();
        Ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum2);
                hideProgressDialog();
            }
        });
        showProgressDialog();
        Ref3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum3);
                hideProgressDialog();
            }
        });
        showProgressDialog();
        Ref4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum4);
                hideProgressDialog();
            }
        });
        showProgressDialog();
        Ref5.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum5);
                hideProgressDialog();
            }
        });

    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out,FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient)+" ("+ID+")");
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
            Toast.makeText(AlbumActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(AlbumActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(AlbumActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(AlbumActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }


        return super.onOptionsItemSelected(item);
    }
}
