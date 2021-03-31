package com.example.tripremainder.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;

import java.util.ArrayList;
import java.util.List;

public class AddNote extends AppCompatActivity {

    EditText editText;
    Button btAdd,btnSave;
    RecyclerView recyclerView;

    List<NoteModel> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    NoteAdapter adapter;
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setTitle("Add Note");

        editText = findViewById(R.id.edit_text);
        btAdd = findViewById(R.id.bt_add);
        btnSave = findViewById(R.id.bt_save);
        recyclerView = findViewById(R.id.recycler);
        database = RoomDB.getInstance(this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        dataList = database.noteDao().getAllNotes((intent.getIntExtra("id" , 0)));
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoteAdapter(dataList,AddNote.this);
        recyclerView.setAdapter(adapter);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                NoteModel note = new NoteModel();
                note.setTripId(intent.getIntExtra("id" , 0));
                String sText = editText.getText().toString();
                if(!sText.equals("")){
                    note.setNote(sText);
                    database.noteDao().insertNote(note);
                    dataList.clear();
                    dataList.addAll(database.noteDao().getAllNotes(note.getTripId()));
                    adapter = new NoteAdapter(dataList,AddNote.this);
                    recyclerView.setAdapter(adapter);
                    editText.setText("");
                    adapter.notifyDataSetChanged();

                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }

        });

    }

}