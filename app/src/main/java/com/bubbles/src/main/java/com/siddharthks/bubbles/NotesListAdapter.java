package com.bubbles.src.main.java.com.siddharthks.bubbles;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tripremainder.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder>{

    private ArrayList<DataClass> dataList;

    public NotesListAdapter(ArrayList<DataClass> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public NotesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.note_cell,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.ViewHolder holder, int position) {

        holder.NoteTextView.setText(dataList.get(position).noteText);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView NoteTextView;

        //public ConstraintLayout constraintLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            this.NoteTextView = (TextView) itemView.findViewById(R.id.noteText);
            //  constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.);
        }
    }
}

