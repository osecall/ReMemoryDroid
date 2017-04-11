package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AlbumActivity extends BaseActivity {

    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference RefFavour;
    private ImageView ivAlbum0, ivAlbum1,ivAlbum2,ivAlbum3,ivAlbum4,ivAlbum5,ivAlbum6,ivAlbum7;
    private ArrayList<Uri> uris = new ArrayList<Uri>();
    private SharedPreferences prefs;
    private String ID, episodi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        ID = new String();
        episodi = new String();

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
        ivAlbum6 = (ImageView) findViewById(R.id.ivAlbum6);
        ivAlbum7 = (ImageView) findViewById(R.id.ivAlbum7);

        RefFavour = myRef.child(ID).child("episodi").child("Favorita").child("favorita.jpg");

        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(AlbumActivity.this);
        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setMessage(R.string.HereShowingImages)
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
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                            R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                            intent.putExtra("favorita", uris.get(0).toString());
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
                ivAlbum0.setDrawingCacheEnabled(true);
                ivAlbum0.buildDrawingCache();
                Bitmap bitmap = ivAlbum0.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                            intent.putExtra("favorita", uris.get(1).toString());
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
                ivAlbum0.setDrawingCacheEnabled(true);
                ivAlbum0.buildDrawingCache();
                Bitmap bitmap = ivAlbum0.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                            intent.putExtra("favorita", uris.get(2).toString());
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
                ivAlbum0.setDrawingCacheEnabled(true);
                ivAlbum0.buildDrawingCache();
                Bitmap bitmap = ivAlbum0.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                            intent.putExtra("favorita", uris.get(3).toString());
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
                ivAlbum0.setDrawingCacheEnabled(true);
                ivAlbum0.buildDrawingCache();
                Bitmap bitmap = ivAlbum0.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                            intent.putExtra("favorita", uris.get(4).toString());
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
                ivAlbum0.setDrawingCacheEnabled(true);
                ivAlbum0.buildDrawingCache();
                Bitmap bitmap = ivAlbum0.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                            intent.putExtra("favorita", uris.get(5).toString());
                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        ivAlbum6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum0.setDrawingCacheEnabled(true);
                ivAlbum0.buildDrawingCache();
                Bitmap bitmap = ivAlbum0.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                            intent.putExtra("favorita", uris.get(6).toString());
                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        ivAlbum7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum0.setDrawingCacheEnabled(true);
                ivAlbum0.buildDrawingCache();
                Bitmap bitmap = ivAlbum0.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                            intent.putExtra("favorita", uris.get(7).toString());
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
        StorageReference Ref6 = myRef.child(ID).child(episodi).child("6.jpg");
        StorageReference Ref7 = myRef.child(ID).child(episodi).child("7.jpg");

        showProgressDialog();
        Ref0.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                while(!task.isComplete()) showProgressDialog();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum0);
                        uris.add(uri);
                        hideProgressDialog();
                    }
                });
            }
        });
        Ref1.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                while(!task.isComplete()) showProgressDialog();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum1);
                        uris.add(uri);
                        hideProgressDialog();
                    }
                });
            }
        });
        Ref2.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                while(!task.isComplete()) showProgressDialog();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum2);
                        uris.add(uri);
                        hideProgressDialog();
                    }
                });
            }
        });
        Ref3.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                while(!task.isComplete()) showProgressDialog();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum3);
                        uris.add(uri);
                        hideProgressDialog();
                    }
                });
            }
        });
        Ref4.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                while(!task.isComplete()) showProgressDialog();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum4);
                        uris.add(uri);
                        hideProgressDialog();
                    }
                });
            }
        });
        Ref5.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                while(!task.isComplete()) showProgressDialog();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum5);
                        uris.add(uri);
                        hideProgressDialog();
                    }
                });
            }
        });
        Ref6.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                while(!task.isComplete()) showProgressDialog();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum6);
                        uris.add(uri);
                        hideProgressDialog();
                    }
                });
            }
        });
        Ref7.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                while(!task.isComplete()) showProgressDialog();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum7);
                        uris.add(uri);
                        hideProgressDialog();
                    }
                });
            }
        });
    }
}
