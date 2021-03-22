package com.example.tripremainder.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final Context context;
    private ArrayList<HomeList> listdata;
    private static final String TAG = "RecyclerView";


    // RecyclerView recyclerView;
    public HomeAdapter(Context context,ArrayList<HomeList> listdata) {
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
        holder.tripName.setText(listdata.get(position).getTripName());
        holder.startPoint.setText(listdata.get(position).getStartPoint());
        holder.endPoint.setText(listdata.get(position).getEndPoint());
        holder.tripTime.setText(listdata.get(position).getTripTime());
        holder.tripDate.setText(listdata.get(position).getTripDate());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click on item: " + listdata.get(position).getTripName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.startBtn.setOnClickListener(v->{

            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+holder.endPoint.getText().toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        });
        Log.i(TAG, "onBindViewHolder:");
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}
