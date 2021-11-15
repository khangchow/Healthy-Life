package com.myapplication.healthylife.utils;

import android.util.Log;

import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;

import java.util.ArrayList;

public class DietUtils {
    public static void saveListofDietForNewUser(ArrayList<Diet> diets, double bmi) {
        boolean startRecommended = false;
        ArrayList<Diet> result = new ArrayList<>();
        int type;
        if (bmi >= 35) {
            type = 5;
        } else if (bmi >= 30 && bmi <= 34.9) {
            type = 4;
        } else if (bmi >= 25 && bmi <= 29.9) {
            type = 3;
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            type = 2;
        } else {
            type = 1;
        }
        Log.d("DATA", String.valueOf(type));
        for (Diet d : diets) {
            for (int i : d.getTypes()) {
                if (i == type && !startRecommended) {
                    d.setRecommended(true);
                    Log.d("REC", d.getName() + " " + d.isRecommended());
                    result.add(d);
                    startRecommended = true;
                    break;
                } else if (i == type && startRecommended) {
                    d.setRecommended(true);
                    Log.d("REC", d.getName() + " " + d.isRecommended());
                    result.add(d);
                    break;
                }
            }
        }
        for (Diet d : result) {
            DatabaseUtils.addDiet(d);
        }
    }

    public static Boolean editAssignedDiet(Diet diet) {
        return DatabaseUtils.editAssignedDiet(diet);
    }

    public static ArrayList<Diet> getDietList() {
        return DatabaseUtils.getDietList();
    }

    public static void removeDiets() {
        DatabaseUtils.removeDiets();
    }

    public static void addDiet(Diet diet) {
        DatabaseUtils.addDiet(diet);
    }
}