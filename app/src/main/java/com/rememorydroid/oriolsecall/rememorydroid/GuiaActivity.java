package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuiaActivity extends AppCompatActivity {

    private Button btBackGuide;
    private Intent GuideIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);

        btBackGuide = (Button) findViewById(R.id.btBackGuide);

        btBackGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuideIntent = new Intent(GuiaActivity.this, TractamentsActivity.class);
                startActivity(GuideIntent);
            }
        });



    }
}
