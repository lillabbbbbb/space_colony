package com.example.spaceCrew.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.spaceCrew.R;
import com.example.spaceCrew.utils.ImageTinter;
import com.example.spaceCrew.utils.CrewMemberManager;
import com.example.spaceCrew.utils.CrewMemberStatistics;

import java.util.List;

public class DropdownAdapter extends ArrayAdapter<DropdownAdapter.DropdownItem> {

    private Context context;
    private List<DropdownItem> items;

    public DropdownAdapter(Context context, List<DropdownItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_item_dropdown, parent, false);
        }

        DropdownItem item = items.get(position);

        ImageView icon = view.findViewById(R.id.item_icon);
        TextView text = view.findViewById(R.id.item_text);


        icon.setImageResource(item.iconRes);


        //if the choice is one of the crewMembers, not the overall chart
        if(!item.toString().equals(CrewMemberStatistics.overall)){
            int tintR = Color.red(CrewMemberManager.getInstance().getCrewMembers().get(position).getColor());
            int tintG = Color.green(CrewMemberManager.getInstance().getCrewMembers().get(position).getColor());
            int tintB = Color.blue(CrewMemberManager.getInstance().getCrewMembers().get(position).getColor());
            icon.setImageBitmap(ImageTinter.tintWithoutBlack(icon, tintR, tintG, tintB));
        }

        text.setText(item.text);

        return view;
    }

    public static class DropdownItem {
        public String text;
        public int iconRes;

        public DropdownItem(String text, int iconRes) {
            this.text = text;
            this.iconRes = iconRes;
        }

        public String getText(){
            return this.text;
        }

        @NonNull
        @Override
        public String toString(){
            return this.text;
        }
    }
}
