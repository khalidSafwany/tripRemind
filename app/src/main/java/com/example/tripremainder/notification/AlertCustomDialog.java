package com.example.tripremainder.notification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bubbles.src.main.java.com.siddharthks.bubbles.FloatingBubblePermissions;
import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;
import com.example.tripremainder.SimpleService;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class AlertCustomDialog extends AppCompatDialogFragment {
    String str;
    String triploc;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle(str);
        builder.setMessage(triploc);
        // add the buttons
        builder.setIcon(R.drawable.ic_baseline_calendar_today_24);
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // NewTrip trip1=tripList.get(newTrip.getId());


                newTrip = database.tripDaos().getTripById((int) id);
                newTrip.setState(1);
                newTrip.setStateType("Done");
                List<NoteModel> notes = database.noteDao().getAllNotes(newTrip.getId());
                startBubble(notes);

                database.tripDaos().updateTripState(newTrip.getId() , newTrip.getState());
                database.tripDaos().updateTripStateType(newTrip.getId() , newTrip.getStateType());

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

                dialog.dismiss();
                getActivity().finishAffinity();

            }
        });


        // create and show the alert dialog

        return builder.create();



    }



}