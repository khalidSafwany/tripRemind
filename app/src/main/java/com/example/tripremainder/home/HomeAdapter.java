package com.example.tripremainder.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.R;

public class HomeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final Context context;
    private HomeList[] listdata;
    private static final String TAG = "RecyclerView";

    // RecyclerView recyclerView;
    public HomeAdapter(Context context, HomeList[] listdata) {
        this.context = context;
        this.listdata = listdata;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(recyclerView.getContext());
        View listItem = layoutInflater.inflate(R.layout.home_rv, recyclerView, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        Log.i(TAG, "onCreateViewHolder:");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tripName.setText(listdata[position].getTripName());
        holder.startPoint.setText(listdata[position].getStartPoint());
        holder.endPoint.setText(listdata[position].getEndPoint());
        holder.tripTime.setText(listdata[position].getTripTime());
        holder.tripDate.setText(listdata[position].getTripDate());

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
