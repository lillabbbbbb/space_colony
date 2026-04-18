package com.example.spaceCrew.activities;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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
import com.example.spaceCrew.adapters.SimulatorAdapter;
import com.example.spaceCrew.crewMembers.CrewMember;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.MissionManager;
import com.example.spaceCrew.utils.CrewMemberManager;
import com.example.spaceCrew.utils.CrewMemberStatistics;

import java.util.Arrays;

public class SimulationActivity extends AppCompatActivity {

    Button backButton;
    Button endButton;
    Button missionButton;
    SimulatorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_simulator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CrewMemberManager.getInstance().unSelectAll();

        RecyclerView recyclerView = findViewById(R.id.simulator_recyclerview);
        adapter = new SimulatorAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        backButton = findViewById(R.id.buttonBack);
        missionButton = findViewById(R.id.missionButton);
        endButton = findViewById(R.id.endButton);

        backButton.setOnClickListener(view -> {
            ActivityNavigator.goBack(SimulationActivity.this);
        });

        endButton.setOnClickListener(view -> {

            //record current simulation's results and add to stats
            adapter.rows.stream()
                            .filter(r -> CrewMemberManager.getInstance().getCrewMembers().get(r.getAdapterPosition()).isSelected())
                            .forEach(r -> {
                                //Syntax from ChatGPT
                                r.chronometer.stop();
                                // Stop chronometer

                                // Calculate elapsed time in ms
                                int elapsedMillis = (int) (SystemClock.elapsedRealtime() - r.chronometer.getBase());

                                // Convert to full minutes
                                int minutes = (elapsedMillis / 1000) / 60;

                                CrewMemberStatistics.addSimulationTime(CrewMemberManager.getInstance().getCrewMembers().get(r.getAdapterPosition()), minutes);
                            });

            //send crewMember from simulator to home
            CrewMemberManager.getInstance().getCrewMembers().stream()
                    .filter(CrewMember::isSelected) // only keep items satisfying condition
                    .forEach(l -> l.setLocation(ActivityNavigator.home));


            long count = CrewMemberManager.getInstance().getCrewMembers().stream()
                    .filter(CrewMember::isSelected) // only keep items satisfying condition
                    .count();
            Log.d("DEBUG", "Count: " + count);

            if(count > 0){
                //navigate to chillin activity
                ActivityNavigator.goToQuarters((AppCompatActivity) this);
            }
        });

        missionButton.setOnClickListener(view -> {

            //store which crewMembers are selected for mission
            CrewMember[] selectedSpaceCrews = CrewMemberManager.getInstance().getCrewMembers().stream()
                    .filter(CrewMember::isSelected) // only keep items satisfying condition
                    .toArray(CrewMember[]::new);

            //check if max 2 crewMembers are selected and there is no mission in progress currently
            if (selectedSpaceCrews.length <= 2 && selectedSpaceCrews.length > 0 && !MissionManager.getIsMissionInProgress()){

                //assign crewMember to the mission control. Note: at this point the crewMember's location hasn't changed to mission control, because the mission hasn't started and the user can still change the crewMembers he wants to go to mission with.
                Arrays.stream(selectedSpaceCrews)
                        .forEach(l -> MissionManager.addCrewMemberToMission(l));

                //Navigate to Mission Control activity
                ActivityNavigator.goToMissionControl(this);

            }else if(selectedSpaceCrews.length > 2){
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("A maximum of two crewMembers can mission at once.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Handle OK click
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Handle Cancel click
                        })
                        .show();
            }else if (selectedSpaceCrews.length > 0){
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