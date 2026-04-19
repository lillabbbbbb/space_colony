package com.example.spaceCrew.crewMembers;

import android.graphics.Color;
import android.util.Log;

import com.example.spaceCrew.utils.ActivityNavigator;

public class CrewMember {
    private String name;
    private int id;
    private int color;
    private String eyeColor;
    private int atk; //attack power
    private int hp; //health points
    private int missionHp; //temporary health points for while the mission lasts
    private int def; //defense ability points
    private int experience; //can be gained via training and by winning missions;  enhances attack power in a mission
    private String location; //where the CrewMember is currently (Quarters, Simulator or Mission Control)
    private String memberType; //pilot, medic, engineer, soldier or scientist
    private boolean isSelected; //whether the CrewMember is selected by the user for some activity

    //private static ArrayList<CrewMember> crewMembers;
    private static int crewMemberCounter = 0;


    //constructor
    public CrewMember(String name, int colorR, int colorG, int colorB, int def, int atk, int hp, int experience, String memberType) {
        this.name = name;
        this.color = Color.rgb(colorR, colorG, colorB);
        this.atk = atk;
        this.hp = hp;
        this.missionHp = hp;
        this.def = def;
        this.experience = experience;
        this.location = ActivityNavigator.home;
        this.isSelected = false;
        setMemberType(memberType);
        Log.i("TAG", "Member type set to " + this.memberType);
        id = ++crewMemberCounter;
    }

    public void select(){
        isSelected = true;
    }
    public void unSelect(){
        isSelected = false;
    }
    public void defend(){
        /*
        * increases own health points in a mission depending on defense ability points
        * */

    }

    public int attack(CrewMember crewMember){
        /*
         * decreases health points of the opponent in a mission, the number of decrease in points depends on own attack power and experience
         * @param crewMember: the opponent
         * @return the updated (decreased) health points of the opponent
         * */
        return 0;
    }


    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public boolean isSelected() {
        return isSelected;
    }


    //flips the selection
    public void setSelected() {
        this.isSelected = !this.isSelected;
    }

    //sets based on given boolean value
    public void setSelected(boolean bool) {
        this.isSelected = bool;
    }
}
