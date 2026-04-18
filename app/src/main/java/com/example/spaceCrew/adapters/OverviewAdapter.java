package com.example.spaceCrew.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceCrew.R;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.CrewMemberStatistics;

import java.util.ArrayList;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {
    private final Context context;
    public static ArrayList<Button> buttons;
    ActivityNavigator navigator;

    /**
     * Interface for item click events
     */
    public interface OnItemClickListener {
        void onItemClick();
    }

    public OverviewAdapter(Context context) {
        this.context = context;
        navigator =  new ActivityNavigator(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_by_area_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Bind data to views
        holder.name.setText(CrewMemberStatistics.getOverview().get(position).get(0));
        holder.description.setText(CrewMemberStatistics.getOverview().get(position).get(1));
        holder.button.setText(CrewMemberStatistics.getOverview().get(position).get(2));
        holder.button.setTag(holder.name.getText());
        buttons = new ArrayList<>();
        buttons.add(holder.button);

        Log.i("TAG", "asdfqadvd.");
        holder.button.setOnClickListener((View view) -> {
            Log.i("TAG", "Button is clicked.");
            String selectedArea = (String) view.getTag();
            navigator.navigateTo(selectedArea);
        });


        holder.button.setOnClickListener(view -> {
                Log.i("TAG", view.getTag().toString());
                if(view.getTag().toString().equals(ActivityNavigator.simulator)){
                    ActivityNavigator.goToSimulator((AppCompatActivity) context);
                }else if(view.getTag().toString().equals(ActivityNavigator.missionControl)){
                    ActivityNavigator.goToMissionControl((AppCompatActivity) context);
                }else if(view.getTag().toString().equals(ActivityNavigator.home)){
                    ActivityNavigator.goToQuarters((AppCompatActivity) context);
                }else if(view.getTag().toString().equals("Statistics")){
                    ActivityNavigator.goToStats((AppCompatActivity) context);
                }
        });
    }

    @Override
    public int getItemCount() {
        return ActivityNavigator.getActions().size();
    }

    /**
     * ViewHolder class for the adapter
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            button = itemView.findViewById(R.id.button5);
        }
    }
}
