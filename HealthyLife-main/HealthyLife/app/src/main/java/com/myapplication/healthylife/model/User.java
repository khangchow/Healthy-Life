package com.myapplication.healthylife.model;

public class User {
    private String name;
    private float height;
    private float weight;
    private double bmi;
    private int caloFitness;
    private int caloDiet;

    public User(String name, float height, float weight) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        bmi = 0;
        caloFitness = 0;
        caloDiet = 0;
    }

    public int getCaloFitness() {
        return caloFitness;
    }

    public void setCaloFitness(int caloFitness) {
        this.caloFitness = caloFitness;
    }

    public int getCaloDiet() {
        return caloDiet;
    }

    public void setCaloDiet(int caloDiet) {
        this.caloDiet = caloDiet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
}
