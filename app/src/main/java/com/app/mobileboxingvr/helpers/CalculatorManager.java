package com.app.mobileboxingvr.helpers;

import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.models.UserActivity;

public class CalculatorManager {

    private UserActivity activity;

    public CalculatorManager(UserActivity activity) {
        this.activity = activity;
    }

    /**
     *  --getStrengthExp--
     *  Calculate activity to Strength Exp
     *  and if it has Strength skill
     *  then add up with bonus exp
     */

    public int getStrengthExp(boolean hasStrengthSkill) {
        double stepCounter = activity.getStepCounter() * MyConstants.EXP_PER_STEP;

        if (hasStrengthSkill) {
            stepCounter += stepCounter * 0.05;
        }

        return (int) Math.round(stepCounter);
    }

    /**
     *  --getStaminaExp--
     *  Calculate activity to Stamina Exp
     *  and if it has Stamina skill
     *  then add up with bonus exp
     */

    public int getStaminaExp(boolean hasStaminaSkill) {
        int stepCounter = activity.getStepCounter();
        int timeSpent = activity.getTimeSpent();

        int stepPerMinute = stepCounter / timeSpent;

        if (hasStaminaSkill) {
            stepPerMinute += stepPerMinute * 0.05;
        }

        return Math.round(stepPerMinute);
    }

    /**
     *  --getAgilityExp--
     *  Calculate activity to Agility Exp
     *  and if it has Agility skill
     *  then add up with bonus exp
     */

    public int getAgilityExp(boolean hasAgilitySkill) {
        double speed = activity.getSpeed() * MyConstants.SPEED_EXP;

        if (hasAgilitySkill) {
            speed += speed * 0.05;
        }

        return (int) Math.round(speed);
    }

    /**
     *  --expToLevel--
     *  Calculate exp to level
     */

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
