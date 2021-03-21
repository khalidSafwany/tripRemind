package com.example.tripremainder.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripremainder.R;

public class HistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        RecyclerView recyclerView =(RecyclerView) view.findViewById(R.id.history_RV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        HistoryList[] myListData = new HistoryList[] {
                new HistoryList("Trip1", "cairo 1","Alex","3.00 pm","20-9-2021","Done"),
                new HistoryList("Trip2", "cairo 2","Alex","9.00 pm","22-9-2021","Cancelled"),


        };

        HistoryAdapter adapter = new HistoryAdapter(getContext(),myListData);
        recyclerView.setAdapter(adapter);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}