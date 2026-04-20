package com.example.spaceCrew.crewMembers;

import android.graphics.Color;

import com.example.spaceCrew.utils.ActivityNavigator;

import java.util.Random;

public class Threat {

    private String name;
    private int color;
    private int skill; //attack power
    private int energy; //health points
    private int missionEnergy;
    private int res; //resense ability points
    private int experience;


    //constructor
    public Threat(String name, int res, int skill, int energy, int experience) {
        this.name = name;
        Random r = new Random();
        //Randomize the color of the threat
        int colorR = r.nextInt(0,255);
        int colorG = r.nextInt(0,255);
        int colorB = r.nextInt(0,255);
        this.color = Color.rgb(colorR, colorG, colorB);
        this.skill = skill;
        this.energy = energy;
        this.missionEnergy = energy;
        this.res = res;
        this.experience = experience;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public int getskill() {
        return skill;
    }

    public void setskill(int skill) {
        this.skill = skill;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getMissionEnergy() {
        return missionEnergy;
    }

    public void setMissionEnergy(int missionEnergy) {
        this.missionEnergy = missionEnergy;
    }

    public int getResilience() {
        return res;
    }

    public void setResilience(int res) {
        this.res = res;
    }

    public int getXp() {
        return experience;
    }

    public void addXp(int xp){
        this.experience += xp;
    }

    public void setXp(int experience) {
        this.experience = experience;
    }

}
