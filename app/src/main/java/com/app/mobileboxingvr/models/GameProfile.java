package com.app.mobileboxingvr.models;

import java.util.List;

public class GameProfile {

    private int strengthLevel;
    private int strengthExp;
    private int staminaLevel;
    private int staminaExp;
    private int health;
    private int damage;
    private int defense;
    private List<Integer> skillTree;
    private String timestamp;

    public GameProfile() {}

    public GameProfile(int strengthExp, int staminaExp, int health, int damage, int defense, String timestamp) {
        this.strengthExp = strengthExp;
        this.staminaExp = staminaExp;
        this.health = health;
        this.damage = damage;
        this.defense = defense;
        this.timestamp = timestamp;
    }

    public void calculateLevel() {
        strengthLevel = strengthExp/100;
        staminaLevel = staminaExp/100;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GameProfile{" +
                "strengthLevel=" + strengthLevel +
                ", strengthExp=" + strengthExp +
                ", staminaLevel=" + staminaLevel +
                ", staminaExp=" + staminaExp +
                ", health=" + health +
                ", damage=" + damage +
                ", defense=" + defense +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
