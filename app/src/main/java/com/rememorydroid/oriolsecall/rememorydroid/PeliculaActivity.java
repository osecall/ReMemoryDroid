package com.rememorydroid.oriolsecall.rememorydroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class PeliculaActivity extends AppCompatActivity {

    private TextDrawable FromPage, ToPage;
    private ImageView ivFromPage, ivToPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        ColorGenerator generator = ColorGenerator.DEFAULT;
        FromPage = TextDrawable.builder().beginConfig().width(70).height(70).endConfig().buildRound("1",generator.getRandomColor());
        ToPage = TextDrawable.builder().beginConfig().width(70).height(70).endConfig().buildRound("30",generator.getRandomColor());

        ivFromPage = (ImageView) findViewById(R.id.ivFromPage1);
        ivToPage = (ImageView) findViewById(R.id.ivToPage1);

        ivFromPage.setImageDrawable(FromPage);
        ivToPage.setImageDrawable(ToPage);


    }
}
