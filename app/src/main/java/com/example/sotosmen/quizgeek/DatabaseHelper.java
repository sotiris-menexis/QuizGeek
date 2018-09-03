package com.example.sotosmen.quizgeek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.style.QuoteSpan;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Questions.db";
    private static final String TABLE_NAME_1 = "CONTINUE";
    private static final String TABLE_NAME = "QUESTIONS";
    private static final String COL_1 = "QUESTION";
    private static final String COL_2 = "DIFFICULTY";
    private static final String COL_3 = "CATEGORY";
    private static final String COL_4 = "CORRECT_ANSWER";
    private static final String COL_5 = "INCORRECT_ANSWER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db =  this.getWritableDatabase();
        Log.d("Tag", "Database created");
    }
    //Here the two tables are created. One for the Questions that are answered and one for the continue question.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE QUESTIONS (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT, DIFFICULTY TEXT," +
                " CATEGORY TEXT, CORRECT_ANSWER TEXT, INCORRECT_ANSWER TEXT)");
        db.execSQL("CREATE TABLE CONTINUE (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT , DIFFICULTY TEXT," +
                " CATEGORY TEXT, CORRECT_ANSWER TEXT, INCORRECT_ANSWER TEXT)");
        Log.d("Tag", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContinue(Question q){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,q.getQuestion());
        contentValues.put(COL_2,q.getDifficulty());
        contentValues.put(COL_3,q.getCategory());
        contentValues.put(COL_4,q.getCorrect_answer());
        contentValues.put(COL_5,q.getIncorrect_answer());
        long result = db.insert(TABLE_NAME_1,null,contentValues);
        db.close();
        if(result==-1) {
            return false;
        }
        else{
            return true;
        }
    }

    public boolean insertData(Question q){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,q.getQuestion());
        contentValues.put(COL_2,q.getDifficulty());
        contentValues.put(COL_3,q.getCategory());
        contentValues.put(COL_4,q.getCorrect_answer());
        contentValues.put(COL_5,q.getIncorrect_answer());
        long result = db.insert(TABLE_NAME,null,contentValues);
        db.close();
        if(result==-1) {
            return false;
        }
        else{
            return true;
        }
    }

    public ArrayList<Question> getAllData(){
        ArrayList<Question> results = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
                + " ORDER BY ID DESC",null);

        if (cursor.moveToFirst()) {
            do {
                Question temp = new Question();
                temp.setQuestion(cursor.getString(1));
                temp.setDifficulty(cursor.getString(2));
                temp.setCategory(cursor.getString(3));
                temp.setCorrect_answer(cursor.getString(4));
                temp.setIncorrect_answer(cursor.getString(5));
                results.add(temp);
            } while (cursor.moveToNext());
        }
        db.close();
        return results;
    }

    public Question getContinue(){
        Question results = new Question();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_1
                + " ORDER BY ID DESC",null);

        if (cursor.moveToFirst()) {
                Question temp = new Question();
                temp.setQuestion(cursor.getString(1));
                temp.setDifficulty(cursor.getString(2));
                temp.setCategory(cursor.getString(3));
                temp.setCorrect_answer(cursor.getString(4));
                temp.setIncorrect_answer(cursor.getString(5));
                results = temp;
        }
        db.close();
        return results;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);

    }

    public void deleteContinue(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_1);

    }

}
