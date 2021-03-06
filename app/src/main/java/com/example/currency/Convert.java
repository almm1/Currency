package com.example.currency;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Convert extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        final TextView textView = findViewById(R.id.textView2);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        LinearLayout linearLayout = findViewById(R.id.back);
        linearLayout.setAlpha(0);
        linearLayout.animate().alpha((float) 0.7).setDuration(300);

        LinearLayout plateLayout = findViewById(R.id.plate);
        plateLayout.setTranslationY(800);
        plateLayout.animate().translationY(0).setDuration(300);

        Intent intent = getIntent();
        String s = intent.getStringExtra("CharCode");
        final float value = intent.getFloatExtra("value", 0);
        final int nominal = intent.getIntExtra("nominal", 0);
        textView.setText(s);

        Button btnAlpha = (Button)findViewById(R.id.button);
        btnAlpha.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                convert(value, nominal);
            }
        });
    }
    public void convert(float value, int nominal){
        EditText textIn = findViewById(R.id.editTextNumber);
        EditText textOut = findViewById(R.id.editTextNumber2);

        float in = Float.parseFloat(String.valueOf(textIn.getText()));
        in = (in/value)*nominal;
        in = (float) (Math.round(in * 100) / 100.0);

        String string =  ""+Float.toString(in);
        textOut.setText(string);
    }
}