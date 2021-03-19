package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        final CharSequence charSequence = s;
        textView.setText(charSequence);

        Button btnAlpha = (Button)findViewById(R.id.button);
        btnAlpha.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                convert(value);
            }
        });
    }
    public void convert(float value){
        EditText textIn = findViewById(R.id.editTextNumber);
        EditText textOut = findViewById(R.id.editTextNumber2);

        float in=0;

        in = Float.parseFloat(String.valueOf(textIn.getText()));
        in = in/value;
        in = (float) (Math.round(in * 100) / 100.0);

        String string =  ""+Float.toString(in);
        textOut.setText(string);
    }
}