package me.opkarol.opforts.characters;

import java.io.Serializable;

public final class CharacterExperience implements Serializable {
    private int level;
    private double experience;

    public CharacterExperience(int level, double experience) {
        this.level = level;
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public void increaseLevel() {
        this.experience = 0;
        this.level++;
    }
}
