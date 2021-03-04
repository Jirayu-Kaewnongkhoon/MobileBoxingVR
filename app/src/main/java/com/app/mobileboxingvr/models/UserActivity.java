package com.app.mobileboxingvr.models;

public class UserActivity {

    private long timestamp;
    private int timeSpent;
    private int stepCount;
    private double distance;
    private double speed;

    public UserActivity() {}

    public UserActivity(long timestamp, int timeSpent, int stepCount, double distance, double speed) {
        this.timestamp = timestamp;
        this.timeSpent = timeSpent;
        this.stepCount = stepCount;
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

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
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
                ", stepCount=" + stepCount +
                ", distance=" + distance +
                ", speed=" + speed +
                '}';
    }
}
