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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;

public class EmocionsActivity3 extends AppCompatActivity {

    private Button play,stop, btBack, btNext;
    private ImageView happy,angry,sad;
    private SeekBar seekbar;
    private TextView tvValueSeekBar,tvAtAll,tvVery, tvHappy,tvSad,tvAngry;
    private String CaraSeleccionada=null;
    private String IntensitatSeleccionada=null;
    private VideoView vvEmotions1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emocions3);


        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(EmocionsActivity3.this);
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
                respostes.setPreguntesEmocionsEscena3_Emocio(CaraSeleccionada);
                respostes.setPreguntesEmocionsEscena3_Intentistat(IntensitatSeleccionada);
                respostes_json = gson.toJson(respostes);
                editor.putString("respostes",respostes_json);
                editor.commit();
                startActivity(new Intent(EmocionsActivity3.this,AlbumActivity.class));

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

}
