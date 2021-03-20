package com.example.tripremainder.home;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView tripName;
    public TextView startPoint;
    public TextView endPoint;
    public TextView tripTime;
    public TextView tripDate;
    public ConstraintLayout constraintLayout;

    public ViewHolder(View itemView) {
        super(itemView);
        tripName = itemView.findViewById(R.id.trip_name);
        startPoint = itemView.findViewById(R.id.start_point);
        endPoint = itemView.findViewById(R.id.end_point);
        tripTime = itemView.findViewById(R.id.trip_time);
        tripDate = itemView.findViewById(R.id.trip_date);

        constraintLayout = itemView.findViewById(R.id.homeConstraint);
    }
}