package com.example.spaceCrew.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.spaceCrew.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spaceCrew.activities.MissionActivity;
import com.example.spaceCrew.activities.ChartActivity;
import com.example.spaceCrew.activities.QuartersActivity;
import com.example.spaceCrew.activities.CreateNewCrewMemberActivity;
import com.example.spaceCrew.activities.MainActivity;
import com.example.spaceCrew.activities.StatisticsActivity;
import com.example.spaceCrew.activities.SimulationActivity;

import java.util.LinkedHashMap;
import java.util.Map;

//handles moving to different activities (game areas)
//was made using ChatGPT
public class ActivityNavigator {

    private final Context context;
    private static final Map<String, Runnable> actions = new LinkedHashMap<>();

    //location names:
    public static String home = "Quarters";
    public static String simulator = "Simulator";
    public static String missionControl = "Mission Control";
    public static String statistics = "Statistics";
    public static String charts = "Charts";


    public static void createNewCrewMemberActivity(AppCompatActivity context) {
        Intent intent = new Intent(context, CreateNewCrewMemberActivity.class);
        context.startActivity(intent);
    }

    public static void goBack(AppCompatActivity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    public static void goToSimulator(AppCompatActivity context) {
        Intent intent = new Intent(context, SimulationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void goToStats(AppCompatActivity context) {
        Intent intent = new Intent(context, StatisticsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void goToCharts(AppCompatActivity context) {
        Intent intent = new Intent(context, ChartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void goToMissionControl(AppCompatActivity context) {
        Intent intent = new Intent(context, MissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void goToQuarters(AppCompatActivity context) {
        Intent intent = new Intent(context, QuartersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }




    public ActivityNavigator(Context context) {
        this.context = context;

        // Register each area's action
        actions.put(home, () -> goToQuarters((AppCompatActivity) context));
        actions.put(ActivityNavigator.missionControl, () -> goToMissionControl((AppCompatActivity) context));
        actions.put(ActivityNavigator.simulator, () -> goToSimulator((AppCompatActivity) context));
        actions.put("Statistics", () -> goToStats((AppCompatActivity) context));
        actions.put("Overview", () -> goBack((AppCompatActivity) context));
    }


    private void goToActivity(Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        Log.i("TAG", "Activity started.");
        context.startActivity(intent);
    }

    public void navigateTo(String areaName) {
        Runnable action = actions.get(areaName);
        if (action != null) {
            action.run();
        }
    }
    public static Map<String, Runnable> getActions(){
        return actions;
    }
}