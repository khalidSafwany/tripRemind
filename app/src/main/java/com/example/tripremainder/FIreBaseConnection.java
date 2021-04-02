package com.example.tripremainder;

import android.content.Context;
import android.os.Handler;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.DataBase.RoomDB;
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
    private RoomDB localDataBase;
    private  final static String usersPath = "users";
    private final static String comingTripsPath = "comingTrips";
    private final static String userEmailPath = "userEmail";
    private final static String historyTripsPath = "historyTrips";

    String userId;
    int i;

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


    public void syncFullData(ArrayList<NewTrip>comingTrips, ArrayList<NewTrip>historyTrips, Context context){
        localDataBase = RoomDB.getInstance(context);
        databaseRef.child(usersPath).child(userId).child(comingTripsPath).removeValue();
        for (NewTrip trip:comingTrips){

            addNewTrip(trip);
            ArrayList<NoteModel> notes = (ArrayList<NoteModel>) localDataBase.noteDao().getAllNotes(trip.getId());

                addNotes(notes);

        }

        databaseRef.child(usersPath).child(userId).child(historyTripsPath).removeValue();
        for (NewTrip trip:historyTrips){
            addTripToHistory(trip);
            ArrayList<NoteModel> notes = (ArrayList<NoteModel>) localDataBase.noteDao().getAllNotes(trip.getId());

            addNotes(notes);
        }

    }

    public void removeNote(NoteModel note){
        new Thread(()-> databaseRef.child(usersPath).child(userId).child(comingTripsPath).child(String.valueOf(note.getTripId())).child("notes").child(String.valueOf(note.getId1())).removeValue()).start();
    }


    public void addNotes(ArrayList<NoteModel> notes){

        new Thread(()->
        {
            for(NoteModel note : notes) {
                databaseRef.child(usersPath).child(userId).child(comingTripsPath).child(String.valueOf(note.getTripId())).child("notes").child(String.valueOf(note.getId1())).setValue(note);
            }}).start();

    }

    public ArrayList<NewTrip> getTripsFromFireBase(Handler handler){
        ArrayList<NewTrip> syncData = new ArrayList<>();
        ArrayList<NoteModel> syncDataNotes = new ArrayList<>();

        databaseRef.child(usersPath).child(userId).child(comingTripsPath).get().addOnCompleteListener(task1 -> {

            for(DataSnapshot ds : task1.getResult().getChildren()) {
                NewTrip tempTrip = ds.getValue(NewTrip.class);
                syncData.add(tempTrip);
            }

            databaseRef.child(usersPath).child(userId).child(historyTripsPath).get().addOnCompleteListener(task -> {
                for(DataSnapshot ds : task.getResult().getChildren()) {
                    NewTrip tempTrip = ds.getValue(NewTrip.class);
                    syncData.add(tempTrip);
                }});

                handler.sendEmptyMessage(2);
            });
        return syncData;
    }


}