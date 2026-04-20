package com.example.spaceCrew.utils;

import android.util.Log;

import com.example.spaceCrew.R;
import com.example.spaceCrew.crewMembers.CrewMember;
import com.example.spaceCrew.crewMembers.Threat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MissionManager {
    /*
     * manages the crewMembers in the Mission Control
     * implemented as a singleton object
     * */

    //global variables
    private static MissionManager instance; //the singleton instance
    private static CrewMember[] crewMembersInMission = new CrewMember[2];
    private static Threat threat;
    private static int playerIndex; //the index of the crewMember in the crewMembersInMission list that the player controls
    private static boolean isThreatTurn;
    private boolean isCrewVictory;
    private CrewMember loser;
    private static String missionDescription = "";
    private static boolean isMissionInProgress = false;
    private static String playerName = "You";
    private static final String ASTEROID_STORM = "Asteroid Storm";
    private static final String ALIEN_ATTACK = "Alien Attack";
    private static final String KITCHEN_FIRE = "Fire in the Kitchen";
    private static final String FUEL_LEAKAGE = "Fuel Leakage";
    private static final String SOLAR_FLARES = "Solar Flares";


    //private constructor, so other classes do not access it
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


    public static void generateThreat() {

        //decide on type of threat based on the players on mission
        ArrayList<String> threatOptions = new ArrayList<>();
        ArrayList<String> selectedTypes = (ArrayList<String>) Arrays.stream(crewMembersInMission).map(m -> m.getMemberType()).collect(Collectors.toList());;

        //select possible threats depending on the specializations of the crew members in the mission
        if(selectedTypes.contains(CrewMemberManager.SOLDIER)){
            threatOptions.add(MissionManager.ASTEROID_STORM);
            threatOptions.add(MissionManager.ALIEN_ATTACK);
        }
        if(selectedTypes.contains(CrewMemberManager.MEDIC)){
            threatOptions.add(MissionManager.KITCHEN_FIRE);
        }
        if(selectedTypes.contains(CrewMemberManager.ENGINEER)){
            threatOptions.add(MissionManager.FUEL_LEAKAGE);
        }
        if(selectedTypes.contains(CrewMemberManager.SCIENTIST)){
            threatOptions.add(MissionManager.SOLAR_FLARES);
        }
        if(selectedTypes.contains(CrewMemberManager.PILOT)){
            threatOptions.add(MissionManager.ASTEROID_STORM);
            threatOptions.add(MissionManager.FUEL_LEAKAGE);
        }

        //Choose a threat randomly from the given options
        Random r = new Random();
        int chosenThreatIndex = (int)(Math.random() * threatOptions.size());

        //init threat stats
        double res = 0;
        double skill = 0;
        double energy = 0;
        double xp = 0;

        // ---- Calculate Averages ----
        double avgSkill = Arrays.stream(crewMembersInMission)
                .mapToInt(CrewMember::getskill)
                .average()
                .orElse(0.0);
        double avgRes = Arrays.stream(crewMembersInMission)
                .mapToInt(CrewMember::getResilience)
                .average()
                .orElse(0.0);
        double avgEnergy = Arrays.stream(crewMembersInMission)
                .mapToInt(CrewMember::getEnergy)
                .average()
                .orElse(0.0);
        double avgXP = Arrays.stream(crewMembersInMission)
                .mapToInt(CrewMember::getXp)
                .average()
                .orElse(0.0);

        // ---- Calculate Team Power ----
        double teamPower =
                (avgSkill * crewMembersInMission.length) +
                        (avgRes * 1.5) +
                        (avgEnergy * 1.2) +
                        (avgXP * crewMembersInMission.length)
                                /crewMembersInMission.length;



        // ---- Scale threat ----
        double threatPower = (teamPower / crewMembersInMission.length) * calculateScalingFactor(avgSkill);

        // ---- Core Stats ----
        energy = threatPower * 3;        // HP pool
        skill = (threatPower * 1.1);  // single attack value

        // ---- Balance rules ----

        // Prevent instant kill players
        double playerTankiness = avgRes * 10;
        if (skill > playerTankiness * 0.5) {
            skill *= 0.85;
        }

        // Early game safety (XP = 0)
        if (avgXP == 0) {
            skill *= 0.7;
            energy *= 0.8;
        }

        //create threat
        //cast all abilities to int
        threat = new Threat(threatOptions.get(chosenThreatIndex), (int)res, (int)skill, (int)energy, (int)xp);

    }

    public static double calculateScalingFactor(double avgSkill){
        double maxSkill = Arrays.stream(crewMembersInMission)
                .mapToInt(CrewMember::getskill)
                .max()
                .orElse(0);

        //Scale based on players' skills
        return
                Math.sqrt(
                        0.75 +
                                (avgSkill / 10.0) * 0.45 +
                                (maxSkill / 10.0) * 0.15
                );

    }

    public static int getThreatImageResource() {
        int resId = 0;

        switch (threat.getName()){
            case MissionManager.ASTEROID_STORM: resId = R.drawable.asteroid; break;
            case MissionManager.KITCHEN_FIRE: resId = R.drawable.kitchen_fire; break;
            case MissionManager.FUEL_LEAKAGE: resId = R.drawable.fuel_leakage; break;
            case MissionManager.SOLAR_FLARES: resId = R.drawable.solar_flame; break;
            case MissionManager.ALIEN_ATTACK: resId = R.drawable.alien_attack; break;
            default: resId = R.drawable.kitchen_fire; break;
        }
        Log.i("TAG", String.valueOf(resId));
        return resId;
    }

    public static void startMission(){
        //if-check in case the startMission() method is called inappropriately
        if(crewMembersInMission[0] == null && crewMembersInMission[1] == null) {
            return;
        }

        //officially set the location of these crew members to the mission control
        Arrays.stream(crewMembersInMission)
                .forEach(l -> l.setLocation(ActivityNavigator.missionControl));

        generateThreat();

        isThreatTurn = assign0or1() == 1;

        //randomize which crew member will strike first
        playerIndex = assign0or1();
        missionDescription = "";

        String text =  isThreatTurn ? "the threat's" : "yours" ;
        missionDescription += "\n" + "The first attack is " + text;
        //(re)set mission Energy to original Energy
        crewMembersInMission[0].setMissionEnergy(crewMembersInMission[0].getEnergy());
        crewMembersInMission[1].setMissionEnergy(crewMembersInMission[1].getEnergy());
        Log.i("TAG", "Mission Energys set.");

        isMissionInProgress = true;

        //if the computer gets to attack first
        if(isThreatTurn){
            computerAttack();
        }

    }

    public static void endMission() {
        /*
         * this method is called when the player decides to call it a day and end the mission before it would normally end
         * */
        isMissionInProgress = false;

        //(re)set crew's mission Energy to original Energy
        crewMembersInMission[0].setMissionEnergy(crewMembersInMission[0].getEnergy());
        crewMembersInMission[1].setMissionEnergy(crewMembersInMission[1].getEnergy());

        //send crewMembers home
        Arrays.stream(crewMembersInMission)
                .forEach(l -> l.setLocation(ActivityNavigator.home));

    }

    public static int assign0or1() {
        return (int) (Math.random() * 2); //returns either 0 or 1. From ChatGPT
    }

    //ChatGPT was used to help with the ternary operator syntax..
    public static int turn(int i) {
        return (i == 0) ? 1 : 0;
    }

    public static int nextPlayerIndex(){
        playerIndex = playerIndex == 1 ? 0 : playerIndex + 1;
        return playerIndex;

    }

    public static boolean areMembersAlive(){
        Log.d("DEBUG", String.valueOf(crewMembersInMission[0].getMissionEnergy()));
        Log.d("DEBUG", String.valueOf(crewMembersInMission[1].getMissionEnergy()));
        return Arrays.stream(crewMembersInMission).filter(c -> c.getMissionEnergy() > 0).count() > 0;
    }

    public static void playerAttack(){

        int attackerIndex = nextPlayerIndex();

        int skill = crewMembersInMission[attackerIndex].getskill();
        int res = crewMembersInMission[attackerIndex].getResilience();
        int xp = crewMembersInMission[attackerIndex].getXp();
        int energy = crewMembersInMission[attackerIndex].getEnergy();
        int missionEnergy = crewMembersInMission[attackerIndex].getMissionEnergy();
        missionDescription += "\n" + crewMembersInMission[attackerIndex].getName() + ") attacks Threat" + " (" + threat.getName() + ").";

        int damageToThreat = calculateDamageTaken(skill, threat.getResilience());

        //reduce player's health
        threat.setMissionEnergy(threat.getMissionEnergy() - damageToThreat);

        //check threat's health; mission continues if threat if still alive
        if(threat.getMissionEnergy() > 0){
            missionDescription += "\nThreat" + " (" + threat.getName() + ") manages to escape death, with remaining of " + threat.getMissionEnergy() + "/" + threat.getEnergy() + "Energy.";
            isThreatTurn = true;
        }
        //mission ends if threat is reseated
        else{

            missionDescription += "\nThreat's (" + threat.getName() + ") health drops to zero.\nThe mission has ended.\nYou won!";
            int extraXp = 60;

            //increase the XP of each participating crew member, even the one(s) that died mid-mission
            for (CrewMember m: crewMembersInMission) {
                m.addXp(calculateXpGain(xp, skill, threat.getXp(),threat.getskill()));
            }
            missionDescription += "\n" + "You have gained +"+ extraXp + "XP.";


            //handle stat changes
            CrewMemberStatistics.addWin(crewMembersInMission);

            endMission();
        }


    }

    public static void computerAttack(){

        int skill = threat.getskill();
        int res = threat.getResilience();
        int xp = threat.getXp();
        int energy = threat.getEnergy();
        int missionEnergy = threat.getMissionEnergy();

        //attack one random player
        int randomIndex = (int)(Math.random() * crewMembersInMission.length);

        //calculate damage
        int damageToPlayer = calculateDamageTaken(skill, crewMembersInMission[randomIndex].getResilience());

        missionDescription += "\n" + threat.getName() + ") attacks all players. -" + damageToPlayer + "Energy";

        //reduce one random player's health based on the damage
        crewMembersInMission[randomIndex].setMissionEnergy(crewMembersInMission[randomIndex].getMissionEnergy() - damageToPlayer);

        //check threat's health; mission continues if at least one crew member if still alive
        if(areMembersAlive()){
            missionDescription += "\nYou escaped defeat.";
            isThreatTurn = false;
        }
        else{
            missionDescription += "\nThe threat has reseated all crew members.\nThe mission has ended.";

            CrewMemberStatistics.addMission(crewMembersInMission[playerIndex]);
            CrewMemberStatistics.addLoss();
            endMission();
        }

    }

    //Below method entirely credited to ChatGPT
    public static int calculateDamageTaken(int attackPower, int resense) {
        // 1. Resilienceense factor: higher resense reduces damage
        double resenseFactor = 1 - (resense / 100.0); // e.g., 20 resense = 0.8 multiplier

        // Cap the resense factor to avoid negative or zero damage
        if (resenseFactor < 0.1) {
            resenseFactor = 0.1; // at least 10% damage gets through
        }

        // 2. Base damage after resense
        int damage = (int) (attackPower * resenseFactor);

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
    public static boolean getIsThreatTurn(){
        return isThreatTurn;
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

    public static Threat getThreat(){
        return threat;
    }

}