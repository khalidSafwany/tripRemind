package com.example.tripremainder.DataBase.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tripremainder.DataBase.Model.NoteModel;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(NoteModel note);

    @Delete
    void delete(NoteModel note);


    @Query("select * from NoteModel where tripId = :id ")
    List<NoteModel> getAllNotes(int id);
}
