package com.example.spaceCrew.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceCrew.R;
import com.example.spaceCrew.adapters.DropdownAdapter;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.CrewMemberManager;
import com.example.spaceCrew.utils.CrewMemberStatistics;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ChartActivity extends AppCompatActivity {

    BarChart barChart;
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.buttonBack);


        //set up the dropdown selector
        ArrayList<DropdownAdapter.DropdownItem> items = CrewMemberManager.getInstance().getCrewMembers().stream()
                .map(l-> new DropdownAdapter.DropdownItem((l.getName().substring(0,3) + ", " + l.getAtk() + " ATK," + l.getDef() + " DEF," + l.getXp() + " XP," + l.getHp() + " HP"), CrewMemberManager.getSkin(l)))
                .collect(Collectors.toCollection(ArrayList::new));

        items.add(new DropdownAdapter.DropdownItem(CrewMemberStatistics.overall, R.drawable.together_5));

        AutoCompleteTextView dropdown = findViewById(R.id.ageDropdown);
        DropdownAdapter adapter = new DropdownAdapter(this, items);
        dropdown.setAdapter(adapter);

        //setup bar chart
        barChart = findViewById(R.id.barChart);

        //setting up pie chart
        pieChart = findViewById(R.id.pieChart);


        //event handling

        backButton.setOnClickListener(v -> {
                ActivityNavigator.goToStats(this);
        });

        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            //if user chooses overall stats
            if((parent.getItemAtPosition(position).toString()).equals(CrewMemberStatistics.overall)){
                setUpChartsOverall();
           }else{
                setupCharts(CrewMemberManager.getInstance().getCrewMembers().get(position).getId());
            }

        });

    }

    public void setUpChartsOverall() {
        //BAR CHART

        BarDataSet numOfSpaceCrews = new BarDataSet(new ArrayList<>(Arrays.asList(new BarEntry(1, CrewMemberManager.getInstance().getCrewMembers().size()))), "SpaceCrews");
        BarDataSet missions = new BarDataSet(new ArrayList<>(Arrays.asList(new BarEntry(2, CrewMemberStatistics.getTotalMissions()))), "Missions");
        BarDataSet winsData = new BarDataSet(new ArrayList<>(Arrays.asList(new BarEntry(3, CrewMemberStatistics.getWins()))), "Wins");
        BarDataSet trainings = new BarDataSet(new ArrayList<>(Arrays.asList(new BarEntry(4, CrewMemberStatistics.getTotalTimes()))), "Trainings");
        numOfSpaceCrews.setColor(Color.YELLOW);
        missions.setColor(Color.RED);
        winsData.setColor(Color.GREEN);
        trainings.setColor(Color.BLUE);

        BarData data = new BarData(numOfSpaceCrews, missions, winsData, trainings);

        float groupSpace = 0.2f;
        float barSpace = 0.05f; // space between bars of the same group
        float barWidth = 0.35f; // width of each bar

        data.setBarWidth(barWidth);
        barChart.setData(data);

        // Group the bars by X value
        barChart.groupBars(0f, groupSpace, barSpace);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setCenterAxisLabels(true);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        barChart.animateY(300); // animate vertically
        barChart.animateX(300); // animate horizontally

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(false);   // hides labels (numbers/text)
        xAxis.setDrawAxisLine(false); // hides the axis line itself (optional)
        xAxis.setDrawGridLines(false); // hides grid lines

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false); // hides right y-axis labels
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);

        barChart.invalidate(); // refresh chart

        //PIE CHART

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(CrewMemberStatistics.getWins(), "Wins"));
        entries.add(new PieEntry(CrewMemberStatistics.getTotalMissions() - CrewMemberStatistics.getWins(), "Losses"));

        // 2️⃣ Create dataset
        PieDataSet dataSet = new PieDataSet(entries, "Wins/Losses Distribution");
        dataSet.setColors(new int[] { Color.GREEN, Color.RED });
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(16f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        pieChart.invalidate(); // refresh chart
    }

    public void setupCharts(int lutId){
        //BAR CHART

        BarDataSet missions = new BarDataSet(new ArrayList<>(Arrays.asList(new BarEntry(1, CrewMemberStatistics.getMissions(lutId)))), "Missions");
        BarDataSet winsData = new BarDataSet(new ArrayList<>(Arrays.asList(new BarEntry(2, CrewMemberStatistics.getWins(lutId)))), "Wins");
        BarDataSet trainings = new BarDataSet(new ArrayList<>(Arrays.asList(new BarEntry(3, CrewMemberStatistics.getNumOfSimulations(lutId)))), "Trainings");
        missions.setColor(Color.RED);
        winsData.setColor(Color.GREEN);
        trainings.setColor(Color.BLUE);

        BarData data = new BarData(missions, winsData, trainings);

        float groupSpace = 0.2f;
        float barSpace = 0.05f; // space between bars of the same group
        float barWidth = 0.35f; // width of each bar

        data.setBarWidth(barWidth);
        barChart.setData(data);

        // Group the bars by X value
        barChart.groupBars(0f, groupSpace, barSpace);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setCenterAxisLabels(true);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        barChart.animateY(300); // animate vertically
        barChart.animateX(300); // animate horizontally

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(false);   // hides labels (numbers/text)
        xAxis.setDrawAxisLine(false); // hides the axis line itself (optional)
        xAxis.setDrawGridLines(false); // hides grid lines

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false); // hides right y-axis labels
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);




        barChart.invalidate(); // refresh chart

        //PIE CHART

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(CrewMemberStatistics.getWins(lutId), "Wins"));
        entries.add(new PieEntry(CrewMemberStatistics.getMissions(lutId) - CrewMemberStatistics.getWins(lutId), "Losses"));

        // 2️⃣ Create dataset
        PieDataSet dataSet = new PieDataSet(entries, "Wins/Losses Distribution");
        dataSet.setColors(new int[] { Color.GREEN, Color.RED });
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(16f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        pieChart.invalidate(); // refresh chart
    }
}