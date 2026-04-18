package com.example.spaceCrew.utils;

import android.util.Log;

import com.example.spaceCrew.crewMembers.CrewMember;

import java.util.Arrays;

public class MissionManager {
    /*
     * manages the crewMembers in the Mission Control
     * implemented as a singleton object
     * */

    //global variables
    private static MissionManager instance; //the singleton instance
    private static CrewMember[] crewMembersInMission = new CrewMember[2];
    private static int playerIndex; //the index of the crewMember in the crewMembersInMission list that the player controls
    private static int whoseTurn; //the index of the crewMember who's turn is currenly
    private CrewMember winner;
    private CrewMember loser;
    private static String missionDescription = "";
    private static boolean isMissionInProgress = false;
    private static String playerName = "You";
    private static String compName = "Computer";


    //private constructor
    private MissionManager() {
    }

    public static void addCrewMemberToMission(CrewMember crewMember, int index) {
        crewMembersInMission[index] = crewMember;
    }

    public static void addCrewMemberToMission(CrewMember crewMember){
        if(crewMembersInMission[0] == null){
            crewMembersInMission[0] = crewMember;
        }
        else if(crewMembersInMission[1] == null){
            crewMembersInMission[1] = crewMember;
        }
        else{
            crewMembersInMission[0] = crewMember;
        }
    }



    public static void startMission(){
        //if-check in case the startMission() method is called inappropriately
        if(crewMembersInMission[0] == null && crewMembersInMission[1] == null) {
            return;
        }
        //randomize the player's crewMember, and who starts the game out of the two
        playerIndex = assign0or1();
        missionDescription = "";
        missionDescription += "You are: " + crewMembersInMission[playerIndex].getName();

        whoseTurn = assign0or1();

        String text = playerIndex == whoseTurn ? "yours" : "the computer's";
        missionDescription += "\n" + "The first attack is " + text;


        Arrays.stream(crewMembersInMission)
                .forEach(l -> l.setLocation(ActivityNavigator.missionControl));

        //(re)set mission HP to original HP
        crewMembersInMission[0].setMissionHp(crewMembersInMission[0].getHp());
        crewMembersInMission[1].setMissionHp(crewMembersInMission[1].getHp());
        Log.i("TAG", "Mission HPs set.");

        isMissionInProgress = true;

        //if the computer gets to attack first
        if(whoseTurn == turn(playerIndex)){
            computerAttack();
        }

    }

    public static void endMission() {
        /*
         * this method is called when the player decides to call it a day and end the mission before it would normally end
         * */
        isMissionInProgress = false;

        //send crewMembers home
        Arrays.stream(crewMembersInMission)
                .forEach(l -> l.setLocation(ActivityNavigator.home));


        //(re)set mission HP to original HP
        crewMembersInMission[0].setMissionHp(crewMembersInMission[0].getHp());
        crewMembersInMission[1].setMissionHp(crewMembersInMission[1].getHp());
    }

    public static int assign0or1() {
        return (int) (Math.random() * 2); //returns either 0 or 1. From ChatGPT
    }

    //ChatGPT was used to help with the ternary operator syntax..
    public static int turn(int i) {
        return (i == 0) ? 1 : 0;
    }

    public static boolean areBothAlive(){
        Log.d("DEBUG", String.valueOf(crewMembersInMission[0].getMissionHp()));
        Log.d("DEBUG", String.valueOf(crewMembersInMission[1].getMissionHp()));
        return crewMembersInMission[0].getMissionHp() > 0 && crewMembersInMission[1].getMissionHp() > 0;
    }



    public static void playerAttack(){

        int attackerIndex= playerIndex;

        attack(attackerIndex, playerName, compName);

        if(crewMembersInMission[turn(playerIndex)].getMissionHp() <= 0){
            missionDescription += "\n" + crewMembersInMission[turn(playerIndex)].getName() + "'s (" + compName + ") health drops to zero.\nThe mission has ended.\nYou won!";

            int extraXp = 60;
            crewMembersInMission[playerIndex].addXp(extraXp);
            missionDescription += "\n" + "You have gained +"+ extraXp + "XP.";

            CrewMemberStatistics.addWin(crewMembersInMission[playerIndex]);
        }

    }

