package com.example.tripremainder.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;

public class DialogCast extends BroadcastReceiver {
    RoomDB database;
    @Override
    public void onReceive(Context context, Intent intent) {

        String text = intent.getStringExtra("event");
        String endLoc = intent.getStringExtra("end");
        int tripId = intent.getIntExtra("id",1);
        boolean isRound = intent.getBooleanExtra("isRound",false);

        if(isRound){
            database = RoomDB.getInstance(context);
            NewTrip showUpTrip = database.tripDaos().getTripById(tripId);
            Intent g2 = new Intent(context, DialogMessageActivity.class);
            g2.putExtra("tripName", text);
            g2.putExtra("tripLocation",showUpTrip.getStartPoint());
            g2.putExtra("tripID", tripId);
            g2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(g2);
        }
        else {
            Intent g2 = new Intent(context, DialogMessageActivity.class);
            g2.putExtra("tripName", text);
            g2.putExtra("tripLocation", endLoc);
            g2.putExtra("tripID", tripId);
            g2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(g2);
        }
    }
}
