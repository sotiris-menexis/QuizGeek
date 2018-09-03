package com.example.sotosmen.quizgeek;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.support.v4.content.ContextCompat.getSystemService;

public class All_Tasks {
    //This class is used only to store the score and the change score method.
    public static int score =0;

    public static void changeScore(int flag){

        if(flag==1){
            score+=10;
        }else
            if(score !=0) {
                score -= 5;
            }
    }

}
