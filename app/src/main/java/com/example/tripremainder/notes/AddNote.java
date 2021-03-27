package com.example.tripremainder.notes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tripremainder.R;

import java.util.ArrayList;
import java.util.List;

public class AddNote extends AppCompatActivity {

    EditText editText;
    Button btAdd,btnSave;
    RecyclerView recyclerView;

    List<NoteList> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editText = findViewById(R.id.edit_text);
        btAdd = findViewById(R.id.bt_add);
        btnSave = findViewById(R.id.bt_save);
        recyclerView = findViewById(R.id.recycler);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sText = editText.getText().toString();
                if(!sText.equals("")){
                    dataList.add(new NoteList(sText));
                    adapter = new NoteAdapter(dataList,AddNote.this);
                    recyclerView.setAdapter(adapter);
                    editText.setText("");

                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

    }

}