package com.example.tripremainder.notification;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;


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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


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
                newTrip = database.tripDaos().getTripById(id).get(0);
                newTrip.setState(1);
                database.tripDaos().updateTripState(newTrip.getId() , newTrip.getState());
                database.tripDaos().delete(newTrip);
                //tripList.remove(newTrip.getId());
              Uri gmmIntentUri = Uri.parse("geo:0,0?q="+triploc);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                getActivity().finish();
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

                }


        });
        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog

        return builder.create();



    }



}