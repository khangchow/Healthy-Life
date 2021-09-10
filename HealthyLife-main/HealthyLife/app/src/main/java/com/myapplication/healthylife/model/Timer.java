package com.myapplication.healthylife.model;

public class Timer {
    private String name;
    private String status;
    private long time;
    private int video;

    public Timer(String name, String status, long time, int video) {
        this.name = name;
        this.status = status;
        this.time = time;
        this.video = video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }
}
