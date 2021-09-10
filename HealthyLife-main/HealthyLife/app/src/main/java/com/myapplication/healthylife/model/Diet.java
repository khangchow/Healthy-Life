package com.myapplication.healthylife.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Diet implements Serializable {
    private int ID;
    private String Name;
    private String Description;
    private String Note;
    private int Calories;
    private int[] Types;
    private boolean isAssigned;
    private boolean isRecommended;
    private boolean isFatAllowed;
    private boolean isCarbAllowed;
    private boolean isVegan;
    private int image;
    private ArrayList<Dish> breakfast;
    private ArrayList<Dish> lunch;
    private ArrayList<Dish> dinner;



    public Diet (int ID, String Name, String Description, String Note, int Calories,int[] Types,
                 boolean isAssigned, boolean isCarbAllowed, boolean isFatAllowed, boolean isVegan, int image){
        this.isRecommended=false;
        this.ID=ID;
        this.Name=Name;
        this.Description=Description;
        this.Note=Note;
        this.Calories = Calories;
        this.Types = Types;
        this.isCarbAllowed=isCarbAllowed;
        this.isFatAllowed=isFatAllowed;
        this.isVegan=isVegan;
        this.isAssigned=isAssigned;
        this.isRecommended=false;
        this.image = image;
        this.breakfast=new ArrayList<>();
        this.lunch=new ArrayList<>();
        this.dinner=new ArrayList<>();
    }

    public Diet (int ID, String Name, String Description, String Note, int Calories,int[] Types,
                 boolean isAssigned, boolean isCarbAllowed, boolean isFatAllowed, boolean isVegan,boolean isRecommended, int image){
        this.isRecommended=false;
        this.ID=ID;
        this.Name=Name;
        this.Description=Description;
        this.Note=Note;
        this.Calories = Calories;
        this.Types = Types;
        this.isCarbAllowed=isCarbAllowed;
        this.isFatAllowed=isFatAllowed;
        this.isVegan=isVegan;
        this.isAssigned=isAssigned;
        this.isRecommended=isRecommended;
        this.image = image;
        this.breakfast=new ArrayList<>();
        this.lunch=new ArrayList<>();
        this.dinner=new ArrayList<>();
    }
    public String getNote() {
        return Note;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public int getID() {
        return ID;
    }

    public int getCalories() {
        return Calories;
    }

    public int[] getTypes() {
        return Types;
    }


    public boolean isAssigned() {
        return isAssigned;
    }
    public void setAssigned(boolean temp) {this.isAssigned = temp; }

    public boolean isVegan() {
        return isVegan;
    }

    public boolean isCarbAllowed() {
        return isCarbAllowed;
    }

    public boolean isFatAllowed() {
        return isFatAllowed;
    }

    public boolean isRecommended() {
        return isRecommended;
    }
    public void setRecommended(boolean isRecommended){this.isRecommended=isRecommended;}
    public int getImage() {
        return image;
    }
    public void insertBreakfast (Dish dish){
        breakfast.add(dish);
    }
    public void insertLunch (Dish dish){
        lunch.add(dish);
    }
    public void insertDinner (Dish dish){
        dinner.add(dish);
    }

    public ArrayList<Dish> getBreakfast() {
        return breakfast;
    }

    public ArrayList<Dish> getLunch() {
        return lunch;
    }

    public ArrayList<Dish> getDinner() {
        return dinner;
    }
}


