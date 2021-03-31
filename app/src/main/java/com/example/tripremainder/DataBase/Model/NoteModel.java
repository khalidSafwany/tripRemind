package com.example.tripremainder.DataBase.Model;//package com.example.tripremainder.DataBase.Model;
//
//
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//
//@Entity
//public class NoteModel {
//
//    @ForeignKey(entity = NewTrip.class, parentColumns = , childColumns = )
//    private int id;
//
//
//}

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(foreignKeys ={ @ForeignKey(entity = NewTrip.class, parentColumns = "id", childColumns = "tripId",
        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = {"tripId"}, unique = false)})
public class NoteModel implements Serializable {
    @ColumnInfo
    private int tripId;

    @ColumnInfo
    private String note;

    @PrimaryKey(autoGenerate = true)
    private int id1;

    public NoteModel() {
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id) {
        this.id1 = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}