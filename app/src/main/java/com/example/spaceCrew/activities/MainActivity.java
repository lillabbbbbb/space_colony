package com.example.spaceCrew.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceCrew.R;
import com.example.spaceCrew.adapters.OverviewAdapter;
import com.example.spaceCrew.utils.ActivityNavigator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button createButton;
    OverviewAdapter adapter;
    public static ArrayList<Button> buttons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttons = new ArrayList<>();

        //test data
//        CrewMemberManager.createCrewMember(new CrewMember("Saku", 35, 100, 189, 100, 89, 1900, 100, 28));
//        CrewMemberManager.createCrewMember(new CrewMember("2nd", 150, 100, 189, 100, 89, 1900, 100, 90));
//        CrewMemberManager.createCrewMember(new CrewMember("3rd", 35, 220, 189, 100, 89, 1900, 100, 2));
//        CrewMemberManager.listCrewMembers();

        RecyclerView recyclerView = findViewById(R.id.home_recyclerview);
        adapter = new OverviewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //handle button clicks navigating to another activity
        ActivityNavigator navigator = new ActivityNavigator(this);

        /*if(OverviewAdapter.buttons != null){
            for (Button button:
                    OverviewAdapter.buttons) {
                button.setOnClickListener((View view) -> {
                    Log.i("TAG", "Button is clicked.");
                    String selectedArea = (String) button.getTag();
                    navigator.navigateTo(selectedArea);
                });
            }
        }
        else Log.i("TAG", "There are no buttons.");*/


        createButton =  findViewById(R.id.button4);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                ActivityNavigator.createNewCrewMemberActivity(MainActivity.this);
            }
        });
    }
}