package com.app.mobileboxingvr.helpers;

import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.models.UserActivity;

public class CalculatorManager {

    private UserActivity activity;

    public CalculatorManager(UserActivity activity) {
        this.activity = activity;
    }

    public int getStrengthExp() {
        int stepCounter = activity.getStepCounter();

        return (int) Math.round(stepCounter * MyConstants.EXP_PER_STEP);
    }

    public int getStaminaExp() {
        int stepCounter = activity.getStepCounter();
        int timeSpent = activity.getTimeSpent();

        return stepCounter / timeSpent;
    }

    public int getAgilityExp() {
        return (int) Math.round(activity.getSpeed() * MyConstants.SPEED_EXP);
    }

    public ExpAndLevel expToLevel(int totalExp) {
        int level = totalExp / MyConstants.MAX_EXP;
        int remainingExp = totalExp % MyConstants.MAX_EXP;

        return new ExpAndLevel(level, remainingExp);
    }

    public static class ExpAndLevel {
        private int level;
        private int exp;

        public ExpAndLevel(int level, int exp) {
            this.level = level;
            this.exp = exp;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getExp() {
            return exp;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }
    }
}
