package com.myapplication.healthylife.utils;

import com.myapplication.healthylife.model.Dish;

import java.util.ArrayList;

public class DishUtils {
    public static void saveListofDishForNewUser(ArrayList<Dish> Dishes){
        for (Dish d:Dishes){
            DatabaseUtils.addDish(d);
        }
    }

    public static void removeDishes()    {
        DatabaseUtils.removeDishes();
    }

    public static void addDish(Dish dish)   {
        DatabaseUtils.addDish(dish);
    }

    public static ArrayList<Dish> getDishList()    {
        return DatabaseUtils.getDishList();
    }
}
