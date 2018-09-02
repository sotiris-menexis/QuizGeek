package com.example.sotosmen.quizgeek;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public class StartScreen extends Activity {
    public static ArrayList<Question> questions = new ArrayList<>();
    public static Button new_B;
    public static Button continue_B;
    public static ProgressBar loading;
    public static DatabaseHelper db;
    public static Button options_B;
    public static boolean startCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        final Animation new_cont_Anim = AnimationUtils.loadAnimation(this,R.anim.new_cont);
        options_B = (Button) findViewById(R.id.options_B);
        continue_B = (Button) findViewById(R.id.continue_B);
        new_B = (Button) findViewById(R.id.new_B);
        loading = (ProgressBar) findViewById(R.id.loading_S);
        loading.setVisibility(View.INVISIBLE);
        //Initializing the Database.
        db = new DatabaseHelper(getApplicationContext());

        new Start().execute();
        //A recommendation for the User to check his/her preferences.
        if(startCheck) {
            Toast.makeText(getApplicationContext(), "Please enter your preferences in the options.", Toast.LENGTH_LONG).show();
        }
        startCheck=false;
        //Starting the options activity.
        options_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartScreen.this, Options.class));
            }
        });
        //If there is internet connection then it starts the Question Activity.
        new_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    view.startAnimation(new_cont_Anim);
                    new Initialization().execute(0,null,null);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //If
        continue_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    view.startAnimation(new_cont_Anim);
                    new Initialization().execute(1,null,null);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class Initialization extends AsyncTask<Integer, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            new_B.setVisibility(View.INVISIBLE);
            continue_B.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        }
        //Creating a connection to the server and the passing the contents of the JSON file to the question Arraylist
        //As question objects.
        @Override
        protected Void doInBackground(Integer... ints) {
            StringBuffer str = new StringBuffer();
            String line = "";
            InputStream input;
            BufferedReader buffer;
            HttpURLConnection connection;

            URL url = null;
            try {
                url = new URL("https://opentdb.com/api.php?amount=50&type=boolean&encode=url3986");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                input = connection.getInputStream();
                buffer = new BufferedReader(new InputStreamReader(input));

                while ((line = buffer.readLine()) != null) {
                    str.append(line + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
                try {
                    int counter = 0;
                    boolean flag = true;
                    do {
                        JSONObject json = new JSONObject(str.toString());
                        JSONArray jsonArray = json.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = jsonArray.getJSONObject(i);
                            Question questTemp = new Question();
                            questTemp.setQuestion(URLDecoder.decode(temp.getString("question"), "UTF-8"));
                            questTemp.setCategory(URLDecoder.decode(temp.getString("category"), "UTF-8"));
                            questTemp.setCorrect_answer(temp.getString("correct_answer"));
                            questTemp.setDifficulty(temp.getString("difficulty"));
                            questTemp.setIncorrect_answer(temp.getString("incorrect_answers"));
                            questions.add(questTemp);
                        }

                        ArrayList<Question> res = db.getAllData();
                        for (int i = 0; i < questions.size(); i++) {
                            for (int j = 0; j < res.size(); j++) {
                                Log.d("Database", questions.get(i).getQuestion());
                                if (questions.get(i).getQuestion().equals(res.get(j).getQuestion())) {
                                    questions.remove(i);
                                    break;
                                }
                            }
                        }

                        if (counter == 59) {
                            db.deleteAll();
                        }
                        if(Questions_Act.num_remaining == -20){
                            if (questions.size() >= Questions_Act.num_remaining_def) {
                                flag = false;
                            }
                        }else {
                            if (questions.size() >= Questions_Act.num_remaining) {
                                flag = false;
                            }
                        }
                        counter++;
                    } while (flag);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(ints[0]==1){
                    Question temp = db.getContinue();
                    questions.add(0,temp);
                    db.deleteContinue();
                }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.setVisibility(View.INVISIBLE);
            startActivity(new Intent(StartScreen.this, Questions_Act.class));
        }

    }
    //Runs at the start of the Activity to check if the continue exists in the Database and if it does
    //It enables the button.
    private class Start extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            new_B.setVisibility(View.INVISIBLE);
            continue_B.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean flag;
            int NoOfRows = (int) DatabaseUtils.longForQuery(db.getWritableDatabase(),"SELECT COUNT(*) FROM CONTINUE",null);
            //Checking if Continue table is empty.
            if (NoOfRows == 0){
                flag=false;
            }else {
                flag=true;
            }
            return flag;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.d("ERROR BOOLEAN", ""+aBoolean);
            new_B.setVisibility(View.VISIBLE);
            continue_B.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
            if(!aBoolean){
                continue_B.setEnabled(false);
            }
        }

    }

}