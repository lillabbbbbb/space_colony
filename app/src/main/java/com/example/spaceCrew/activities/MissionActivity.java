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
    TextView atk1, hp1, def1, xp1;
    TextView atk2, hp2, def2, xp2;
    ImageView imageView1, imageView2;
    ImageView swordView;
    Button attackButton, endmissionButton;
    ArrayList<TextView> atks = new ArrayList<>();
    ArrayList<TextView> hps = new ArrayList<>();
    ArrayList<TextView> defs = new ArrayList<>();
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
        swordView = findViewById(R.id.sword);
        missionDescription = findViewById(R.id.my_textbox);

        atk1 = findViewById(R.id.atk2);
        hp1 = findViewById(R.id.hp2);
        def1 = findViewById(R.id.def2);
        xp1 = findViewById(R.id.experience2);
        imageView1 = findViewById(R.id.image1);

        atk2 = findViewById(R.id.atk4);
        hp2 = findViewById(R.id.hp4);
        def2 = findViewById(R.id.def4);
        xp2 = findViewById(R.id.experience4);
        imageView2 = findViewById(R.id.image2);




        ArrayList<DropdownAdapter.DropdownItem> items = CrewMemberManager.getInstance().getCrewMembers().stream()
                .map(l-> new DropdownAdapter.DropdownItem((l.getName().substring(0,3) + ", " + l.getAtk() + " ATK," + l.getDef() + " DEF," + l.getXp() + " XP," + l.getHp() + " HP"), CrewMemberManager.getSkin(l)))
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
        atks.add(atk1);
        atks.add(atk2);
        hps.add(hp1);
        hps.add(hp2);
        defs.add(def1);
        defs.add(def2);
        xps.add(xp1);
        xps.add(xp2);
        images.add(imageView1);
        images.add(imageView2);


        //call getInstance() method to instantiate getLutemonsInmission array
        //MissionManager.getInstance();

        for(int j = 0; j < MissionManager.getCrewMembersInMission().length; j++){
            if(MissionManager.getCrewMembersInMission()[j] != null){
                //bind views to variables for Lutemon i
                atks.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getAtk()));
                hps.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getMissionHp()));
                defs.get(j).setText(String.valueOf(MissionManager.getCrewMembersInMission()[j].getDef()));
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
            if(MissionManager.getPlayerIndex() == MissionManager.getWhoseTurn() && MissionManager.areBothAlive()){
                Log.d("DEBUG", String.valueOf(MissionManager.getWhoseTurn()));
                Log.d("DEBUG", MissionManager.getMissionDescription());

                //PLAYER ATTACK

                MissionManager.playerAttack();

                //refresh visuals
                refresh();

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (MissionManager.areBothAlive()) {

                        //COMPUTER ATTACK

                        MissionManager.computerAttack();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            //refresh visuals
                            refresh();

                        }, 3000);
                    }
                }, 1000);

                Log.d("DEBUG", MissionManager.getMissionDescription());

            }
            else {

                Log.d("DEBUG", "else");
                Log.d("DEBUG", String.valueOf(MissionManager.getPlayerIndex() == MissionManager.getWhoseTurn()));
                Log.d("DEBUG", String.valueOf(MissionManager.areBothAlive()));
                Log.d("DEBUG", String.valueOf(MissionManager.getWhoseTurn()));
                Log.d("DEBUG", MissionManager.getMissionDescription());

            }
        });

        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            CrewMember lutemon = CrewMemberManager.getInstance().getCrewMembers().get(position);

            int index = 0;

            MissionManager.addCrewMemberToMission(lutemon, index);
            //bind views to variables for Lutemon 1
            setViewValues(lutemon, index);
        });

        dropdown2.setOnItemClickListener((parent, view, position, id) -> {
            CrewMember lutemon = CrewMemberManager.getInstance().getCrewMembers().get(position);

            int index = 1;

            MissionManager.addCrewMemberToMission(lutemon, index);
            //bind views to variables for Lutemon 2
            setViewValues(lutemon, index);
        });

        endmissionButton.setOnClickListener(view -> {

            if (endmissionButton.getText().toString().equals("Start mission")) {

                //making sure that two lutemons are selected
                if(MissionManager.getCrewMembersInMission()[0] == null || MissionManager.getCrewMembersInMission()[1] == null) {
                    new AlertDialog.Builder(this)
                            .setTitle("Warning")
                            .setMessage("Two Lutemons must be selected in order to start a mission.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Handle OK click
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                // Handle Cancel click
                            })
                            .show();
                }
                //check if two lutemons are selected and they are not the same
                else if(MissionManager.getCrewMembersInMission()[0] != null && MissionManager.getCrewMembersInMission()[1] != null) {
                    if (MissionManager.getCrewMembersInMission()[0].getId() == MissionManager.getCrewMembersInMission()[1].getId()) {
                        new AlertDialog.Builder(this)
                                .setTitle("Warning")
                                .setMessage("You must select two different Lutemons to mission.")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    // Handle OK click
                                })
                                .show();
                    }
                    else{ //start mission fr now!
                        endmissionButton.setText("End mission");
                        attackButton.setVisibility(View.VISIBLE);
                        attackButton.setEnabled(MissionManager.getWhoseTurn() == MissionManager.getPlayerIndex());
                        swordView.setImageResource(R.drawable.lightsaber_left);
                        MissionManager.startMission();

                        //refresh visuals
                        refresh();

                        Log.d("DEBUG", MissionManager.getMissionDescription());

                        //apply colors to dropdown to show which lutemon is the player's (green) and the computer's (red)
                        GradientDrawable drawable1 = new GradientDrawable();
                        drawable1.setColor(Color.GREEN); // Background color
                        drawable1.setStroke(4, Color.GREEN); // Border width and color
                        drawable1.setCornerRadius(16); // Optional: rounded corners
                        // Apply it to the dropdown
                        dropdownList.get(MissionManager.getPlayerIndex()).setDropDownBackgroundDrawable(drawable1);

                        //apply colors to dropdown to show which lutemon is the player's (green) and the computer's (red)
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

    public void swordAnimation(ImageView swordView){
        ObjectAnimator flipAnimator = ObjectAnimator.ofFloat(swordView, "scaleX", 1f, -1f);
        flipAnimator.setDuration(500); // time to flip
        flipAnimator.setRepeatMode(ValueAnimator.REVERSE); // flip back
        flipAnimator.setRepeatCount(1); // do it only once (flip there and back)
        flipAnimator.start();
        swordView.animate()
                .scaleX(-1f)   // flip horizontally
                .setDuration(300)
                .start();
    }
    public void oneWayAnmiation(ImageView swordView){
        swordView.animate()
                .scaleX((-1) * swordView.getScaleX())   // flip horizontally
                .setDuration(300)
                .start();
    }


    public void setViewValues(CrewMember lutemon, int index){
        atks.get(index).setText(String.valueOf(lutemon.getAtk()));
        hps.get(index).setText(String.valueOf(lutemon.getHp()));
        defs.get(index).setText(String.valueOf(lutemon.getDef()));
        xps.get(index).setText(String.valueOf(lutemon.getXp()));
        images.get(index).setImageResource(CrewMemberManager.getSkin(lutemon));
        images.get(index).setImageBitmap(ImageTinter.tintWithoutBlack(images.get(index), lutemon));
    }

    public void refresh(){
        attackButton.setEnabled(MissionManager.getWhoseTurn() == MissionManager.getPlayerIndex());
        missionDescription.setText(MissionManager.getMissionDescription());
        oneWayAnmiation(swordView);
    }

}