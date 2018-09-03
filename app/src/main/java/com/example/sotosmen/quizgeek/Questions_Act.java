package com.example.sotosmen.quizgeek;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Questions_Act extends AppCompatActivity {
    private static Button trueB;
    private static Button falseB;
    private static ProgressBar loading;
    protected static int flag =0;
    public static TextView question;
    public static TextView scoreTxt;
    private static TextView questStr;
    protected static DatabaseHelper db;
    protected static Question temp;
    protected static int num_remaining=-20;
    protected static final int num_remaining_def=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        //Initializing all widgets used in the activity.
        final Animation true_false_Anim = AnimationUtils.loadAnimation(this,R.anim.true_false);

        trueB = (Button) findViewById(R.id.true_B);
        falseB = (Button) findViewById(R.id.false_B);
        question = (TextView) findViewById(R.id.questText);
        loading = (ProgressBar) findViewById(R.id.loading_Q);
        questStr = (TextView) findViewById(R.id.questStr);
        scoreTxt = (TextView) findViewById(R.id.scoreStr);
        //If the number of the questions to be answered hasn't been changed by the user the default number is used which is 10.
        if(num_remaining == -20){
            num_remaining = num_remaining_def;
        }
        //Setting all the widgets to be invisible since everything is set to visible dynamicly by the QuestionInit asyncTask.
        scoreTxt.setVisibility(View.INVISIBLE);
        questStr.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
        question.setVisibility(View.INVISIBLE);
        trueB.setVisibility(View.INVISIBLE);
        falseB.setVisibility(View.INVISIBLE);
        db = new DatabaseHelper(this);
        //Executing the Initialization asyncTask.
        new QuestionInit().execute();

        trueB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(true_false_Anim);
                checkAnswer(1);
                temp = StartScreen.questions.get(0);
                Log.d("Question", StartScreen.questions.get(0).getQuestion());
                boolean res = db.insertData(StartScreen.questions.get(0));
                Log.d("Data",""+res);
                StartScreen.questions.remove(0);
            }
        });
        falseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(true_false_Anim);
                checkAnswer(0);
                temp = StartScreen.questions.get(0);
                Log.d("Question", StartScreen.questions.get(0).getQuestion());
                boolean res = db.insertData(StartScreen.questions.get(0));
                Log.d("Data",""+res);
                StartScreen.questions.remove(0);
            }
        });
    }
    //The asyncTask QuestionInit sets the text of the Question textView to be the first question from the
    //questions list and then inserts the question to the database.
    //After that it removes that question from the questions list.
    //Before anything happens on the background the asyncTask sets the loading progressBar to be visible
    //and when the whole calculation finishes sets all the widgets to be visible and the loading progressBar
    //to be invisible.
    private class QuestionInit extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            question.setText(StartScreen.questions.get(0).getQuestion());
            temp = StartScreen.questions.get(0);
            Log.d("Question", StartScreen.questions.get(0).getQuestion());
            boolean res = db.insertData(StartScreen.questions.get(0));
            Log.d("Data",""+res);
            StartScreen.questions.remove(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            scoreTxt.setText("Score: " + All_Tasks.score);
            scoreTxt.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
            questStr.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);
            trueB.setVisibility(View.VISIBLE);
            falseB.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }
    }
    //Checking if the answer is correct and updating the Score accordingly.
    public void checkAnswer(int flag){
        String user_answer = "";

        if(flag==1){
            user_answer = "True";
        }else if (flag==0) {
            user_answer = "False";
        }

        if(StartScreen.questions.get(0).getCorrect_answer().equals(user_answer)){
            All_Tasks.changeScore(1);
        }else {
            All_Tasks.changeScore(0);
        }
        scoreTxt.setText("Score: " + All_Tasks.score);
        num_remaining--;
        if(num_remaining==0){
            startActivity(new Intent(Questions_Act.this, Result_Screen.class));
            Result_Screen.scoreNum = All_Tasks.score;
            num_remaining=0;
            All_Tasks.score=0;
        }
        question.setText(StartScreen.questions.get(0).getQuestion());
    }
    //If during the answering of the questions the activity stops gets destroyed
    //or put in the background the question that was being displayed is saved in the
    //continue table.
    //If then the Activity gets put in the foreground again then the continue entries
    //in the continue table get deleted.
    @Override
    protected void onStop() {
        super.onStop();
        if(!temp.equals(null)) {
            db.insertContinue(temp);
        }
        Log.d("ERROR DB", temp.getQuestion());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!temp.equals(null)) {
            db.insertContinue(temp);
        }
        Log.d("ERROR DB", temp.getQuestion());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(num_remaining == -20){
            num_remaining = num_remaining_def;
        }
        db.deleteContinue();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(num_remaining == -20){
            num_remaining = num_remaining_def;
        }
    }
}
