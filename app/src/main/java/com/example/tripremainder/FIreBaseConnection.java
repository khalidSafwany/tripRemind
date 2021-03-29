package com.example.tripremainder;

import com.example.tripremainder.DataBase.Model.NewTrip;
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


    public void addNewTrip(NewTrip newTrip){
//change getTripName to get tripID
        databaseRef.child("users").child(userId).child("comingTrips").child(String.valueOf(newTrip.getId())).setValue(newTrip);

    }
    public void deleteTrip(int tripId) {
        //change getTripName to get tripID
         databaseRef.child("users").child(userId).child("comingTrips").child(String.valueOf(tripId)).removeValue();

    }

    public void updateTrip(NewTrip trip) {
        //databaseRef.child("users").child(userId).child("comingTrips").child(oldTripName).removeValue();
        //change getTripName to get tripID
        databaseRef.child("users").child(userId).child("comingTrips").child(String.valueOf(trip.getId())).setValue(trip);

       // databaseRef.child("users").child(userId).child("comingTrips").child(trip.getTripName()).updateChildren((Map<String, Object>) trip);
    }
}