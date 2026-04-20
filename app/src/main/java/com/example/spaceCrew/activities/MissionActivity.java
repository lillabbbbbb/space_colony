package com.example.spaceCrew.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceCrew.R;
import com.example.spaceCrew.adapters.DropdownAdapter;
import com.example.spaceCrew.crewMembers.CrewMember;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.MissionManager;
import com.example.spaceCrew.utils.ImageTinter;
import com.example.spaceCrew.utils.CrewMemberManager;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MissionActivity extends AppCompatActivity {
    Button backButton;
    TextView skill1, energy1, res1, xp1;
    TextView skill2, energy2, res2, xp2;
    TextView skillThreat2, energyThreat2, resThreat2, xpThreat2;
    ImageView imageView1, imageView2, imageViewThreat;
    ImageView lightsaberView;
    Button attackButton, endmissionButton;
    ArrayList<TextView> skills = new ArrayList<>();
    ArrayList<TextView> energys = new ArrayList<>();
    ArrayList<TextView> ress = new ArrayList<>();
    ArrayList<TextView> xps = new ArrayList<>();
    ArrayList<ImageView> images = new ArrayList<>();
    TextView missionDescription;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mission);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CrewMemberManager.getInstance().unSelectAll();

        backButton = findViewById(R.id.buttonBack);
        attackButton = findViewById(R.id.attackButton);
        attackButton.setVisibility(View.INVISIBLE);
        endmissionButton = findViewById(R.id.endButton);
        lightsaberView = findViewById(R.id.sword);
        missionDescription = findViewById(R.id.my_textbox);

        skill1 = findViewById(R.id.skill2);
        energy1 = findViewById(R.id.energy2);
        res1 = findViewById(R.id.res2);
        xp1 = findViewById(R.id.experience2);
        imageView1 = findViewById(R.id.image1);
        imageView1.setOnClickListener(view -> {
            //fun animation
            ObjectAnimator flipAnimator = ObjectAnimator.ofFloat(imageView1, "scaleX", 1.2f, 1.2f);
            flipAnimator.setDuration(500); // time to flip
            flipAnimator.setRepeatMode(ValueAnimator.REVERSE); // flip back
            flipAnimator.setRepeatCount(1); // do it only once (flip there and back)
            flipAnimator.start();
        });

        skill2 = findViewById(R.id.skill4);
        energy2 = findViewById(R.id.energy4);
        res2 = findViewById(R.id.res4);
        xp2 = findViewById(R.id.experience4);
        imageView2 = findViewById(R.id.image2);
        imageView2.setOnClickListener(view -> {
            //fun animation
            ObjectAnimator flipAnimator = ObjectAnimator.ofFloat(imageView2, "scaleX", 1.2f, 1.2f);
            flipAnimator.setDuration(500); // time to flip
            flipAnimator.setRepeatMode(ValueAnimator.REVERSE); // flip back
            flipAnimator.setRepeatCount(1); // do it only once (flip there and back)
            flipAnimator.start();
        });

        skillThreat2 = findViewById(R.id.skillThreat2);
        energyThreat2 = findViewById(R.id.energyThreat2);
        resThreat2 = findViewById(R.id.resThreat2);
        xpThreat2 = findViewById(R.id.experienceThreat2);
        imageViewThreat = findViewById(R.id.imageThreat);





        ArrayList<DropdownAdapter.DropdownItem> items = CrewMemberManager.getInstance().getCrewMembers().stream()
                .map(l-> new DropdownAdapter.DropdownItem((l.getName().substring(0,3) + ", " + l.getskill() + " skill," + l.getResilience() + " DEF," + l.getXp() + " XP," + l.getEnergy() + " Energy"), CrewMemberManager.getSkin(l)))
                .collect(Collectors.toCollection(ArrayList::new));

        //this component was suggested by ChatGPT
        AutoCompleteTextView dropdown = findViewById(R.id.dropdownMenu1);
        DropdownAdapter adapter = new DropdownAdapter(this, items);
        dropdown.setAdapter(adapter);

        AutoCompleteTextView dropdown2 = findViewById(R.id.dropdownMenu2);
        dropdown2.setAdapter(adapter);

        ArrayList<AutoCompleteTextView> dropdownList = new ArrayList<>();
        dropdownList.add(dropdown);
        dropdownList.add(dropdown2);
        skills.add(skill1);
        skills.add(skill2);
        energys.add(energy1);
        energys.add(energy2);
        ress.add(res1);
        ress.add(res2);
        xps.add(xp1);
        xps.add(xp2);
        images.add(imageView1);
        images.add(imageView2);


        //call getInstance() method to instantiate getCrew MembersInmission array
        //MissionManager.getInstance();

        for(int j = 0; j < MissionManager.getCrewMembersInMission().length; j++){
            if(MissionManager.getCrewMembersInMission()[j] != null){
                //bind views to variables for Crew Member i
                skills.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getskill()));
                energys.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getMissionEnergy()));
                ress.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getResilience()));
                xps.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getXp()));
                images.get(j).setImageResource(CrewMemberManager.getSkin(MissionManager.getCrewMembersInMission()[j]));
                images.get(j).setImageBitmap(ImageTinter.tintWithoutBlack(images.get(j), MissionManager.getCrewMembersInMission()[j]));

                for(int i = 0; i < CrewMemberManager.getInstance().getCrewMembers().size(); i++){
                    if(MissionManager.getCrewMembersInMission()[j].getId() == CrewMemberManager.getInstance().getCrewMembers().get(i).getId()){
                        dropdownList.get(j).setText(items.get(i).getText(), false);
                    }
                }
            }
        }


        backButton.setOnClickListener(view -> {
                ActivityNavigator.goBack(MissionActivity.this);
        });

        attackButton.setOnClickListener(view -> {
            //execute attack if it's the player's turn
            if(!MissionManager.getIsThreatTurn() && MissionManager.getIsMissionInProgress() && MissionManager.areMembersAlive()){
                Log.d("DEBUG", String.valueOf(!MissionManager.getIsThreatTurn() && MissionManager.getIsMissionInProgress()));
                Log.d("DEBUG", MissionManager.getMissionDescription());

                //PLAYER ATTACK

                MissionManager.playerAttack();

                //refresh visuals
                refresh();

                //short artificial delay
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (MissionManager.areMembersAlive() && MissionManager.getIsMissionInProgress()) {

                        //COMPUTER ATTACK

                        MissionManager.computerAttack();

                        //short artificial delay
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            //refresh visuals
                            refresh();

                        }, 3000);
                    }
                }, 1000);

                Log.d("DEBUG", MissionManager.getMissionDescription());

            }
            //debugging
            else {

                Log.d("DEBUG", "else");
                Log.d("DEBUG", String.valueOf(!MissionManager.getIsThreatTurn() && MissionManager.getIsMissionInProgress()));
                Log.d("DEBUG", String.valueOf(MissionManager.areMembersAlive()));
                Log.d("DEBUG", MissionManager.getMissionDescription());

            }
        });

        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            CrewMember crewMember = CrewMemberManager.getInstance().getCrewMembers().get(position);

            int index = 0;

            MissionManager.addCrewMemberToMission(crewMember, index);

            //bind views to variables for Member 1
            setViewValues(crewMember, index);
        });

        dropdown2.setOnItemClickListener((parent, view, position, id) -> {
            CrewMember crewMember = CrewMemberManager.getInstance().getCrewMembers().get(position);

            int index = 1;

            MissionManager.addCrewMemberToMission(crewMember, index);

            //bind views to variables for Member 2
            setViewValues(crewMember, index);
        });

        endmissionButton.setOnClickListener(view -> {

            if (endmissionButton.getText().toString().equals("Start Mission")) {

                //making sure that two crewMembers are selected
                if(MissionManager.getCrewMembersInMission()[0] == null || MissionManager.getCrewMembersInMission()[1] == null) {
                    new AlertDialog.Builder(this)
                            .setTitle("Warning")
                            .setMessage("Two Crew Members must be selected in order to start a mission.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Handle OK click
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                // Handle Cancel click
                            })
                            .show();
                }
                //check if two crewMembers are selected and they are not the same
                else if (MissionManager.getCrewMembersInMission()[0] != null && MissionManager.getCrewMembersInMission()[1] != null){
                    if (MissionManager.getCrewMembersInMission()[0].getId() == MissionManager.getCrewMembersInMission()[1].getId()) {
                        new AlertDialog.Builder(this)
                                .setTitle("Warning")
                                .setMessage("You must select two different Crew Members to mission.")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    // Handle OK click
                                })
                                .show();
                    }
                    else{ //start mission fr now!

                        MissionManager.startMission();

                        endmissionButton.setText("End mission");
                        attackButton.setVisibility(View.VISIBLE);
                        attackButton.setEnabled(!MissionManager.getIsThreatTurn() && MissionManager.getIsMissionInProgress());
                        lightsaberView.setImageResource(R.drawable.lightsaber_left);

                        //display threat's abilities
                        skillThreat2.setText(String.valueOf(MissionManager.getThreat().getskill()));
                        energyThreat2.setText(String.valueOf(MissionManager.getThreat().getMissionEnergy()));
                        resThreat2.setText(String.valueOf(MissionManager.getThreat().getResilience()));
                        xpThreat2.setText(String.valueOf(MissionManager.getThreat().getXp()));


                        //set the threat's image and tint
                        imageViewThreat.setImageResource(MissionManager.getThreatImageResource());
                        int tintR = Color.red(MissionManager.getThreat().getColor());     // returns 0–255
                        int tintG = Color.green(MissionManager.getThreat().getColor()); // returns 0–255
                        int tintB = Color.blue(MissionManager.getThreat().getColor());  // returns 0–255
                        imageViewThreat.setImageBitmap(ImageTinter.tintWithoutBlack(imageViewThreat,tintR, tintG, tintB));


                        //refresh visuals
                        refresh();

                        Log.d("DEBUG", MissionManager.getMissionDescription());

                        //apply colors to dropdown to show which crewMember is the player's (green) and the computer's (red)
                        GradientDrawable drawable1 = new GradientDrawable();
                        drawable1.setColor(Color.GREEN); // Background color
                        drawable1.setStroke(4, Color.GREEN); // Border width and color
                        drawable1.setCornerRadius(16); // Optional: rounded corners
                        // Apply it to the dropdown
                        dropdownList.get(MissionManager.getPlayerIndex()).setDropDownBackgroundDrawable(drawable1);

                        //apply colors to dropdown to show which crewMember is the player's (green) and the computer's (red)
                        GradientDrawable drawable2 = new GradientDrawable();
                        drawable2.setColor(Color.RED); // Background color
                        drawable2.setStroke(4, Color.RED); // Border width and color
                        drawable2.setCornerRadius(16); // Optional: rounded corners
                        // Apply it to the dropdown
                        dropdownList.get(MissionManager.turn(MissionManager.getPlayerIndex())).setDropDownBackgroundDrawable(drawable2);


                        missionDescription.setText(MissionManager.getMissionDescription());

                    }
                }
            }
            else{ //text == "End mission"

                MissionManager.endMission();
                adapter.notifyDataSetChanged();

                //relaunch page
                ActivityNavigator.goToMissionControl(this);

            }

            //enable or disable dropdown, depending on whether the mission is still in progress
            dropdownList.stream()
                    .forEach(d -> d.setOnTouchListener((v, event) -> {
                            // Return true to consume the touch event and prevent dropdown
                            return MissionManager.getIsMissionInProgress();
                    }));

        });
    }
    public void oneWayAnmiation(ImageView lightsaberView){
        lightsaberView.animate()
                .scaleX((-1) * lightsaberView.getScaleX())   // flip horizontally
                .setDuration(300)
                .start();
    }


    public void setViewValues(CrewMember crewMember, int index){
        skills.get(index).setText(String.valueOf(crewMember.getskill()));
        energys.get(index).setText(String.valueOf(crewMember.getEnergy()));
        ress.get(index).setText(String.valueOf(crewMember.getResilience()));
        xps.get(index).setText(String.valueOf(crewMember.getXp()));
        images.get(index).setImageResource(CrewMemberManager.getSkin(crewMember));
        images.get(index).setImageBitmap(ImageTinter.tintWithoutBlack(images.get(index), crewMember));
    }

    public void refresh(){
        attackButton.setEnabled(!MissionManager.getIsThreatTurn() && MissionManager.getIsMissionInProgress());
        if(MissionManager.getIsMissionInProgress()){
            attackButton.setVisibility(View.VISIBLE);
            endmissionButton.setText("End Mission");
        }else{
            attackButton.setVisibility(View.INVISIBLE);
            endmissionButton.setText("Start New Mission");

        }
        missionDescription.setText(MissionManager.getMissionDescription());
        oneWayAnmiation(lightsaberView);


        //refresh Players' stats
        for(int j = 0; j < MissionManager.getCrewMembersInMission().length; j++){
            if(MissionManager.getCrewMembersInMission()[j] != null){
                //bind views to variables for Crew Member i
                skills.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getskill()));
                energys.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getMissionEnergy()));
                ress.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getResilience()));
                xps.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getXp()));
            }
        }

        //refresh threat stats
        skillThreat2.setText(String.valueOf(MissionManager.getThreat().getskill()));
        energyThreat2.setText(String.valueOf(MissionManager.getThreat().getMissionEnergy()));
        resThreat2.setText(String.valueOf(MissionManager.getThreat().getResilience()));
        xpThreat2.setText(String.valueOf(MissionManager.getThreat().getXp()));

    }

}