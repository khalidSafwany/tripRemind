package com.example.tripremainder.home.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripremainder.AddNewTripActivity;
import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;
import com.example.tripremainder.notes.AddNote;
import com.example.tripremainder.notes.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    TextView tripname;
    TextView startpoint;
    TextView endpoint;
    TextView date;
    TextView time;
    TextView state;
    RecyclerView recyclerView;
    Toolbar toolbarTop ;
    TextView mTitle ;

    List<NoteModel> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    NoteAdapter adapter;
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("Trip Details");
         toolbarTop = findViewById(R.id.toolbar);
         mTitle = toolbarTop.findViewById(R.id.toolbar_title);
         mTitle.setText("Trip Details");
        Intent intent = getIntent();
        tripname=findViewById(R.id.nameOfTrip);
        startpoint=findViewById(R.id.startpoint);
        endpoint=findViewById(R.id.endPoint);
        date=findViewById(R.id.date_txt);
        time=findViewById(R.id.timetxt);
        state=findViewById(R.id.state);
        tripname.setText(intent.getStringExtra("tripname"));
        startpoint.setText(intent.getStringExtra("tripstart"));
        endpoint.setText(intent.getStringExtra("tripend"));
        date.setText(intent.getStringExtra("tripdate"));
        time.setText(intent.getStringExtra("triptime").toString().trim());
        state.setText(intent.getStringExtra("tripstate"));
        recyclerView = findViewById(R.id.recyclerView);
        database = RoomDB.getInstance(this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        dataList.addAll(database.noteDao().getAllNotes((intent.getIntExtra("tripId" , 0))));
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoteAdapter(dataList, DetailsActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}