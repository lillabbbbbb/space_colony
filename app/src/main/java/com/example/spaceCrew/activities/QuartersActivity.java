package com.example.spaceCrew.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceCrew.R;
import com.example.spaceCrew.adapters.QuartersAdapter;
import com.example.spaceCrew.crewMembers.CrewMember;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.MissionManager;
import com.example.spaceCrew.utils.CrewMemberManager;

import java.util.Arrays;

public class QuartersActivity extends AppCompatActivity {

    Button backButton, sendToTrain, sendToMission;
    QuartersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quarters);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CrewMemberManager.getInstance().unSelectAll();

        RecyclerView recyclerView = findViewById(R.id.quarters_recyclerview);
        adapter = new QuartersAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        backButton = findViewById(R.id.buttonBack);
        sendToTrain = findViewById(R.id.button);
        sendToMission = findViewById(R.id.button2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityNavigator.goBack(QuartersActivity.this);
            }
        });

        sendToTrain.setOnClickListener(view -> {
            Log.d("DEBUG", "SendToTrain button is clicked.");
            //check how many boxes are checked
            long count = CrewMemberManager.getInstance().getCrewMembers().stream()
                    .filter(CrewMember::isSelected) // only keep items satisfying condition
                    .count();
            Log.d("DEBUG", "Count: " + count);

            long current = CrewMemberManager.getInstance().getCrewMembers().stream()
                    .filter(l -> l.getLocation().equals(ActivityNavigator.simulator)) // only keep items satisfying condition
                    .count();
            Log.d("DEBUG", "Current: " + current);



            if(count + current > 3){
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("A maximum of three spaceCrews can train simultaneously.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Handle OK click
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Handle Cancel click
                        })
                        .show();
            }
            else {
                Log.d("DEBUG", "Else fulfilled");

                CrewMemberManager.getInstance().getCrewMembers().stream()
                        .filter(CrewMember::isSelected) // only keep items satisfying condition
                        .forEach(l -> l.setLocation(ActivityNavigator.simulator));

                if(count > 0){
                    ActivityNavigator.goToSimulator(this);
                }
            }
            CrewMemberManager.getInstance().getCrewMembers()
                    .forEach(l -> Log.d("DEBUG", l.getLocation()));
        });

        sendToMission.setOnClickListener(view -> {

            //store which spaceCrews are selected for mission
            CrewMember[] selectedCrewMembers = CrewMemberManager.getInstance().getCrewMembers().stream()
                    .filter(CrewMember::isSelected) // only keep items satisfying condition
                    .toArray(CrewMember[]::new);

            //check if max 2 spaceCrews are selected and there is no mission in progress currently
            if (selectedCrewMembers.length <= 2 && !MissionManager.getIsMissionInProgress()){

                //assign spacecolony to the mission control. Note: at this point the spacecolony's location hasn't changed to mission control, because the mission hasn't started and the user can still change the spaceCrews he wants to go to mission with.
                Arrays.stream(selectedCrewMembers)
                        .forEach(MissionManager::addCrewMemberToMission);

                //Navigate to Mission Control activity
                ActivityNavigator.goToMissionControl(this);

            }else if(selectedCrewMembers.length > 2){
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("A maximum of two spaceCrews can mission at once.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Handle OK click
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Handle Cancel click
                        })
                        .show();
            }else {
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("There is already a mission in progress.")
                        .setNegativeButton("OK", (dialog, which) -> {
                            // Handle Cancel click
                        })
                        .setPositiveButton("Go to Mission Control", (dialog, which) -> {
                            ActivityNavigator.goToMissionControl(this);
                        })
                        .show();
            }
        });
    }



}