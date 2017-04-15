package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class EmocionsActivity2 extends AppCompatActivity {

    private Button play,stop, btBack, btNext;
    private ImageView happy,angry,sad;
    private SeekBar seekbar;
    private TextView tvValueSeekBar,tvAtAll,tvVery, tvHappy,tvSad,tvAngry;
    private String CaraSeleccionada=null;
    private String IntensitatSeleccionada=null;
    private VideoView vvEmotions1;
    private Animation votar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emocions2);

        votar = AnimationUtils.loadAnimation(getBaseContext(), R.anim.votar);
        votar.reset();


            AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(EmocionsActivity2.this);
            DialegFormControl
                    .setTitle(getString(R.string.Attention))
                    .setMessage(R.string.AssignEmotions)
                    .setNeutralButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();

                        }
                    })
                    .show();

            seekbar = (SeekBar) findViewById(R.id.seekBar);

            happy = (ImageView) findViewById(R.id.ivHappy);
            sad = (ImageView) findViewById(R.id.ivSad);
            angry = (ImageView) findViewById(R.id.ivAngry);

            vvEmotions1 = (VideoView) findViewById(R.id.vvEmotions1);

            play = (Button) findViewById(R.id.btPlayEmotions);
            stop = (Button) findViewById(R.id.btStopEmotions);
            btBack = (Button) findViewById(R.id.btBackEmotions1);
            btNext = (Button) findViewById(R.id.btNextEmotions1);

            tvValueSeekBar = (TextView) findViewById(R.id.tvValueSeekBar);
            tvAtAll = (TextView) findViewById(R.id.tvAtAll);
            tvVery = (TextView) findViewById(R.id.tvVery);
            tvHappy = (TextView) findViewById(R.id.tvHappy);
            tvSad = (TextView) findViewById(R.id.tvSad);
            tvAngry = (TextView) findViewById(R.id.tvAngry);

            happy.setEnabled(false);
            sad.setEnabled(false);
            angry.setEnabled(false);
            seekbar.setEnabled(false);


            vvEmotions1.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+ R.raw.video1));

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    tvValueSeekBar.setVisibility(View.VISIBLE);
                    tvValueSeekBar.setText(String.valueOf(i));
                    if(i==0){
                        tvAtAll.setTextColor(Color.RED);
                    }
                    else if(i==10){
                        tvVery.setTextColor(Color.RED);
                    }
                    else{
                        tvAtAll.setTextColor(Color.BLACK);
                        tvVery.setTextColor(Color.BLACK);
                    }
                    IntensitatSeleccionada = String.valueOf(i);
                    if(CaraSeleccionada!=null){
                        btNext.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            happy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    happy.setAnimation(votar);
                    happy.startAnimation(votar);

                    happy.setImageDrawable(getDrawable(R.drawable.checkgreensesentadp));
                    sad.setImageDrawable(getDrawable(R.drawable.iconsadface));
                    angry.setImageDrawable(getDrawable(R.drawable.iconangryface));

                    tvHappy.setTextColor(Color.BLUE);
                    tvSad.setTextColor(Color.BLACK);
                    tvAngry.setTextColor(Color.BLACK);

                    CaraSeleccionada = "Feliç";

                    if(IntensitatSeleccionada!=null){
                        btNext.setVisibility(View.VISIBLE);

                    }

                }
            });

            sad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sad.setAnimation(votar);
                    sad.startAnimation(votar);

                    sad.setImageDrawable(getDrawable(R.drawable.sadcheckedsesentadp));
                    happy.setImageDrawable(getDrawable(R.drawable.iconhappyface));
                    angry.setImageDrawable(getDrawable(R.drawable.iconangryface));

                    tvHappy.setTextColor(Color.BLACK);
                    tvSad.setTextColor(Color.BLUE);
                    tvAngry.setTextColor(Color.BLACK);

                    CaraSeleccionada = "Trist";

                    if(IntensitatSeleccionada!=null){
                        btNext.setVisibility(View.VISIBLE);

                    }
                }
            });

            angry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    angry.setAnimation(votar);
                    angry.startAnimation(votar);

                    angry.setImageDrawable(getDrawable(R.drawable.angrychecksesentadp));
                    happy.setImageDrawable(getDrawable(R.drawable.iconhappyface));
                    sad.setImageDrawable(getDrawable(R.drawable.iconsadface));

                    tvHappy.setTextColor(Color.BLACK);
                    tvSad.setTextColor(Color.BLACK);
                    tvAngry.setTextColor(Color.BLUE);

                    CaraSeleccionada = "Enfadat";

                    if(IntensitatSeleccionada!=null){
                        btNext.setVisibility(View.VISIBLE);
                    }
                }
            });

            btNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    Gson gson = new Gson();
                    //Passem valor sel·leccionat com Integer
                    String respostes_json = prefs.getString("respostes",null);
                    TestAnswers respostes = gson.fromJson(respostes_json,TestAnswers.class);
                    respostes.setPreguntesEmocionsEscena2_Emocio(CaraSeleccionada);
                    respostes.setPreguntesEmocionsEscena2_Intentistat(IntensitatSeleccionada);
                    respostes_json = gson.toJson(respostes);
                    editor.putString("respostes",respostes_json);
                    editor.commit();
                    startActivity(new Intent(EmocionsActivity2.this,EmocionsActivity3.class));

                }
            });

            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    happy.setEnabled(true);
                    sad.setEnabled(true);
                    angry.setEnabled(true);
                    seekbar.setEnabled(true);

                    if(vvEmotions1.isPlaying()){
                        vvEmotions1.resume();
                    }
                    else{
                        vvEmotions1.start();
                    }

                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vvEmotions1.pause();

                }
            });

        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            vvEmotions1=null;
        }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
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
            Toast.makeText(EmocionsActivity2.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EmocionsActivity2.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(EmocionsActivity2.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(EmocionsActivity2.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }


        return super.onOptionsItemSelected(item);
    }



    }
