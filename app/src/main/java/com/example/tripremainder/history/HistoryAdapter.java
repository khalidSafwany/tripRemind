package com.example.tripremainder.history;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private final Context context;
    private List<NewTrip>tripList;
    private RoomDB database;
    private Activity con;
    private static final String TAG = "RecyclerView";

    // RecyclerView recyclerView;
    public HistoryAdapter(Context context, List<NewTrip> listdata) {
        this.context = context;
        this.tripList = listdata;
    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(recyclerView.getContext());
        View listItem = layoutInflater.inflate(R.layout.history_rv, recyclerView, false);
        HistoryViewHolder viewHolder = new HistoryViewHolder(listItem);
        Log.i(TAG, "onCreateViewHolder:");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

        database = RoomDB.getInstance(con);

        holder.tripName.setText(tripList.get(position).getTripName());
        holder.startPoint.setText(tripList.get(position).getStartPoint());
        holder.endPoint.setText(tripList.get(position).getEndPoint());
        holder.tripTime.setText(tripList.get(position).getTripTime());
        holder.tripDate.setText(tripList.get(position).getTripDate());
       // holder.status.setText(listdata[position].getStatus());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click on item: " + tripList.get(position).getTripName(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.i(TAG, "onBindViewHolder:");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTrip trip = tripList.get(holder.getAdapterPosition());
                database.tripDaos().delete(trip);
                int postion = holder.getAdapterPosition();
                tripList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,tripList.size());
            }
        });
    }



    @Override
    public int getItemCount() {
        System.out.println("list size = "+tripList.size());
        return tripList.size();
    }
}
