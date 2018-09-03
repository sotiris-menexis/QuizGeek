package com.example.sotosmen.quizgeek;

import java.util.ArrayList;

public class Question {
    //This is the Object that contains all the different fields that exist in the JSON file.
    //This class also contains getters and setters for all the variables.
    private String question;
    private String difficulty;
    private String category;
    private String correct_answer;
    private String incorrect_answer;

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getIncorrect_answer() {
        return incorrect_answer;
    }

    public void setIncorrect_answer(String incorrect_answer) {
        this.incorrect_answer = incorrect_answer;
    }
}