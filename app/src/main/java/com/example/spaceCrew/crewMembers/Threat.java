package com.example.spaceCrew.crewMembers;

import android.graphics.Color;

import com.example.spaceCrew.utils.ActivityNavigator;

import java.util.Random;

public class Threat {

    private String name;
    private int color;
    private int atk; //attack power
    private int hp; //health points
    private int missionHp;
    private int def; //defense ability points
    private int experience;
    private static int crewMemberCounter = 0;


    //constructor
    public Threat(String name, int def, int atk, int hp, int experience) {
        this.name = name;
        Random r = new Random();
        //Randomize the color of the threat
        int colorR = r.nextInt(0,255);
        int colorG = r.nextInt(0,255);
        int colorB = r.nextInt(0,255);
        this.color = Color.rgb(colorR, colorG, colorB);
        this.atk = atk;
        this.hp = hp;
        this.missionHp = hp;
        this.def = def;
        this.experience = experience;
    }

    public void defend(){


    }

    public int attack(CrewMember crewMember){

        return 0;
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


    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMissionHp() {
        return missionHp;
    }

    public void setMissionHp(int missionHp) {
        this.missionHp = missionHp;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
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
