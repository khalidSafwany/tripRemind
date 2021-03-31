package com.example.tripremainder.history;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    public TextView tripName;
    public TextView startPoint;
    public TextView endPoint;
    public TextView tripTime;
    public TextView tripDate;
    public TextView status;
    public ImageView delete;
    Button showNote;

    public ConstraintLayout constraintLayout;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        tripName = itemView.findViewById(R.id.trip_name);
        startPoint = itemView.findViewById(R.id.start_point);
        endPoint = itemView.findViewById(R.id.end_point);
        tripTime = itemView.findViewById(R.id.trip_time);
        tripDate = itemView.findViewById(R.id.trip_date);
        status = itemView.findViewById(R.id.status);
        delete = itemView.findViewById(R.id.delete_trip);
        constraintLayout = itemView.findViewById(R.id.homeConstraint);
        showNote = itemView.findViewById(R.id.viewNote_btn);
    }
}