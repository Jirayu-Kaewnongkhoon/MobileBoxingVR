package com.app.mobileboxingvr.models;

public class UserActivity {

    private String timestamp;
    private int timeSpent;
    private int stepCounter;

    public UserActivity() {}

    public UserActivity(String timestamp, int timeSpent, int stepCounter) {
        this.timestamp = timestamp;
        this.timeSpent = timeSpent;
        this.stepCounter = stepCounter;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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

    @Override
    public String toString() {
        return "UserActivity{" +
                "timestamp='" + timestamp + '\'' +
                ", timeSpent=" + timeSpent +
                ", stepCounter=" + stepCounter +
                '}';
    }
}
