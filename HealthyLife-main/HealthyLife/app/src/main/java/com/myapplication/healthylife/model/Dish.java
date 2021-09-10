package com.myapplication.healthylife.model;

import java.io.Serializable;

public class Dish implements Serializable {
    private int ID;
    private String Name;
    private String description;
    private String tutorial;
    private String note;
    private String ingredients;
    private int image;
    private String video;
    private boolean isCarb;
    private boolean isFat;
    private boolean isVegan;
    private boolean isBreakfast;
    private boolean isLunch;
    private boolean isDinner;

    public Dish(int id, String Name, String description, String tutorial, String note,
                String ingredients, int image, boolean isFat, boolean isCarb, boolean isVegan,
                boolean isBreakfast, boolean isLunch, boolean isDinner) {
        this.ID = id;
        this.Name = Name;
        this.description = description;
        this.tutorial = tutorial;
        this.note = note;
        this.ingredients = ingredients;
        this.image = image;
        this.video = "https://www.youtube.com/results?search_query="+ Name;
        this.isFat = isFat;
        this.isCarb = isCarb;
        this.isVegan = isVegan;
        this.isBreakfast = isBreakfast;
        this.isLunch = isLunch;
        this.isDinner = isDinner;
    }

    public int getID() {
        return ID;
    }


    public String getName() {
        return Name;
    }

    public String getDescription() {
        return description;
    }

    public String getTutorial() {
        return tutorial;
    }

    public String getNote() {
        return note;
    }

    public String getIngredients() {
        return ingredients;
    }

    public int getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public boolean isBreakfast() {
        return isBreakfast;
    }

    public boolean isLunch() {
        return isLunch;
    }

    public boolean isDinner() {
        return isDinner;
    }

    public boolean isCarb() {
        return isCarb;
    }

    public boolean isFat() {
        return isFat;
    }

    public boolean isVegan() {
        return isVegan;
    }
}
