package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Convert extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        LinearLayout linearLayout = findViewById(R.id.back);
        linearLayout.setAlpha(0);
        linearLayout.animate().alpha((float) 0.7).setDuration(300);

        LinearLayout plateLayout = findViewById(R.id.plate);
        plateLayout.setTranslationY(800);
        plateLayout.animate().translationY(0).setDuration(300);
        Intent intent = getIntent();
    }
}