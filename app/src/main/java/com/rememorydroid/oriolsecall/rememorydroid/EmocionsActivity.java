package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

public class EmocionsActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private Button play,stop, btBack, btNext;
    private ImageView happy,angry,sad;
    private SeekBar seekbar;
    private TextView tvValueSeekBar,tvAtAll,tvVery, tvHappy,tvSad,tvAngry;
    private String CaraSeleccionada, IntensitatSeleccionada;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emocions);


        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(EmocionsActivity.this);
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

                btNext.setVisibility(View.VISIBLE);

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

                btNext.setVisibility(View.VISIBLE);

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

                btNext.setVisibility(View.VISIBLE);

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
                respostes.setPreguntesEmocionsEscena1_Emocio(CaraSeleccionada);
                respostes.setPreguntesEmocionsEscena1_Intentistat(IntensitatSeleccionada);
                respostes_json = gson.toJson(respostes);
                editor.putString("respostes",respostes_json);
                editor.commit();
                startActivity(new Intent(EmocionsActivity.this,EmocionsActivity2.class));

            }
        });




    }
}
