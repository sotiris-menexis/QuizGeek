package com.example.sotosmen.quizgeek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Result_Screen extends AppCompatActivity {
    public static TextView finalScore;
    public static int scoreNum;
    public static Button menu_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //This is the activity that displays the final Score
        //After that sets the score back to 0 and deletes any entries in the continue table
        //since the game session has been completed successfully.
        final Animation menu_Anim = AnimationUtils.loadAnimation(this,R.anim.main_menu);

        finalScore = (TextView) findViewById(R.id.finalScore);
        menu_B = (Button) findViewById(R.id.menu_B);
        finalScore.setText("     Final\nScore: " + scoreNum);
        All_Tasks.score = 0;
        menu_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(menu_Anim);
                Questions_Act.db.deleteContinue();
                Questions_Act.num_remaining = -20;
                startActivity(new Intent(Result_Screen.this,StartScreen.class));
            }
        });
    }
    //In case the app stops before it runs the code in oncreate these two methods make sure that the
    //continue table entries will be deleted.
    @Override
    protected void onStop() {
        super.onStop();
        Questions_Act.db.deleteContinue();
    }

    protected void onDestroy() {
        super.onDestroy();
        Questions_Act.db.deleteContinue();
    }

}