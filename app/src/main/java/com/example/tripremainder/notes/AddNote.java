package com.example.tripremainder.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class AddNote extends AppCompatActivity {

    EditText editText;
    Button btAdd,btnSave;
    RecyclerView recyclerView;

    List<NoteModel> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    NoteAdapter adapter;
    RoomDB database;
    Toolbar toolbarTop ;
    TextView mTitle ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
       // setTitle("Add Note");
        toolbarTop = findViewById(R.id.toolbar);
        mTitle = toolbarTop.findViewById(R.id.toolbar_title);
        mTitle.setText("Notes");
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
        new ItemTouchHelper(itemHelberCallBack).attachToRecyclerView(recyclerView);
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
    ItemTouchHelper.SimpleCallback itemHelberCallBack =
            new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder((viewHolder.itemView.getContext()),R.style.MyDialogTheme);
                    builder.setTitle("Delete Note");
                    builder.setMessage("Are you sure You Want to Delete Note??");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NoteModel d = dataList.get(viewHolder.getAdapterPosition());
                            database.noteDao().delete(d);
                            dataList.remove(viewHolder.getAdapterPosition());
                            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            adapter.notifyItemRangeChanged(viewHolder.getAdapterPosition(),dataList.size());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());

                        }
                    });
                    builder.create();
                    builder.show();




                }
            };

}