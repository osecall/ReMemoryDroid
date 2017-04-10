package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.DialogInterface;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AlbumActivity extends BaseActivity {

    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private ImageView ivAlbum0, ivAlbum1,ivAlbum2,ivAlbum3,ivAlbum4,ivAlbum5,ivAlbum6,ivAlbum7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);


        ivAlbum0 = (ImageView) findViewById(R.id.ivAlbum0);
        ivAlbum1 = (ImageView) findViewById(R.id.ivAlbum1);
        ivAlbum2 = (ImageView) findViewById(R.id.ivAlbum2);
        ivAlbum3 = (ImageView) findViewById(R.id.ivAlbum3);
        ivAlbum4 = (ImageView) findViewById(R.id.ivAlbum4);
        ivAlbum5 = (ImageView) findViewById(R.id.ivAlbum5);
        ivAlbum6 = (ImageView) findViewById(R.id.ivAlbum6);
        ivAlbum7 = (ImageView) findViewById(R.id.ivAlbum7);

        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(AlbumActivity.this);
        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setMessage(R.string.HereShowingImages)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                })
                .show();

        StorageReference Ref0 = myRef.child("0").child("java.png");
        StorageReference Ref1 = myRef.child("0").child("audiof.png");
        StorageReference Ref2 = myRef.child("0").child("audio.png");
        StorageReference Ref3 = myRef.child("0").child("iconecatalunya.png");
        StorageReference Ref4 = myRef.child("0").child("iconerememory.png");
        StorageReference Ref5 = myRef.child("0").child("iconoinglaterra.png");
        StorageReference Ref6 = myRef.child("0").child("iconeespana.png");
        StorageReference Ref7 = myRef.child("0").child("microf.png");



        showProgressDialog();
        Ref0.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum0);
           }
        });
        Ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum1);
            }
        });
        Ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum2);
            }
        });
        Ref3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum3);
            }
        });
        Ref4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum4);
            }
        });
        Ref5.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum5);
            }
        });
        Ref6.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum6);
            }
        });
        Ref7.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum7);
            }
        });
        hideProgressDialog();

        ivAlbum0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAlbum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAlbum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAlbum3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAlbum4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAlbum5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAlbum7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });





    }
}
