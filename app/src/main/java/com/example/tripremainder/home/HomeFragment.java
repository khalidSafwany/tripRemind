package com.example.tripremainder.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.AddNewTripActivity;
import com.example.tripremainder.Connectivity.Connectivity;
import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private OnFragmentItemSelectedListener listener;
    private FloatingActionButton floatButtonAction;
    private HomeAdapter adapter;
    RoomDB database;
    private List<NewTrip> tripList;

    // The Entry point of the database
    private FirebaseDatabase mFirebaseDatabase;

    String email;

    private static boolean isSyncNeeded = false;
    private final static String isSyncNeededString = "sync";

void SyncData(){
    // implement  with getting data from database then to firebase
}
    void showSyncDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Sync");




// Set up the input
        builder.setMessage("Some Trips may be changed with no connection on \n Do you want to sync you data now?");

// Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               SyncData();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        readSyncStateFromShared();

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        floatButtonAction = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatButtonAction.setOnClickListener(v->{
            Intent addTripIntent = new Intent(getActivity(), AddNewTripActivity.class);
           // addTripIntent.putExtra("id" , NewTrip.getId());

            startActivityForResult(addTripIntent,200);
        });
        RecyclerView recyclerView =(RecyclerView) view.findViewById(R.id.home_RV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        //Database Authantication
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if(mFirebaseDatabase == null){
            mFirebaseDatabase = FirebaseDatabase.getInstance( );
        }

        tripList = new ArrayList<>();
        database = RoomDB.getInstance(getContext());
        //tripList = database.tripDaos().getUpcomingTrips();

        adapter = new HomeAdapter(getContext(),tripList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            if(isSyncNeeded && Connectivity.checkConnection()){
                showSyncDialog();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200 ) {
            assert data != null;
            isSyncNeeded = data.getBooleanExtra(isSyncNeededString,false);
            NewTrip result= (NewTrip) data.getSerializableExtra("result");
            tripList.clear();
            tripList.addAll(database.tripDaos().getUpcomingTrips(email));
            tripList.addAll(database.tripDaos().getUpcomingTrips());

            adapter.notifyDataSetChanged();


        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnFragmentItemSelectedListener){
            listener = (OnFragmentItemSelectedListener) context;

        }else {
            throw new ClassCastException(context.toString() + " must implement listener");
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        tripList.clear();
        tripList.addAll(database.tripDaos().getUpcomingTrips(email));
        adapter.notifyDataSetChanged();
    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelected();
    }
//khalid////////////////////
   void readSyncStateFromShared(){
        SharedPreferences sync = Objects.requireNonNull(this.getActivity()).getSharedPreferences(isSyncNeededString,Context.MODE_PRIVATE);
        isSyncNeeded = sync.getBoolean(isSyncNeededString,false);

    }

    @Override
    public void onStop() {
        super.onStop();
        saveSuncStateToShared();
    }

    void saveSuncStateToShared(){
        SharedPreferences sync = Objects.requireNonNull(this.getActivity()).getSharedPreferences(isSyncNeededString,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sync.edit();
        editor.putBoolean(isSyncNeededString,isSyncNeeded);
        editor.commit();

    }


}