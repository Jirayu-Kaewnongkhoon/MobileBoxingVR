package com.app.mobileboxingvr.models;

public class GameProfile {

    private double strength;
    private double stamina;
    private double health;
    private String timestamp;

    public GameProfile() {}

    public GameProfile(double strength, double stamina, double health, String timestamp) {
        this.strength = strength;
        this.stamina = stamina;
        this.health = health;
        this.timestamp = timestamp;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
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
                "strength=" + strength +
                ", stamina=" + stamina +
                ", health=" + health +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
