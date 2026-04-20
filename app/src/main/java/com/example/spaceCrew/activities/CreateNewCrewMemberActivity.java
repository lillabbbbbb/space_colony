package com.example.spaceCrew.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spaceCrew.R;
import com.example.spaceCrew.crewMembers.CrewMember;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.ImageTinter;
import com.example.spaceCrew.utils.CrewMemberManager;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateNewCrewMemberActivity extends AppCompatActivity {

    Button createButton;
    Button cancelButton;
    EditText nameField;
    Spinner spinner;
    ImageView crewMemberImage;
    private View colorPreview;
    TextView resense, attack, health, experience;
    private int resaultColor = Color.BLACK;
    private int tintR = 255, tintG = 255, tintB = 255;

    public void cancel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, resaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // User selected a color
                resaultColor = color;
                colorPreview.setBackgroundColor(color);
                tintR = Color.red(color);     // returns 0–255
                tintG = Color.green(color); // returns 0–255
                tintB = Color.blue(color);   // returns 0–255

                crewMemberImage.setImageBitmap(ImageTinter.tintWithoutBlack(crewMemberImage, tintR, tintG, tintB));
            }
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // User cancelled
            }
        });
        colorPicker.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_new_crew_member);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createButton = findViewById(R.id.button4);
        cancelButton = findViewById(R.id.button3);
        nameField = findViewById(R.id.nameField);
        crewMemberImage = findViewById(R.id.imageView);

        colorPreview = findViewById(R.id.colorPreview);
        Button buttonPickColor = findViewById(R.id.buttonPickColor);

        buttonPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintR = 255;
                tintG = 255;
                tintB = 255;

                openColorPicker();
            }
        });

        AutoCompleteTextView dropdown = findViewById(R.id.ageDropdown);
        String[] items = {CrewMemberManager.PILOT, CrewMemberManager.ENGINEER, CrewMemberManager.MEDIC,CrewMemberManager.SOLDIER, CrewMemberManager.SCIENTIST};

        // Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                items
        );

        // Set adapter to AutoCompleteTextView
        dropdown.setAdapter(adapter);

        //Set item selected by resault
        if (adapter.getCount() > 0) {
            String resaultItem = adapter.getItem(0);
            dropdown.setText(resaultItem, false);
        }
        // Trigger the dropdown’s listener
        AdapterView.OnItemClickListener listener = dropdown.getOnItemClickListener();
//        if (listener != null) {
//            listener.onItemClick(
//                    (AdapterView<?>)dropdown,               // parent
//                    null,                               // view (you can inflate if needed)
//                    0,                           // position
//                    adapter.getItemId(0)         // id
//            );
//        }

        final int[] res = new int[1];
        final int[] skill = new int[1];
        final int[] energy = new int[1];
        final int[] xp = new int[1];
        final String[] type = new String[1];

        crewMemberImage.setImageResource(R.drawable.pilot);
        res[0] = 4;
        skill[0] = 5;
        energy[0] = 20;
        xp[0] = 0;
        type[0] = CrewMemberManager.PILOT;

        resense = findViewById(R.id.resense2);
        resense.setText(String.valueOf(res[0]));
        attack = findViewById(R.id.attack2);
        attack.setText(String.valueOf(skill[0]));
        health = findViewById(R.id.health2);
        health.setText(String.valueOf(energy[0]));
        experience = findViewById(R.id.xp2);
        experience.setText(String.valueOf(xp[0]));


// Handle selection
        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            type[0] = parent.getItemAtPosition(position).toString();
            Log.i("TAG", "Selected type: " + type[0]);

            // Example navigation
            switch (type[0]) {
                case CrewMemberManager.MEDIC:
                    crewMemberImage.setImageResource(R.drawable.medic);
                    res[0] = 2;
                    skill[0] = 7;
                    energy[0] = 18;
                    xp[0] = 0;
                    break;
                case CrewMemberManager.ENGINEER:
                    crewMemberImage.setImageResource(R.drawable.engineer);
                    res[0] = 3;
                    skill[0] = 6;
                    energy[0] = 19;
                    xp[0] = 0;
                    break;
                case CrewMemberManager.PILOT:
                    crewMemberImage.setImageResource(R.drawable.pilot);
                    res[0] = 4;
                    skill[0] = 5;
                    energy[0] = 20;
                    xp[0] = 0;
                    break;
                case CrewMemberManager.SOLDIER:
                    crewMemberImage.setImageResource(R.drawable.soldier);
                    res[0] = 0;
                    skill[0] = 9;
                    energy[0] = 16;
                    xp[0] = 0;
                    break;
                case CrewMemberManager.SCIENTIST:
                    crewMemberImage.setImageResource(R.drawable.scientist);
                    res[0] = 1;
                    skill[0] = 8;
                    energy[0] = 17;
                    xp[0] = 0;
                    break;
                default:
                    crewMemberImage.setImageResource(R.drawable.pilot);
                    res[0] = 4;
                    skill[0] = 5;
                    energy[0] = 20;
                    xp[0] = 0;
                    break;
            }
            //apply the previously selected color for the new skin
            crewMemberImage.setImageBitmap(ImageTinter.tintWithoutBlack(crewMemberImage, tintR, tintG, tintB));

            resense.setText(String.valueOf(res[0]));
            attack.setText(String.valueOf(skill[0]));
            health.setText(String.valueOf(energy[0]));
            experience.setText(String.valueOf(xp[0]));
        });


        //"Create" button click event
        createButton.setOnClickListener(view -> {
            String name = nameField.getText().toString();
            if(name.isEmpty()){
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Please enter a valid name.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Handle OK click
                        })
                        .show();
            }else{
                CrewMemberManager.createCrewMember(new CrewMember(name, tintR, tintG, tintB, res[0], skill[0], energy[0], xp[0], type[0]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    Log.i("TAG", CrewMemberManager.getInstance().getCrewMembers().getLast().getName() + " created.");
                    Log.i("TAG", CrewMemberManager.getInstance().getCrewMembers().getLast().getMemberType() + " created.");

                }
                ActivityNavigator.goBack(this);
            }
        });


        //Cancel
        cancelButton.setOnClickListener(view -> {
            ActivityNavigator.goBack(this);
        });
    }

    public int getTintR() {
        return tintR;
    }

    public void setTintR(int tintR) {
        this.tintR = tintR;
    }

    public int getTintG() {
        return tintG;
    }

    public void setTintG(int tintG) {
        this.tintG = tintG;
    }

    public int getTintB() {
        return tintB;
    }

    public void setTintB(int tintB) {
        this.tintB = tintB;
    }
}