package com.example.tripremainder.history;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private final Context context;
    private HistoryList[] listdata;
    private static final String TAG = "RecyclerView";

    // RecyclerView recyclerView;
    public HistoryAdapter(Context context, HistoryList[] listdata) {
        this.context = context;
        this.listdata = listdata;
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
        holder.tripName.setText(listdata[position].getTripName());
        holder.startPoint.setText(listdata[position].getStartPoint());
        holder.endPoint.setText(listdata[position].getEndPoint());
        holder.tripTime.setText(listdata[position].getTripTime());
        holder.tripDate.setText(listdata[position].getTripDate());
        holder.status.setText(listdata[position].getStatus());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click on item: " + listdata[position].getTripName(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.i(TAG, "onBindViewHolder:");
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }
}
