package com.myapplication.healthylife.utils;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.model.Dish;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DishUtils {
    public static ArrayList<Dish> initDishList() {
        ArrayList<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish(-1, "Bacon and egg"," No Description",
                "Fried Bacon and egg in medium heat in 3 mins, until it is done",
                "Eat with green veggies for fiber + vitamins","2-3 eggs + the amount of bacon you want",
                R.drawable.eggnbacon,true, false, false, true,false, false));
        dishes.add(new Dish(-1, "Avocado salad"," No Description",
                "Prepare and wash lettuce, avocado, bell chillies, cucumber. Mix them all together with  Vinaigrette",
                "Non-vegan can top up with eggs  for diversity","Lettuce,cucumber, avocados, bell chillies, Vinaigrette  ",
                R.drawable.avocadosalad,false, false, true, false,false, true));
        dishes.add(new Dish(-1, "Nuts milk with chia seed"," No Description",
                "Use any types of nuts milk such as almond milk, soy milk, walnuts milk... and drop 1 tablespoon of chia seed in",
                "Eat with fruits(apples, strawberries) if you like","Chia Seed, nuts milk"  ,
                R.drawable.milknchia,true, false, true, false,true, true));
        dishes.add(new Dish(-1, "Green pea with tofu/beef  ","No Description ",
                "Wash green pea, season tofu/beef with garlics, soy bean. After stir frying tofu/beef for 2-3 min with medium/high flame, add the green peas, continue stirring for 5 min and turr off the heat.",
                "Non-vegan can cook and eat with beef, Vegan can do the same with tofu, people with no restriction in carb can eat with rice",
                "Green peas, tofu/beef, soy sauce, garlic, ",
                R.drawable.peasntofu,true, false, true, false,false,true));
        dishes.add(new Dish(-1, "Omelet with veggies","No Description ",
                "Break eggs, fry with olive oil for few mins. Meanwhile prepare boiled veggies (carrot,raddish, brocoli) ",
                "No note","Eggs, vegetables ( carrot/brocoli/etc...) ",
                R.drawable.eggnveg,true, false, false, true,false, false));
        dishes.add(new Dish(-1, "(Nuts) Yogurt with strawberries and nuts","No Description ",
                "Just mix the yogurt with berries and nuts (any type you want)",
                "Non-vegan can have cow milk yogurt, Vegan can alternate with nuts yogurt","Yogurt(nuts or normal), strawberries/blueberries, nuts(almond, walnuts...) ",
                R.drawable.yogurtstrawberries,true, false,true, true,true, false));
        dishes.add(new Dish(-1, "Pork chop with veggies  ","No Description ",
                "wash pork and boil it with salt (1 coffee spoon) until bubbles come up, after that countinue doing so for 15 min(or until the red part inside is gone).Meanwhile wash any kind of green veggies (cabbage, amaranth,brocoli...) and boil it after finishing the meat.",
                "No note","Pork, favorite veggies",
                R.drawable.porknveg,true, false, false, false,true, true));
        return dishes;
    }

    public static void saveListofDishForNewUser(){
        ArrayList<Dish> dishes = initDishList();
        for (Dish d:dishes){
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
