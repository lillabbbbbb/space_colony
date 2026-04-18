package com.example.spaceCrew.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceCrew.R;
import com.example.spaceCrew.adapters.StatisticsAdapter;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.CrewMemberStatistics;

public class StatisticsActivity extends AppCompatActivity {

    StatisticsAdapter adapter;
    Button backButton, chartsButton;
    TextView missions, wins, totalSimulationMins, totalSimulationTimes, totalAvgTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //set up recyclerview
        RecyclerView recyclerView = findViewById(R.id.stat_recycler_view);
        adapter = new StatisticsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        backButton = findViewById(R.id.buttonBack);
        chartsButton = findViewById(R.id.buttonCharts);

        //load total statistics data
        missions = findViewById(R.id.missions2);
        missions.setText(String.valueOf(CrewMemberStatistics.getTotalWins()));
        wins = findViewById(R.id.wins2);
        wins.setText(String.valueOf(CrewMemberStatistics.getTotalWins()));
        totalSimulationMins = findViewById(R.id.minsTotal2);
        totalSimulationMins.setText(String.valueOf(CrewMemberStatistics.getTotalMins()));
        totalSimulationTimes = findViewById(R.id.simulationTotal2);
        totalSimulationTimes.setText(String.valueOf(CrewMemberStatistics.getTotalTimes()));
        totalAvgTimes = findViewById(R.id.simulationAvg2);
        totalAvgTimes.setText(String.valueOf(CrewMemberStatistics.getTotalAvg()));


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityNavigator.goBack(StatisticsActivity.this);
            }
        });

        chartsButton.setOnClickListener(v -> {
            ActivityNavigator.goToCharts(this);
        });
    }
}