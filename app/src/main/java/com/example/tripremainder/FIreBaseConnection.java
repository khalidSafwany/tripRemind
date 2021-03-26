package com.example.tripremainder;

import com.example.tripremainder.home.HomeList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        databaseRef.child("users").child(userId).child("comingTrips").child(newTrip.getTripName()).setValue(newTrip);

    }
    public void deleteTrip(String tripName) {
         databaseRef.child("users").child(userId).child("comingTrips").child(tripName).removeValue();

    }

    public void updateTrip(HomeList trip,String oldTripName) {
        databaseRef.child("users").child(userId).child("comingTrips").child(oldTripName).removeValue();
        databaseRef.child("users").child(userId).child("comingTrips").child(trip.getTripName()).setValue(trip);
    }
}