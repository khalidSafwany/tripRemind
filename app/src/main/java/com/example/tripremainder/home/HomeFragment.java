package com.example.tripremainder.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.AddNewTripActivity;
import com.example.tripremainder.DataBase.Model.NewTrip;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private OnFragmentItemSelectedListener listener;
    private FloatingActionButton floatButtonAction;
    private HomeAdapter adapter;
    RoomDB database;
    private List<NewTrip> tripList;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        floatButtonAction = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatButtonAction.setOnClickListener(v->{
            Intent addTripIntent = new Intent(getActivity(), AddNewTripActivity.class);
            startActivityForResult(addTripIntent,200);
        });
        RecyclerView recyclerView =(RecyclerView) view.findViewById(R.id.home_RV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        tripList = new ArrayList<>();
        database = RoomDB.getInstance(getContext());
        tripList = database.tripDaos().getUpcomingTrips();

        adapter = new HomeAdapter(getContext(),tripList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200 ) {

            NewTrip result= (NewTrip) data.getSerializableExtra("result");
            database.tripDaos().insertTrip(result);
            tripList.clear();
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

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelected();
    }
}