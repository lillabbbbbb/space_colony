package com.example.spaceCrew.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceCrew.R;
import com.example.spaceCrew.crewMembers.CrewMember;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.ImageTinter;
import com.example.spaceCrew.utils.CrewMemberManager;
import com.example.spaceCrew.utils.CrewMemberStatistics;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {
    private final Context context;
    ActivityNavigator navigator;

    /**
     * Interface for item click events
     */
    public interface OnItemClickListener {
        void onItemClick();
    }

    public StatisticsAdapter(Context context) {
        this.context = context;
        navigator =  new ActivityNavigator(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_statistics, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Bind data to views
        Log.i("TAG", String.valueOf(position));
        CrewMember crewMember = CrewMemberManager.getInstance().getCrewMembers().get(position);
        holder.name.setText(crewMember.getName() + ", " + String.valueOf(crewMember.getMemberType()));
        holder.wins.setText(String.valueOf(CrewMemberStatistics.getWins(crewMember.getId())));
        holder.missions.setText(String.valueOf(CrewMemberStatistics.getMissions(crewMember.getId())));
        holder.minsTotal.setText(String.valueOf(CrewMemberStatistics.getNumOfSimulations(crewMember.getId())));
        holder.totalTime.setText(String.valueOf(CrewMemberStatistics.getTotalSimulationMins(crewMember.getId())));
        holder.avgTime.setText(String.valueOf(CrewMemberStatistics.getAvgSimulationTimes(crewMember.getId())));

        int tintR = Color.red(crewMember.getColor());     // returns 0–255
        int tintG = Color.green(crewMember.getColor()); // returns 0–255
        int tintB = Color.blue(crewMember.getColor());  // returns 0–255

        holder.crewMemberImage.setImageResource(CrewMemberManager.getSkin(crewMember));
        holder.crewMemberImage.setImageBitmap(ImageTinter.tintWithoutBlack(holder.crewMemberImage, tintR, tintG, tintB));


    }

    @Override
    public int getItemCount() {
        return CrewMemberManager.getInstance().getCrewMembers().size();
    }

    /**
     * ViewHolder class for the adapter
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, missions, wins, totalTime, minsTotal, avgTime;
        ImageView crewMemberImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            crewMemberImage = itemView.findViewById(R.id.crewMemberImage);
            missions = itemView.findViewById(R.id.missions2);
            wins = itemView.findViewById(R.id.wins2);
            avgTime = itemView.findViewById(R.id.trainingAvg2);
            totalTime = itemView.findViewById(R.id.trainingTotal2);
            minsTotal = itemView.findViewById(R.id.minsTotal2);
        }
    }
}
