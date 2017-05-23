package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AlbumActivity extends BaseActivity {

    private StorageReference myRef = FirebaseStorage.getInstance().getReference();
    private StorageReference RefFavour;
    private ImageView ivAlbum0, ivAlbum1,ivAlbum2,ivAlbum3,ivAlbum4,ivAlbum5;
    private String ID, episodi;
    private Animation translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        AudioRecordPermissos();
        WriteStoragePermissos();
        ReadStoragePermissos();

        showProgressDialog();

        ID = new String();
        episodi = new String();

        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);

        PacientUsuari pacient = ObtenirPacient();
        episodi = ObtenirEpisodi();
        ID = pacient.getID();

        ivAlbum0 = (ImageView) findViewById(R.id.ivAlbum0);
        ivAlbum1 = (ImageView) findViewById(R.id.ivAlbum1);
        ivAlbum2 = (ImageView) findViewById(R.id.ivAlbum2);
        ivAlbum3 = (ImageView) findViewById(R.id.ivAlbum3);
        ivAlbum4 = (ImageView) findViewById(R.id.ivAlbum4);
        ivAlbum5 = (ImageView) findViewById(R.id.ivAlbum5);

        ivAlbum0.setVisibility(View.INVISIBLE);
        ivAlbum1.setVisibility(View.INVISIBLE);
        ivAlbum2.setVisibility(View.INVISIBLE);
        ivAlbum3.setVisibility(View.INVISIBLE);
        ivAlbum4.setVisibility(View.INVISIBLE);
        ivAlbum5.setVisibility(View.INVISIBLE);

        ivAlbum0.setEnabled(false);
        ivAlbum1.setEnabled(false);
        ivAlbum2.setEnabled(false);
        ivAlbum3.setEnabled(false);
        ivAlbum4.setEnabled(false);
        ivAlbum5.setEnabled(false);

        descargarImatges();

        //Per animar les imatges al clicar a sobre
        translate = AnimationUtils.loadAnimation(getBaseContext(), R.anim.translate);
        translate.reset();

        RefFavour = myRef.child(ID).child(episodi).child("favorita").child("favorita.jpg");

        AlertDialog.Builder Dialeg = new AlertDialog.Builder(AlbumActivity.this);
        TextView Missatge = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);
        Missatge.setText(getString(R.string.HereShowingImages));

        Dialeg
                .setTitle(getString(R.string.Attention))
                .setView(textEntryView);
        final AlertDialog alerta = Dialeg.create();

        alerta.show();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });

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

                showProgressDialog();
                UploadTask uploadTask = RefFavour.putBytes(data0);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            hideProgressDialog();
                            showToast(getString(R.string.PictureUploaded),true);
                            ivAlbum0.setAnimation(translate);
                            ivAlbum0.startAnimation(translate);


                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    Intent intent = new Intent(AlbumActivity.this,EscenaActivity.class);
                                    intent.putExtra("favorita",data0);
                                    startActivity(intent);
                                }
                            }, 2500);

                        }
                        if(!task.isSuccessful()){
                            showToastError();
                            ivAlbum0.setEnabled(false);
                            ivAlbum0.setVisibility(View.INVISIBLE);
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

                showProgressDialog();
                UploadTask uploadTask = RefFavour.putBytes(data1);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            hideProgressDialog();
                            showToast(getString(R.string.PictureUploaded),true);
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
                           showToastError();
                            ivAlbum1.setEnabled(false);
                            ivAlbum1.setVisibility(View.INVISIBLE);
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

                showProgressDialog();
                UploadTask uploadTask = RefFavour.putBytes(data3);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            hideProgressDialog();
                            showToast(getString(R.string.PictureUploaded),true);
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
                            showToastError();
                            ivAlbum2.setEnabled(false);
                            ivAlbum2.setVisibility(View.INVISIBLE);
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

                showProgressDialog();
                UploadTask uploadTask = RefFavour.putBytes(data2);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            hideProgressDialog();
                            showToast(getString(R.string.PictureUploaded),true);
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
                            showToastError();
                            ivAlbum3.setEnabled(false);
                            ivAlbum3.setVisibility(View.INVISIBLE);
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

                showProgressDialog();
                UploadTask uploadTask = RefFavour.putBytes(data4);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            hideProgressDialog();
                            showToast(getString(R.string.PictureUploaded),true);
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
                            showToastError();
                            ivAlbum4.setEnabled(false);
                            ivAlbum4.setVisibility(View.INVISIBLE);
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

                showProgressDialog();
                UploadTask uploadTask = RefFavour.putBytes(data5);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            hideProgressDialog();
                            showToast(getString(R.string.PictureUploaded),true);
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
                            showToastError();
                            ivAlbum5.setEnabled(false);
                            ivAlbum5.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        hideProgressDialog();
    }

    private void descargarImatges(){

        StorageReference Ref0 = myRef.child(ID).child(episodi).child("imatges").child("0.jpg");
        StorageReference Ref1 = myRef.child(ID).child(episodi).child("imatges").child("1.jpg");
        StorageReference Ref2 = myRef.child(ID).child(episodi).child("imatges").child("2.jpg");
        StorageReference Ref3 = myRef.child(ID).child(episodi).child("imatges").child("3.jpg");
        StorageReference Ref4 = myRef.child(ID).child(episodi).child("imatges").child("4.jpg");
        StorageReference Ref5 = myRef.child(ID).child(episodi).child("imatges").child("5.jpg");

        showProgressDialog();
        Ref0.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivAlbum0.setVisibility(View.VISIBLE);
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum0);
                ivAlbum0.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToastError();
                ivAlbum0.setVisibility(View.INVISIBLE);
            }
        });
        Ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivAlbum1.setVisibility(View.VISIBLE);
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum1);
                ivAlbum1.setEnabled(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToastError();
                ivAlbum1.setVisibility(View.INVISIBLE);

            }
        });
        Ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivAlbum2.setVisibility(View.VISIBLE);
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum2);
                ivAlbum2.setEnabled(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToastError();
                ivAlbum2.setVisibility(View.INVISIBLE);

            }
        });
        Ref3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivAlbum3.setVisibility(View.VISIBLE);
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum3);
                ivAlbum3.setEnabled(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToastError();
                ivAlbum3.setVisibility(View.INVISIBLE);

            }
        });
        Ref4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivAlbum4.setVisibility(View.VISIBLE);
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum4);
                ivAlbum4.setEnabled(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToastError();
                ivAlbum4.setVisibility(View.INVISIBLE);

            }
        });
        Ref5.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivAlbum5.setVisibility(View.VISIBLE);
                Picasso.with(AlbumActivity.this).load(uri).into(ivAlbum5);
                ivAlbum5.setEnabled(true);
                hideProgressDialog();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToastError();
                ivAlbum5.setVisibility(View.INVISIBLE);

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
            showToast(getString(R.string.signed_out),true);
            Intent areaAvaluador = new Intent(AlbumActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {
            //Retorna a la pantalla 'Area Avaluador'
            BorrarPacient();
            showToast(getString(R.string.MenuChangePacient),true);
            Intent areaAvaluador = new Intent(AlbumActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }
        return super.onOptionsItemSelected(item);
    }
}
