package com.example.tripremainder.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tripremainder.DataBase.Model.NoteModel;
import com.example.tripremainder.DataBase.RoomDB;
import com.example.tripremainder.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<NoteModel> dataList;
    private Context context;
    private RoomDB database;


    public NoteAdapter(List<NoteModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main,parent,false);

        database = RoomDB.getInstance(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // MainData data = dataList.get(position);
        NoteModel note = dataList.get(position);
        holder.textView.setText(note.getNote());

//        holder.btDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NoteModel d = dataList.get(position);
//                database.noteDao().delete(d);
//                int postion = holder.getAdapterPosition();
//                dataList.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position,dataList.size());
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
