package com.myapplication.healthylife.utils;

import android.util.Log;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;

import java.util.ArrayList;

public class DietUtils {
    public static ArrayList<Diet> initDietList() {
        ArrayList<Diet> diets = new ArrayList<>();

        diets.add(new Diet(-1, "Low-carb Diet",
                "Diets with restriction on carbohydrate-rich products. The primary " +
                        "aim of the diet is to force your body to use more fats for fuel " +
                        "instead of using carbs as a main source of energy.",
                "In extremely rare cases, low-carb diets can cause a serious condition " +
                        "called nondiabetic ketoacidosis. This condition seems to be more " +
                        "common in lactating women and can be fatal if left untreated.",
                1800,new int[]{2, 3, 4, 5},false,false,
                true, false, R.drawable.eggnveg));

        diets.add(new Diet(-1,"Vegan Diet","A vegan diet excludes all " +
                "animal products.","Vegan diets is effective at helping people naturally " +
                "reduce the amount of calories they eat, resulting in weight loss.However," +
                "Vegans may be at an increased risk of certain nutrient deficiencies.",
                1500, new int[]{2,3,4},false,true,
                true, true, R.drawable.peasntofu));

        diets.add(new Diet(-1,"3k Diet", "A diet to gain weight for " +
                "underweight people","The menu shown later is only cover 75% amount of " +
                "calories needed. Keep exercising for balance, or you will be overwhelmed " +
                "by the calories taken in.", 3000, new int[]{1}, false,
                true,true, false, R.drawable.porknveg));

        return diets;
    }

    public static void saveListofDietForNewUser(double bmi) {
        Log.d("DIET", "DIET");
        boolean startRecommended = false;
        ArrayList<Diet> diets = initDietList();
        Log.d("DIET", diets.toString());
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

        for (Diet d : diets) {
            for (int i : d.getTypes()) {
                if (i == type && !startRecommended) {
                    d.setRecommended(true);
                    result.add(d);
                    startRecommended = true;
                    break;
                } else if (i == type && startRecommended) {
                    d.setRecommended(true);
                    result.add(d);
                    break;
                }
            }
        }
        for (Diet d : result) {
            Log.d("DIET", d.toString());
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