    public static void computerAttack(){

        int attackerIndex= turn(playerIndex);

        attack(attackerIndex, compName, playerName);

        if(crewMembersInMission[playerIndex].getMissionHp() <= 0){
            missionDescription += "\n" + crewMembersInMission[turn(attackerIndex)].getName() + "'s (" + playerName + ") health drops to zero.\nYou died.\nThe mission has ended.";

            CrewMemberStatistics.addMission(crewMembersInMission[playerIndex]);
        }

    }

    private static void attack(int attackerIndex, String attackerName, String defendantName){
        int atk = crewMembersInMission[attackerIndex].getAtk();
        int def = crewMembersInMission[attackerIndex].getDef();
        int xp = crewMembersInMission[attackerIndex].getXp();
        int hp = crewMembersInMission[attackerIndex].getHp();
        int missionHp = crewMembersInMission[attackerIndex].getMissionHp();

        int attackPower = calcAttackPower(atk, missionHp, hp, xp);

        missionDescription += "\n" + attackerName +  "(" + crewMembersInMission[attackerIndex].getName() + ") attacks " + crewMembersInMission[turn(attackerIndex)].getName() + " (" + defendantName + ").";

        int damageToPlayer = calculateDamageTaken(attackPower, crewMembersInMission[turn(attackerIndex)].getDef());

        //reduce player's health
        crewMembersInMission[turn(attackerIndex)].setMissionHp(damageToPlayer);

        if(crewMembersInMission[turn(attackerIndex)].getMissionHp() > 0){
            missionDescription += "\n" + crewMembersInMission[turn(attackerIndex)].getName() + " (" + defendantName + ") manages to escape death, with remaining of " + crewMembersInMission[turn(attackerIndex)].getMissionHp() + "/" + crewMembersInMission[turn(attackerIndex)].getHp() + "HP.";
            whoseTurn = turn(whoseTurn);
        }
        else{
            isMissionInProgress = false;
        }
    }

    //Below method inspired by ChatGPT
    public static int calcAttackPower(int baseAttack, int health, int maxHealth, int xp) {
        // 1. Health factor: more health → full attack, less health → weaker attack
        double healthFactor = (double) health / maxHealth; // between 0 and 1

        // 2. XP factor: higher XP → stronger attack
        double xpFactor = 1 + (xp / 50.0); // every 50 XP adds +1 multiplier

        // 3. Final attack power
        int attackPower = (int) (baseAttack * healthFactor * xpFactor);

        return attackPower;
    }

    //Below method entirely credited to ChatGPT
    public static int calculateDamageTaken(int attackPower, int defense) {
        // 1. Defense factor: higher defense reduces damage
        double defenseFactor = 1 - (defense / 100.0); // e.g., 20 defense = 0.8 multiplier

        // Cap the defense factor to avoid negative or zero damage
        if (defenseFactor < 0.1) {
            defenseFactor = 0.1; // at least 10% damage gets through
        }

        // 2. Base damage after defense
        int damage = (int) (attackPower * defenseFactor);

        // 3. Optional: random variation ±10%
        double variation = 0.9 + Math.random() * 0.2; // random between 0.9 and 1.1
        damage = (int) (damage * variation);

        return damage;
    }

    //Below method is inspired by ChatGPT
    public static int calculateXpGain(int playerXp, int playerAttack, int opponentXp, int opponentAttack) {
        // Base XP is a fraction of opponent's XP
        double baseXp = opponentXp * 0.5;

        // Bonus for stronger opponents
        double attackRatio = (double) opponentAttack / playerAttack;
        double bonusXp = baseXp * attackRatio;

        // Level adjustment (optional)
        double xpDiff = playerXp - opponentXp; // assume XP/100 ≈ opponent level
        if(xpDiff > 1000) {
            bonusXp *= 0.5; // reduce XP if opponent is much weaker
        }

        int totalXp = (int) Math.round(baseXp + bonusXp);
        return Math.max(totalXp, 1); // ensure at least 1 XP is awarded
    }


    //getters
    public static int getPlayerIndex(){
        return playerIndex;
    }

    public static int getWhoseTurn(){
        return whoseTurn;
    }

    public static CrewMember[] getCrewMembersInMission(){
        return crewMembersInMission;
    }

    public static boolean getIsMissionInProgress(){
        return isMissionInProgress;
    }
    public static String getMissionDescription(){
        return missionDescription;
    }

}