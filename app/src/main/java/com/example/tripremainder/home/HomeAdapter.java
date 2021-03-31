package com.example.tripremainder.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.AddNewTripActivity;
import com.example.tripremainder.FIreBaseConnection;
import com.example.tripremainder.R;
import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.home.details.DetailsActivity;
import com.example.tripremainder.notes.AddNote;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final Context context;
    private static final String TAG = "RecyclerView";

    private List<NewTrip>tripList;
    private RoomDB database;
    private Activity con;


    // RecyclerView recyclerView;
    public HomeAdapter(Context context,List<NewTrip> listdata) {
        this.context = context;
        this.tripList = listdata;
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

        // Hager code
        NewTrip trip = tripList.get(position);
        //initalize database
        database = RoomDB.getInstance(con);

        holder.tripName.setText(trip.getTripName());
        holder.startPoint.setText(trip.getStartPoint());
        holder.endPoint.setText(trip.getEndPoint());
        holder.tripDate.setText(trip.getTripDate());
        holder.tripTime.setText(trip.getTripTime());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("tripname",trip.getTripName());
                intent.putExtra("tripstart",trip.getStartPoint());
                intent.putExtra("tripend",trip.getEndPoint());
                intent.putExtra("tripdate",trip.getTripDate());
                intent.putExtra("triptime",trip.getTripTime());
                intent.putExtra("tripstate",trip.getStateType());
                context.startActivity(intent);
            }
        });
        holder.startBtn.setOnClickListener(v->{

            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+holder.endPoint.getText().toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);

            NewTrip trip1 = tripList.get(position);
            trip1.setState(1);
            trip1.setStateType("Done");
            database.tripDaos().updateTripState(trip1.getId() , trip1.getState());
            database.tripDaos().updateTripStateType(trip1.getId() , trip1.getStateType());
            tripList.remove(position);
            notifyDataSetChanged();

        });
        Log.i(TAG, "onBindViewHolder:");


        holder.deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {

                NewTrip trip = tripList.get(holder.getAdapterPosition());
                //database.tripDaos().delete(trip);
                trip.setState(2);
                trip.setStateType("Cancelled");
                database.tripDaos().updateTripState(trip.getId() , trip.getState());
                database.tripDaos().updateTripStateType(trip.getId() , trip.getStateType());
                notifyDataSetChanged();
                FIreBaseConnection conn = new FIreBaseConnection();
                conn.deleteTrip(trip.getId());
                tripList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,tripList.size());
            }
        });

        holder.updateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTrip trip = tripList.get(position);
                Intent intent = new Intent(context , AddNewTripActivity.class);
                intent.putExtra("UpdatedTrip",trip);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                context.startActivity(intent);
            }
        });
        holder.AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddNote.class);
                context.startActivity(intent);
            }
        });
        holder.notestrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.notesdialog, viewGroup, false);
                builder.setView(dialogView);
                final Button btnClose = dialogView.findViewById(R.id.buttonClose);


                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
