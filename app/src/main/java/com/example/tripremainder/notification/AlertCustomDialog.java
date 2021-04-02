package com.example.tripremainder.notification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bubbles.src.main.java.com.siddharthks.bubbles.FloatingBubblePermissions;
import com.example.tripremainder.Connectivity.Connectivity;
import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.FIreBaseConnection;
import com.example.tripremainder.R;
import com.example.tripremainder.SimpleService;
import com.example.tripremainder.home.HomeFragment;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class AlertCustomDialog extends AppCompatDialogFragment {
    String str;
    String triploc;
    String timeTonotify;
    int id;
    NewTrip newTrip;
    private RoomDB database;
    public AlertCustomDialog(String s,String endPoint,int tripid) {
        this.str = s;
        this.triploc=endPoint;
        this.id=tripid;
        this.setCancelable(false);
    }

    void startBubble(List<NoteModel> notes){
        FloatingBubblePermissions.startPermissionRequest(getActivity());

        try {

            FileOutputStream fos = getContext().openFileOutput("data.txt",MODE_PRIVATE);
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
        getContext().startService(new Intent(getContext(), SimpleService.class));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //newTrip=new NewTrip();

        database = RoomDB.getInstance(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() ,R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle(str);
        builder.setMessage(triploc);
        // add the buttons
        builder.setIcon(R.drawable.earthsmall);
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // NewTrip trip1=tripList.get(newTrip.getId());
                NewTrip trip1 = database.tripDaos().getTripById((int) id);
                List<NoteModel> notes = database.noteDao().getAllNotes(trip1.getId());
                startBubble(notes);
                String start = trip1.getStartPoint();
                String end = trip1.getEndPoint();
                if(trip1.getDirection().equals("Round trip")){
                    trip1.setDirection("One way");
                    trip1.setEndPoint(start);
                    trip1.setStartPoint(end);
                    database.tripDaos().updateTripEndPoint(trip1.getId() , trip1.getEndPoint());
                    database.tripDaos().updateTripStartPoint(trip1.getId() , trip1.getStartPoint());
                    database.tripDaos().updateTripDirection(trip1.getId() , trip1.getDirection());
                    database.tripDaos().updateTripBackDate(trip1.getId() , trip1.getTripBackDate());
                    database.tripDaos().updateTripBackTime(trip1.getId() , trip1.getTripBackTime());

                    timeTonotify = trip1.getTripBackTime();
                    setAlarm(trip1.getTripName(), trip1.getTripBackDate(), start, (int) trip1.getId(), false);

                }else{
                    trip1.setState(1);
                    trip1.setStateType("Done");
                    database.tripDaos().updateTripState(trip1.getId() , trip1.getState());
                    database.tripDaos().updateTripStateType(trip1.getId() , trip1.getStateType());
                    FIreBaseConnection con = new FIreBaseConnection();
                    con.addTripToHistory(trip1);
                    con.deleteTrip(trip1.getId());

                }
                try {
                    HomeFragment.isSyncNeeded = !Connectivity.checkConnection();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                if(!HomeFragment.isSyncNeeded) {

                    FIreBaseConnection connection = new FIreBaseConnection();
                    connection = new FIreBaseConnection();
                    connection.updateTrip(newTrip);
                }

                //database.tripDaos().delete(newTrip);
                //tripList.remove(newTrip.getId());
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+triploc);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                getActivity().finishAffinity();
            }
        });
        builder.setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), AlarmBrodcast.class);
                intent.putExtra("event", str);
                intent.putExtra("end", triploc);
                intent.putExtra("tripID",id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                am.set(AlarmManager.RTC_WAKEUP, 1000, pendingIntent);
                getActivity().finishAffinity();

                }


        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newTrip = database.tripDaos().getTripById((int) id);
                newTrip.setState(2);
                newTrip.setStateType("Cancelled");
                database.tripDaos().updateTripState(newTrip.getId() , newTrip.getState());
                database.tripDaos().updateTripStateType(newTrip.getId() , newTrip.getStateType());

                try {
                    HomeFragment.isSyncNeeded = !Connectivity.checkConnection();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                if(!HomeFragment.isSyncNeeded) {

                    FIreBaseConnection connection = new FIreBaseConnection();
                    connection = new FIreBaseConnection();
                    connection.updateTrip(newTrip);
                }
                dialog.dismiss();
                getActivity().finishAffinity();

            }
        });


        // create and show the alert dialog

        return builder.create();



    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setAlarm(String tripName, String date, String endLocation,int tripId, boolean isRound) {

        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext().getApplicationContext(), DialogCast.class);
        intent.putExtra("event", tripName);
        intent.putExtra("end", endLocation);
        intent.putExtra("id", tripId);
        intent.putExtra("isRound", false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;

        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.setExact(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




}