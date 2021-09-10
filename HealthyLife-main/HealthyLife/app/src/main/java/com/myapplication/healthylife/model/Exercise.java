package com.myapplication.healthylife.model;

import java.io.Serializable;


public class Exercise implements Serializable {
    private int id;
    private String name;
    private String level;
    private int duration;
    private int progress;
    private int image;
    private boolean isFinished;
    private boolean isRecommended;
    private boolean isOthers;
    private boolean isFirst;
    private int[] types;
    private int video;
    private String description;
    private String tutorial;
    private String equipment;
    private int durationSet;
    private int breakSet;
    private int numSet;
    private int breakEx;
    private int caloSet;

    public Exercise(int id, String name, String level, int duration, int image, int[] types, int video, String description, String tutorial, String equipment, int durationSet, int breakSet, int numSet, int breakEx, int caloSet) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.duration = duration;
        this.progress = 0;
        this.image = image;
        this.isFinished = false;
        this.isRecommended = false;
        this.isOthers = false;
        this.isFirst = false;
        this.types = types;
        this.video = video;
        this.description = description;
        this.tutorial = tutorial;
        this.equipment = equipment;
        this.durationSet=durationSet;
        this.breakSet=breakSet;
        this.numSet=numSet;
        this.breakEx=breakEx;
        this.caloSet=caloSet;
    }

    public Exercise(int id, String name, String level, int duration, int progress, int image, boolean isFinished, boolean isRecommended, boolean isOthers, boolean isFirst, int[] types, int video, String description, String tutorial, String equipment, int durationSet, int breakSet, int numSet, int breakEx, int caloSet) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.duration = duration;
        this.progress = progress;
        this.image = image;
        this.isFinished = isFinished;
        this.isRecommended = isRecommended;
        this.isOthers = isOthers;
        this.isFirst = isFirst;
        this.types = types;
        this.video = video;
        this.description = description;
        this.tutorial = tutorial;
        this.equipment = equipment;
        this.durationSet=durationSet;
        this.breakSet=breakSet;
        this.numSet=numSet;
        this.breakEx=breakEx;
        this.caloSet=caloSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public boolean isOthers() {
        return isOthers;
    }

    public void setOthers(boolean others) {
        isOthers = others;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public int[] getTypes() {
        return types;
    }

    public void setTypes(int[] types) {
        this.types = types;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) { this.tutorial = tutorial; }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getDurationSet() {
        return durationSet;
    }

    public void setDurationSet(int durationSet) { this.durationSet=durationSet; }

    public int getbreakEx() {
        return breakEx;
    }

    public void setbreakEx(int breakEx) {
        this.breakEx = breakEx;
    }

    public int getnumSet() {
        return numSet;
    }

    public void setnumSet(int numSet) {
        this.numSet = numSet;
    }

    public int getbreakSet() {return breakSet;}

    public void setbreakSet(int breakSet) {
        this.breakSet = breakSet;
    }

    public int getcaloSet() {
        return caloSet;
    }

    public void setcaloSet(int caloSet) {
        this.caloSet = caloSet;
    }

    public String convertSecToMin()    {
        if(this.duration >= 60)   {
            int m = this.duration/60;
            int s = this.duration-m*60;
            return m + "m" + s + "s";
        }else   {
            return this.duration+"s";
        }
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", progress=" + progress +
                ", isFinished=" + isFinished +
                '}';
    }
}
