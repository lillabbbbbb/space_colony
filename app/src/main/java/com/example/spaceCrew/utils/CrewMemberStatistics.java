package com.example.spaceCrew.utils;

import android.os.Build;
import android.util.Log;

import com.example.spaceCrew.crewMembers.CrewMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrewMemberStatistics {
    /*
    * responsible for keeping track of general statistics and individual
    * crewMember statistics regarding their missions and simuations.
    * */
    public static String overall = "Overall";

    private static Map<Integer, Integer> totalMissions = new HashMap<>();
    private static Map<Integer, Integer> totalWins = new HashMap<>();
    private static Map<Integer, List<Integer>> simulationTimes = new HashMap<>();
    private static Map<Integer, Float> avgSimulationTime  = new HashMap<>();
    private static Map<Integer, Integer> gainedXP  = new HashMap<>();


    public static void addMission(CrewMember crewMember){
       //increment the crewMember's number of missions
        int id = crewMember.getId();
        try {
            int missions = totalMissions.get(id);
            totalMissions.replace(id, ++missions);
        }catch(NullPointerException e){
            totalMissions.put(id, 1);
        }

    }

    public static void addWin(CrewMember crewMember){
        //increment the crewMember's number of wins
        int id = crewMember.getId();
        try {
            int missions = totalWins.get(id);
            totalWins.replace(id, ++missions);
        }catch(NullPointerException e){
            totalWins.put(id, 1);
        }

    }

    public static void addSimulationTime(int id, int durationMins){
        //if there is no crewmember with the given id
        if(simulationTimes.get(id) == null) return;

        //increment the crewMember's number of wins
        try {
            List<Integer> times = simulationTimes.get(id);
            times.add(durationMins);
            simulationTimes.replace(id, times);
        }catch(NullPointerException e){
            List<Integer> times = new ArrayList<>();
            times.add(durationMins);
            simulationTimes.put(id, times);
        }

        //update the average simuation time
        int sum = 0;
        for (int i = 0; i < simulationTimes.get(id).size(); i++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sum += simulationTimes.get(id).get(i);
            }
        }
        int total = simulationTimes.get(id).size();
        avgSimulationTime.replace(id, calcAvg(sum, total));

    }

    public static float calcAvg(float sum, float total){
        return sum / total;
    }

    //the following methods provide the data for the recycler view of the overview activity
    //
    public static ArrayList<ArrayList<String>> getOverview(){
        ArrayList<ArrayList<String>> list = new ArrayList<>();

        CrewMemberManager crewMemberManager = CrewMemberManager.getInstance();

        Log.d("TAG", String.valueOf(ActivityNavigator.getActions().size()));
        //iterate the list of areas
        List<String> names = new ArrayList<>(ActivityNavigator.getActions().keySet());
        for(int i = 0; i < names.size(); i++){
            ArrayList<String> innerList = new ArrayList<>();

            String areaName = names.get(i);

            //find the number of crewMembers that are at the corresponding location
            int counter = 0;
            for(int j = 0; j < crewMemberManager.getCrewMembers().size();j++) {
                if (crewMemberManager.getCrewMembers().get(j).getLocation().equals(areaName)) {
                    counter++;
                }
            }

            //add data to inner list
            Log.d("TAG", areaName);
            innerList.add(areaName);
            Log.d("TAG", "There are currently " + counter + " crewMembers here.");
            innerList.add("There are currently " + counter + " crew members here.");
            innerList.add("View " + areaName);
            Log.d("TAG", "View " + areaName);


            list.add(innerList);
        }
        return list;
    }


    //return value if found, return 0 if there is no value with the key @id
    public static int getMissions(int id){
        return totalMissions.getOrDefault(id, 0);
    }

    public static int getWins(int id){
        return totalWins.getOrDefault(id, 0);
    }

    public static int getNumOfSimulations(int id){
        return simulationTimes.get(id) == null ? 0 : simulationTimes.get(id).size();
    }

    public static int getTotalSimulationMins(int id){
        if(simulationTimes.get(id) == null){
            return 0;
        }else{
            return simulationTimes.get(id).stream()
                    .mapToInt(i -> i)
                    .sum();
        }
    }
    public static float getAvgSimulationTimes(int id){
        return avgSimulationTime.getOrDefault(id, (float) 0);
    }


    public static int getTotalMissions(){
        return totalMissions.values().stream()
                .mapToInt(i -> i)
                .sum();
    }
    public static int getTotalWins(){
        return totalWins.values().stream()
                .mapToInt(i -> i)
                .sum();
    }

    //ChatGPT was used for figuring out the syntax for this one
    public static int getTotalMins(){
        return simulationTimes.values().stream()
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .sum();
    }
    public static int getTotalTimes(){
        return simulationTimes.values().stream()
                .mapToInt(List::size)
                .sum();
    }
    public static float getTotalAvg(){
        return calcAvg(getTotalMins(), getTotalTimes());
    }

}
