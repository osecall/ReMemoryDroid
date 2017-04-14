package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AlbumActivity extends BaseActivity {

    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference RefFavour;
    private ImageView ivAlbum0, ivAlbum1,ivAlbum2,ivAlbum3,ivAlbum4,ivAlbum5,ivAlbum6,ivAlbum7;
    private SharedPreferences prefs;
    private String ID, episodi;
    private Animation aumentar, translate;

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
        ivAlbum6 = (ImageView) findViewById(R.id.ivAlbum6);
        ivAlbum7 = (ImageView) findViewById(R.id.ivAlbum7);

        //Per animar les imatges al clicar a sobre
        aumentar = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom);
        aumentar.reset();
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
                ivAlbum0.setAnimation(aumentar);
                ivAlbum0.startAnimation(aumentar);

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
                        while(!task.isComplete()) showProgressDialog();
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
                                };
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
                ivAlbum1.setAnimation(aumentar);
                ivAlbum1.startAnimation(aumentar);

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
                                };
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
                ivAlbum2.setAnimation(aumentar);
                ivAlbum2.startAnimation(aumentar);

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
                                };
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
                ivAlbum3.setAnimation(aumentar);
                ivAlbum3.startAnimation(aumentar);

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
                                };
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
                ivAlbum4.setAnimation(aumentar);
                ivAlbum4.startAnimation(aumentar);

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
                                };
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
                ivAlbum5.setAnimation(aumentar);
                ivAlbum5.startAnimation(aumentar);

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
                                };
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
        ivAlbum6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivAlbum6.setAnimation(aumentar);
                ivAlbum6.startAnimation(aumentar);

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum6.setDrawingCacheEnabled(true);
                ivAlbum6.buildDrawingCache();
                Bitmap bitmap = ivAlbum6.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data6 = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data6);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            ivAlbum6.setAnimation(translate);
                            ivAlbum6.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data6);
                                    startActivity(intent);
                                };
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
        ivAlbum7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivAlbum7.setAnimation(aumentar);
                ivAlbum7.startAnimation(aumentar);

                //Aconseguim els bits de la foto que conté el view Imageview i el pujem al Firebase
                ivAlbum7.setDrawingCacheEnabled(true);
                ivAlbum7.buildDrawingCache();
                Bitmap bitmap = ivAlbum7.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data7 = baos.toByteArray();

                UploadTask uploadTask = RefFavour.putBytes(data7);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        while(!task.isComplete()) showProgressDialog();
                        if(task.isComplete()){
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),
                                    R.string.PictureUploaded, Toast.LENGTH_LONG).show();
                            ivAlbum7.setAnimation(translate);
                            ivAlbum7.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data7);
                                    startActivity(intent);
                                };
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
                        hideProgressDialog();
                    }
                });
            }
        });
    }
}
