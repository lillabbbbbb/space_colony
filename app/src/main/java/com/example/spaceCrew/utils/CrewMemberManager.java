package com.example.spaceCrew.utils;

import android.util.Log;

import com.example.spaceCrew.R;
import com.example.spaceCrew.crewMembers.CrewMember;

import java.util.ArrayList;
import java.util.List;

public class CrewMemberManager {
    /*
     * manages all the crewMembers in the game
     * */

    private static CrewMemberManager instance; //the singleton instance
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
    public void unSelectAll(){
        CrewMemberManager.crewMembers
                .forEach(l -> l.setSelected(false));
    }

    public static int getSkin(CrewMember crewMember){
        String type = crewMember.getMemberType();
        int resId = 0;

        switch (type){
            case CrewMemberManager.MEDIC: resId = R.drawable.medic; break;
            case CrewMemberManager.PILOT: resId = R.drawable.pilot; break;
            case CrewMemberManager.ENGINEER: resId = R.drawable.engineer; break;
            case CrewMemberManager.SCIENTIST: resId = R.drawable.scientist; break;
            case CrewMemberManager.SOLDIER: resId = R.drawable.soldier; break;
            default: resId = R.drawable.soldier; break;

        }
        Log.i("TAG", String.valueOf(resId));
        return resId;
    }

    public static void emptyCrewData(){
        if(crewMembers != null){
            crewMembers.clear();
        }
    }

    //getters and setters

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
}
