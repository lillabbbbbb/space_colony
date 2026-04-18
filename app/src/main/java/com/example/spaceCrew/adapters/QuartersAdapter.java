package com.example.spaceCrew.adapters;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spaceCrew.R;
import com.example.spaceCrew.crewMembers.CrewMember;
import com.example.spaceCrew.utils.ActivityNavigator;
import com.example.spaceCrew.utils.ImageTinter;
import com.example.spaceCrew.utils.CrewMemberManager;

public class QuartersAdapter extends RecyclerView.Adapter<QuartersAdapter.ViewHolder> {
    private final Context context;
    ActivityNavigator navigator;


    public QuartersAdapter(Context context) {
        this.context = context;
        navigator =  new ActivityNavigator(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_general_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(CrewMemberManager.getInstance().getCrewMembers().get(position).getLocation().equals(ActivityNavigator.home)){
            // Bind data to views
            Log.i("TAG", String.valueOf(position));
            CrewMember crewMember = CrewMemberManager.getInstance().getCrewMembers().get(position);
            holder.name.setText(crewMember.getName() + ", " + String.valueOf(crewMember.getMemberType()));
            holder.def.setText(String.valueOf(crewMember.getDef()));
            holder.atk.setText(String.valueOf(crewMember.getAtk()));
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
        }
    }

    @Override
    public int getItemCount() {
        return (int) CrewMemberManager.getInstance().getCrewMembers().stream()
                .filter(l -> l.getLocation().equals(ActivityNavigator.home))
                .count();
    }

    /**
     * ViewHolder class for the adapter
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, atk, def, hp, xp;
        ImageView crewMemberImage;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            crewMemberImage = itemView.findViewById(R.id.crewMemberImage);
            atk = itemView.findViewById(R.id.atk2);
            def = itemView.findViewById(R.id.def2);
            xp = itemView.findViewById(R.id.xp2);
            hp = itemView.findViewById(R.id.hp2);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
