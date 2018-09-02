package com.example.sotosmen.quizgeek;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Options extends AppCompatActivity {
    protected EditText userQNum;
    protected Button saveB;
    protected ProgressBar loading_O;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        final Animation save_Anim = AnimationUtils.loadAnimation(this,R.anim.save);
        userQNum = (EditText) findViewById(R.id.userQNum);
        saveB = (Button) findViewById(R.id.save_B);
        loading_O = (ProgressBar) findViewById(R.id.loading_O);
        loading_O.setVisibility(View.INVISIBLE);
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = String.valueOf(userQNum.getText()).trim();
                view.startAnimation(save_Anim);
                if(isInteger(str)){
                    Questions_Act.num_remaining = Integer.valueOf(str);
                    startActivity(new Intent(Options.this, StartScreen.class));
                }else if(str.equals("")) {
                    startActivity(new Intent(Options.this, StartScreen.class));
                }else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

}