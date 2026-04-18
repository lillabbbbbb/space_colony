package com.example.spaceCrew.adapters;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceCrew.R;
import com.example.spaceCrew.crewMembers.CrewMember;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.ImageTinter;
import com.example.spaceCrew.utils.CrewMemberManager;

import java.util.ArrayList;

public class SimulatorAdapter extends RecyclerView.Adapter<SimulatorAdapter.ViewHolder> {
    private final Context context;
    ActivityNavigator navigator;
    public ArrayList<SimulatorAdapter.ViewHolder> rows;

    /**
     * Interface for item click events
     */
    public interface OnItemClickListener {
        void onItemClick();
    }

    public SimulatorAdapter(Context context) {
        this.context = context;
        navigator =  new ActivityNavigator(context);
        rows = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_simulator, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(CrewMemberManager.getInstance().getCrewMembers().get(position).getLocation().equals(ActivityNavigator.simulator)){
            // Bind data to views
            Log.i("TAG", String.valueOf(position));
            CrewMember crewMember = CrewMemberManager.getInstance().getCrewMembers().get(position);
            holder.name.setText(crewMember.getName() + ", " + String.valueOf(crewMember.getMemberType()));
            holder.def.setText(String.valueOf(crewMember.getDef()));
            holder.hp.setText(String.valueOf(crewMember.getHp()));
            holder.xp.setText(String.valueOf(crewMember.getXp()));

            int tintR = Color.red(crewMember.getColor());     // returns 0–255
            int tintG = Color.green(crewMember.getColor()); // returns 0–255
            int tintB = Color.blue(crewMember.getColor());  // returns 0–255

            holder.crewMemberImage.setImageResource(CrewMemberManager.getSkin(crewMember));
            holder.crewMemberImage.setImageBitmap(ImageTinter.tintWithoutBlack(holder.crewMemberImage, tintR, tintG, tintB));

            if (holder.checkBox.isChecked()){
                Log.i("TAG", "CheckBox is ticked.");
                CrewMemberManager.getInstance().getCrewMembers().get(position).setSelected();
            }

            holder.checkBox.setOnClickListener(view -> {
                CrewMemberManager.getInstance().getCrewMembers().get(position).setSelected();

                //fun animation
                ObjectAnimator flipAnimator = ObjectAnimator.ofFloat(holder.crewMemberImage, "scaleX", 1f, -1f);
                flipAnimator.setDuration(500); // time to flip
                flipAnimator.setRepeatMode(ValueAnimator.REVERSE); // flip back
                flipAnimator.setRepeatCount(1); // do it only once (flip there and back)
                flipAnimator.start();
            });

            //start simulator timer
            holder.chronometer.setBase(SystemClock.elapsedRealtime());
            holder.chronometer.start();

            rows.add(holder);
        }
    }

    @Override
    public int getItemCount() {
        return (int) CrewMemberManager.getInstance().getCrewMembers().stream()
                .filter(l -> l.getLocation().equals(ActivityNavigator.simulator))
                .count();
    }

    /**
     * ViewHolder class for the adapter
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, def, hp, xp;
        ImageView crewMemberImage;
        CheckBox checkBox;
        public Chronometer chronometer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            crewMemberImage = itemView.findViewById(R.id.crewMemberImage);
            chronometer = itemView.findViewById(R.id.chronometer);
            def = itemView.findViewById(R.id.def2);
            xp = itemView.findViewById(R.id.xp2);
            hp = itemView.findViewById(R.id.hp2);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
