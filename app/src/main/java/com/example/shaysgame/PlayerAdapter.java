package com.example.shaysgame;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlayerAdapter extends ArrayAdapter<Player> {
    Context context;
    List<Player> objects;



    public PlayerAdapter( Context context, int resource, int textViewResourceId,  List<Player> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.player_layout, parent, false);

        TextView tvPlayerName = (TextView)view.findViewById(R.id.tvPlayerName);
        TextView tvPlayerPoints = (TextView)view.findViewById(R.id.tvPoints);
        ImageView ivMedal=(ImageView)view.findViewById(R.id.ivMedal);

        Player temp = objects.get(position);

        ivMedal.setImageResource(temp.getMedal());
        tvPlayerName.setText(temp.getName());
        tvPlayerPoints.setText(""+temp.getScore());

        return view;
    }
}
