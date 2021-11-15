package com.myapplication.healthylife.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;
import com.myapplication.healthylife.model.Exercise;
import com.myapplication.healthylife.model.Stat;

import java.util.ArrayList;

public class DatabaseUtils extends Application {
    private static DatabaseHelper db;

    public static void getDatabase(Context context) {
        if(db == null)  {
            db = new DatabaseHelper(context);
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

    public static void removeDiets()    {
        db.deleteAllExercises();
    }

    public static void addDiet(Diet diet)   {
        db.addDiet(diet);
    }

    public static ArrayList<Diet> getDietList()    {
        return db.getDietList();
    }

    public static void removeDishes()    {
        db.deleteAllDishes();
    }

    public static void addDish(Dish dish)   {
        db.addDish(dish);
    }

    public static ArrayList<Dish> getDishList()    {
        return db.getDishList();
    }

    public static void removeStats()    {
        db.deleteAllStat();
    }

    public static void addStat(Stat stat)   {
        db.addStat(stat);
    }

    public static ArrayList<Stat> getStatList()    {
        return db.getStatList();
    }

    public static Boolean editAssignedDiet(Diet diet) {
        return db.editAssignedDiet(diet);
    }
}
