package com.example.spaceCrew.utils;

import android.util.Log;

import com.example.spaceCrew.R;
import com.example.spaceCrew.crewMembers.CrewMember;

import java.util.ArrayList;
import java.util.List;

public class CrewMemberManager {
    /*
     * manages all the crewMembers in the game
     * implemented as a singleton object
     * */

    private static CrewMemberManager instance; //the singleton instance
    private int totalMissions; //the number of missions played in the game in total
    private static List<CrewMember> crewMembers; //all the crewMembers that have been created
    private static List<String> locationNames; //a list containing the names of all the locations where crewMembers can be in the game

    public static final String MEDIC = "Medic";
    public static final String SOLDIER = "Soldier";
    public static final String SCIENTIST = "Scientist";
    public static final String PILOT = "Pilot";
    public static final String ENGINEER = "Engineer";




    //private constructor
    private CrewMemberManager(){}

    //getter for the singleton instance
    public static CrewMemberManager getInstance(){
        if(instance == null){
            instance = new CrewMemberManager();

            crewMembers = new ArrayList<>();
            //add location names
            locationNames = new ArrayList<>();
            locationNames.add(ActivityNavigator.simulator);
            locationNames.add(ActivityNavigator.home);
            locationNames.add(ActivityNavigator.missionControl);
        }
        return instance;
    }

    public static void createCrewMember(CrewMember crewMember){
        //@param crewMember: the object to be appended to the list
        addCrewMember(crewMember);
        listCrewMembers();
    }

    public void addXP(CrewMember crewMember){
        /*
        adds one experience point to a crewMember
        @param crewMember: the crewMember whose experience points are to be incremented
        * */

        //iterate crewMembers list


        //find the crewMember by memory code

    }
    public void implementEyeColor(CrewMember crewMember){
        //????
    }

    public void unSelectAll(){
        CrewMemberManager.crewMembers
                .forEach(l -> l.setSelected(false));
    }


    public void select(List<CrewMember> crewMembers){
        /*
        * marks each crewMember in the parameter list as selected
        *  @param crewMembers: a list of those crewMembers that have been ticked and are therefore to be selected to do some activity
        * */
    }

    public void sendToControl(CrewMember crewMember){
        /*
        * sends a crewMember to the Mission Control by changing its location
        * @param crewMember: the crewMember whose location is to be changed
        * */
    }

    public void sendToTraining(CrewMember crewMember){
        /*
         * sends a crewMember to the Training Area by changing its location
         * @param crewMember: the crewMember whose location is to be changed
         * */
    }

    public void sendCrewMemberToChill(CrewMember crewMember){
        /*
         * sends a crewMember to Chillin' by changing its location
         * @param crewMember: the crewMember whose location is to be changed
         * */
    }

    public static int getSkin(CrewMember crewMember){
        String type = crewMember.getMemberType();
        int resId;

        switch (type){
            case CrewMemberManager.MEDIC: resId = R.drawable.medic;
            case CrewMemberManager.PILOT: resId = R.drawable.pilot;
            case CrewMemberManager.ENGINEER: resId = R.drawable.engineer;
            case CrewMemberManager.SCIENTIST: resId = R.drawable.scientist;
            case CrewMemberManager.SOLDIER: resId = R.drawable.soldier;
            default: resId = R.drawable.soldier;

        }
        return resId;
    }


    public void addMission(){
        //increment the number of missions by 1
        totalMissions++;
    }


    //getters and setters
    public int getTotalMissions() {
        return totalMissions;
    }

    public List<CrewMember> getCrewMembers() {
        return crewMembers;
    }

    public static void addCrewMember(CrewMember crewMember){
        if(crewMembers == null){
            crewMembers = new ArrayList<CrewMember>();
        }
        crewMembers.add(crewMember);
    }

    public static void listCrewMembers(){
        for (CrewMember crewMember:
             crewMembers) {
            Log.i("TAG", crewMember.toString());
        }
    }
    public List<String> getLocationNames() {
        return locationNames;
    }

    public void setLocationNames(List<String> locationNames) {
        this.locationNames = locationNames;
    }
}
