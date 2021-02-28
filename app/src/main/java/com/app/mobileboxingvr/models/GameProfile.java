package com.app.mobileboxingvr.models;

import java.util.List;

public class GameProfile {

    private int strengthLevel;
    private int strengthExp;
    private int staminaLevel;
    private int staminaExp;
    private int agilityLevel;
    private int agilityExp;
    private int health;
    private int damage;
    private int defense;
    private String playerName;
    private int playerLevel;
    private int playerExp;
    private List<Skill> skills;
    private long timestamp;

    public GameProfile() {}

    public void initializeStatus() {
        health = 100;
        damage = 5;
        defense = 2;
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }

    public void setStrengthLevel(int strengthLevel) {
        this.strengthLevel = strengthLevel;
    }

    public int getStrengthExp() {
        return strengthExp;
    }

    public void setStrengthExp(int strengthExp) {
        this.strengthExp = strengthExp;
    }

    public int getStaminaLevel() {
        return staminaLevel;
    }

    public void setStaminaLevel(int staminaLevel) {
        this.staminaLevel = staminaLevel;
    }

    public int getStaminaExp() {
        return staminaExp;
    }

    public void setStaminaExp(int staminaExp) {
        this.staminaExp = staminaExp;
    }

    public int getAgilityLevel() {
        return agilityLevel;
    }

    public void setAgilityLevel(int agilityLevel) {
        this.agilityLevel = agilityLevel;
    }

    public int getAgilityExp() {
        return agilityExp;
    }

    public void setAgilityExp(int agilityExp) {
        this.agilityExp = agilityExp;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getPlayerExp() {
        return playerExp;
    }

    public void setPlayerExp(int playerExp) {
        this.playerExp = playerExp;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GameProfile{" +
                "strengthLevel=" + strengthLevel +
                ", strengthExp=" + strengthExp +
                ", staminaLevel=" + staminaLevel +
                ", staminaExp=" + staminaExp +
                ", agilityLevel=" + agilityLevel +
                ", agilityExp=" + agilityExp +
                ", health=" + health +
                ", damage=" + damage +
                ", defense=" + defense +
                ", playerName=" + playerName +
                ", playerLevel=" + playerLevel +
                ", playerExp=" + playerExp +
                ", skills=" + skills +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public static class Skill {
        private int skillId;
        private long timestamp;

        public Skill() {}

        public int getSkillId() {
            return skillId;
        }

        public void setSkillId(int skillId) {
            this.skillId = skillId;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "Skill{" +
                    "skillId=" + skillId +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
}
