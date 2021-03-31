package com.example.tripremainder.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RoomDB database;
    private List<NewTrip> tripHistoryList;
    HistoryAdapter adapter;

    // The Entry point of the database
    private FirebaseDatabase mFirebaseDatabase;
    // The Database Reference
    private DatabaseReference mDatabaseReference;
    String email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        RecyclerView recyclerView =(RecyclerView) view.findViewById(R.id.history_RV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Database Authantication
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if(mFirebaseDatabase == null){
            mFirebaseDatabase = FirebaseDatabase.getInstance( );
        }

        tripHistoryList = new ArrayList<>();
        database = RoomDB.getInstance(getContext());
        tripHistoryList = database.tripDaos().getHistoryTrips(email);


        adapter = new HistoryAdapter(getContext(),tripHistoryList);
        recyclerView.setAdapter(adapter);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            tripHistoryList.addAll(database.tripDaos().getHistoryTrips(email));
            adapter.notifyDataSetChanged();

    }
}