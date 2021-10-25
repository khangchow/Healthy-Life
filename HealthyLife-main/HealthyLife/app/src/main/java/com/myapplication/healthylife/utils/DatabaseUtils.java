package com.myapplication.healthylife.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;
import com.myapplication.healthylife.model.Exercise;

import java.util.ArrayList;

public class DatabaseUtils extends Application {
    private static DatabaseHelper db;

    public static void getDatabase(Context context) {
        if(db == null)  {
            db = new DatabaseHelper(context);
        }
    }

    public static void saveListofDietForNewUser(ArrayList<Diet> diets, double bmi){
        boolean startRecommended = false;
        ArrayList<Diet> result = new ArrayList<>();
        int type;
        if (bmi >= 35) {
            type = 5;
        }else if(bmi >= 30 && bmi <= 34.9) {
            type = 4;
        }else if(bmi >= 25 && bmi <= 29.9)  {
            type = 3;
        }else if(bmi >= 18.5 && bmi <= 24.9)    {
            type = 2;
        }else   {
            type = 1;
        }
        Log.d("DATA", String.valueOf(type));
        for (Diet d: diets)    {
            for (int i: d.getTypes())  {
                if (i == type && !startRecommended)  {
                    d.setRecommended(true);
                    Log.d("REC", d.getName()+" "+d.isRecommended());
                    result.add(d);
                    startRecommended = true;
                    break;
                }else if(i == type && startRecommended) {
                    d.setRecommended(true);
                    Log.d("REC", d.getName()+" "+d.isRecommended());
                    result.add(d);
                    break;
                }
            }
        }
        for (Diet d:result){
            db.addDiet(d);
        }
    }

    public static void saveListofDishForNewUser(ArrayList<Dish> Dishes){

        for (Dish d:Dishes){
            db.addDish(d);
        }
    }

    public static void removeExercises()    {
        db.deleteAllExercises();
    }

    public static void addExercise(Exercise ex)   {
        db.addExercise(ex);
    }

    public static ArrayList<Exercise> getExerciseList()    {
        return db.getExerciseList();
    }
}
