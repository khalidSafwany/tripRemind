package com.example.tripremainder;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class FIreBaseConnection {
    private final DatabaseReference databaseRef;
    private  final static String usersPath = "users";
    private final static String comingTripsPath = "comingTrips";
    private final static String userEmailPath = "userEmail";
    private final static String historyTripsPath = "historyTrips";

    String userId;

    public FIreBaseConnection(){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseRef = database.getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            userId = user.getUid();
    }


    public void createNewUser(String userEmail){
        new Thread(() -> databaseRef.child(usersPath).child(userId).child(userEmailPath).setValue(userEmail)).start();

    }


    public void addNewTrip(NewTrip newTrip){
//change getTripName to get tripID
        new Thread(() ->  databaseRef.child(usersPath).child(userId).child(comingTripsPath).child(String.valueOf(newTrip.getId())).setValue(newTrip)).start();


    }
    public void deleteTrip(int tripId) {
        //change getTripName to get tripID
        new Thread(() -> databaseRef.child(usersPath).child(userId).child(comingTripsPath).child(String.valueOf(tripId)).removeValue()).start();


    }

    public void updateTrip(NewTrip trip) {
        //databaseRef.child("users").child(userId).child("comingTrips").child(oldTripName).removeValue();
        //change getTripName to get tripID
        new Thread(() -> databaseRef.child(usersPath).child(userId).child(comingTripsPath).child(String.valueOf(trip.getId())).setValue(trip)).start();


       // databaseRef.child("users").child(userId).child("comingTrips").child(trip.getTripName()).updateChildren((Map<String, Object>) trip);
    }

    public void addTripToHistory(NewTrip newTrip){
//change getTripName to get tripID
        new Thread(() -> databaseRef.child(usersPath).child(userId).child(historyTripsPath).child(String.valueOf(newTrip.getId())).setValue(newTrip)).start();


    }


    public void syncFullData(ArrayList<NewTrip>comingTrips, ArrayList<NewTrip>historyTrips){
        databaseRef.child(usersPath).child(userId).child(comingTripsPath).removeValue();
        for (NewTrip trip:comingTrips){
            addNewTrip(trip);
        }

        databaseRef.child(usersPath).child(userId).child(historyTripsPath).removeValue();
        for (NewTrip trip:historyTrips){
            addTripToHistory(trip);
        }

    }


    public ArrayList<NewTrip> getTripsFromFireBase(){
        ArrayList<NewTrip> syncData = new ArrayList<>();
        databaseRef.child(usersPath).child(userId).child(comingTripsPath).get().addOnCompleteListener(task1 -> {

            for(DataSnapshot ds : task1.getResult().getChildren()) {
                NewTrip tempTrip = ds.getValue(NewTrip.class);
                syncData.add(tempTrip);
            }

                    });

            databaseRef.child(usersPath).child(userId).child(historyTripsPath).get().addOnCompleteListener(task2 -> {

                for(DataSnapshot ds : task2.getResult().getChildren()) {
                    NewTrip tempTrip = ds.getValue(NewTrip.class);
                    syncData.add(tempTrip);
                }

        });

        return syncData;
    }


}