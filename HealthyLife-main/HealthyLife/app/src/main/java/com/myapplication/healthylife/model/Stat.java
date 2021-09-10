package com.myapplication.healthylife.model;

import java.util.Objects;

public class Stat {
    private int id;
    private float height;
    private float weight;
    private double bmi;
    private String date;

    public Stat(int id, float height, float weight, double bmi, String date) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stat stat = (Stat) o;
        return id == stat.id && Float.compare(stat.height, height) == 0 && Float.compare(stat.weight, weight) == 0 && Double.compare(stat.bmi, bmi) == 0 && Objects.equals(date, stat.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, height, weight, bmi, date);
    }
}
