package com.example.tripremainder;

import com.example.tripremainder.home.HomeList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class FIreBaseConnection {
    private final DatabaseReference databaseRef;
    String userId;

    public FIreBaseConnection(){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseRef = database.getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            userId = user.getUid();
    }


    public void createNewUser(String userEmail){

        databaseRef.child("users").child(userId).child("userEmail").setValue(userEmail);
    }


    public void addNewTrip(HomeList newTrip){
//change getTripName to get tripID
        databaseRef.child("users").child(userId).child("comingTrips").child(newTrip.getTripName()).setValue(newTrip);

    }
    public void deleteTrip(String tripName) {
        //change getTripName to get tripID
         databaseRef.child("users").child(userId).child("comingTrips").child(tripName).removeValue();

    }

    public void updateTrip(HomeList trip) {
        //databaseRef.child("users").child(userId).child("comingTrips").child(oldTripName).removeValue();
        //change getTripName to get tripID
        databaseRef.child("users").child(userId).child("comingTrips").child(trip.getTripName()).setValue(trip);

       // databaseRef.child("users").child(userId).child("comingTrips").child(trip.getTripName()).updateChildren((Map<String, Object>) trip);
    }
}