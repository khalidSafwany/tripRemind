package com.example.tripremainder;

import android.os.Bundle;

import com.bubbles.src.main.java.com.siddharthks.bubbles.DataClass;
import com.bubbles.src.main.java.com.siddharthks.bubbles.FloatingBubbleTouch;
import com.bubbles.src.main.java.com.siddharthks.bubbles.NotesListAdapter;
import com.example.tripremainder.R;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HiActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hi_activty);
        System.out.println("i am on screen");

        ArrayList<DataClass>myNotes = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = openFileInput("data.txt");
            DataInputStream dis = new DataInputStream(fis);
            String temp = dis.readUTF();
            String [] data = temp.split("~");
            for(String note : data){
                myNotes.add(new DataClass(note));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        RecyclerView recyclerView = findViewById(R.id.notesRecycleView);
        NotesListAdapter adapter = new NotesListAdapter(myNotes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("i am off");
        FloatingBubbleTouch.isOn = false;
    }


}