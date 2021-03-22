package com.example.tripremainder.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.tripremainder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private OnFragmentItemSelectedListener listener;
    private FloatingActionButton floatButtonAction;
    private ArrayList<HomeList> myListData;
    private HomeAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
      /*  Button clickMe = view.findViewById(R.id.clickMe);
        clickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onButtonSelected();
            }
        });*/
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
        myListData = new ArrayList<>();
        adapter = new HomeAdapter(getContext(),myListData);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {


                HomeList result= (HomeList) data.getSerializableExtra("result");
               myListData.add(result);
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