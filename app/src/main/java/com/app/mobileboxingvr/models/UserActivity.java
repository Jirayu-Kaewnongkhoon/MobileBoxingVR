package com.app.mobileboxingvr.models;

public class UserActivity {

    private long timestamp;
    private int timeSpent;
    private int stepCounter;
    private double distance;
    private double speed;

    public UserActivity() {}

    public UserActivity(long timestamp, int timeSpent, int stepCounter, double distance, double speed) {
        this.timestamp = timestamp;
        this.timeSpent = timeSpent;
        this.stepCounter = stepCounter;
        this.distance = distance;
        this.speed = speed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "timestamp='" + timestamp + '\'' +
                ", timeSpent=" + timeSpent +
                ", stepCounter=" + stepCounter +
                ", distance=" + distance +
                ", speed=" + speed +
                '}';
    }
}
