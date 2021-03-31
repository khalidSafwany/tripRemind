package com.example.tripremainder.history;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;
import com.example.tripremainder.auth.Sign_inActivity;
import com.example.tripremainder.home.HomeActivity;
import com.example.tripremainder.home.details.DetailsActivity;
import com.google.firebase.auth.FirebaseAuth;

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
        NewTrip trip = tripList.get(position);

        database = RoomDB.getInstance(con);

        holder.tripName.setText(trip.getTripName());
        holder.startPoint.setText(trip.getStartPoint());
        holder.endPoint.setText(trip.getEndPoint());
        holder.tripTime.setText(trip.getTripTime());
        holder.tripDate.setText(trip.getTripDate());
        holder.status.setText(trip.getStateType());

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
                intent.putExtra("tripId",trip.getId());
                context.startActivity(intent);            }
        });
        Log.i(TAG, "onBindViewHolder:");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Trip");
                builder.setMessage("Are you sure to Delete Trip ??");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewTrip trip = tripList.get(position);
                        database.tripDaos().delete(trip);
                        tripList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,tripList.size());
                    }
                });
                builder.setNegativeButton("Cancel" , null);
                builder.create();
                builder.show();


            }
        });

        holder.showNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notes = "";
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.notesdialog, viewGroup, false);
                builder.setView(dialogView);
                final Button btnClose = dialogView.findViewById(R.id.buttonClose);
                TextView note_txt = dialogView.findViewById(R.id.note_txt);
                TextView note_txt1 = dialogView.findViewById(R.id.note_txt1);
                TextView note_txt2 = dialogView.findViewById(R.id.note_txt2);
                NewTrip trip = tripList.get(position);
                List<NoteModel> noteList = database.noteDao().getAllNotes(trip.getId());
                System.out.println("list size = " + noteList.size());
                int size = noteList.size();
                if(noteList.size()>=1) {
                    note_txt.setText(noteList.get(size - 1).getNote());
                }else {
                    note_txt.setText("");
                }
                if(noteList.size()>=2) {
                    note_txt1.setText(noteList.get(size - 2).getNote());
                }else {
                    note_txt1.setText("");
                }
                if(noteList.size()>=3) {
                    note_txt2.setText(noteList.get(size - 3).getNote());
                }else {
                    note_txt2.setText("");
                }
                android.app.AlertDialog alertDialog = builder.create();
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
        System.out.println("list size = "+tripList.size());
        return tripList.size();
    }
}
