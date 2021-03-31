package com.example.tripremainder.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bubbles.src.main.java.com.siddharthks.bubbles.DataClass;
import com.bubbles.src.main.java.com.siddharthks.bubbles.FloatingBubblePermissions;
import com.example.tripremainder.AddNewTripActivity;
import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.FIreBaseConnection;
import com.example.tripremainder.R;
import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.SimpleService;
import com.example.tripremainder.home.details.DetailsActivity;
import com.example.tripremainder.auth.Sign_inActivity;
import com.example.tripremainder.notes.AddNote;
import com.google.firebase.auth.FirebaseAuth;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

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
                intent.putExtra("tripId",trip.getId());
                context.startActivity(intent);
            }
        });



        holder.startBtn.setOnClickListener(v->{

            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+holder.endPoint.getText().toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
            NewTrip trip1 = tripList.get(position);
            String start = trip1.getStartPoint();
            String end = trip1.getEndPoint();
            List<NoteModel> notes = database.noteDao().getAllNotes(trip1.getId());
            startBubble(notes);
            if(trip1.getDirection().equals("Round trip")){
                trip1.setDirection("One way");
                trip1.setEndPoint(start);
                trip1.setStartPoint(end);
                database.tripDaos().updateTripEndPoint(trip1.getId() , trip1.getEndPoint());
                database.tripDaos().updateTripStartPoint(trip1.getId() , trip1.getStartPoint());
                database.tripDaos().updateTripDirection(trip1.getId() , trip1.getDirection());
                database.tripDaos().updateTripBackDate(trip1.getId() , trip1.getTripBackDate());
                database.tripDaos().updateTripBackTime(trip1.getId() , trip1.getTripBackTime());
                notifyDataSetChanged();
            }else{
                trip1.setState(1);
                trip1.setStateType("Done");
                database.tripDaos().updateTripState(trip1.getId() , trip1.getState());
                database.tripDaos().updateTripStateType(trip1.getId() , trip1.getStateType());
                FIreBaseConnection con = new FIreBaseConnection();
                con.addTripToHistory(trip1);
                con.deleteTrip(trip1.getId());
                tripList.remove(position);
                notifyDataSetChanged();
            }

        });
        Log.i(TAG, "onBindViewHolder:");


        holder.deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle("Delete Trip");
                builder.setMessage("Are you sure to Delete Trip??");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewTrip trip = tripList.get(position);
                        trip.setState(2);
                        trip.setStateType("Cancelled");
                        database.tripDaos().updateTripState(trip.getId() , trip.getState());
                        database.tripDaos().updateTripStateType(trip.getId() , trip.getStateType());
                        tripList.remove(position);
                        notifyDataSetChanged();
                        FIreBaseConnection conn = new FIreBaseConnection();
                        conn.deleteTrip(trip.getId());
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,tripList.size());
                    }
                });
                builder.setNegativeButton("Cancel" , null);
                builder.create();
                builder.show();

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
                NewTrip trip = tripList.get(position);
                Intent intent = new Intent(context, AddNote.class);
                intent.putExtra("id" , trip.getId());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                context.startActivity(intent);
            }
        });
        holder.notestrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notes = "";
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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


    void startBubble(List<NoteModel> notes){
        FloatingBubblePermissions.startPermissionRequest((Activity) context);

        try {

            FileOutputStream fos = context.openFileOutput("data.txt",MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            StringBuilder data = new StringBuilder();
            for (NoteModel item : notes) {
                data.append(item.getNote());
                data.append("~");



            }
            dos.writeUTF(data.toString());
            dos.flush();

            dos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        context.startService(new Intent(context, SimpleService.class));
    }

}
