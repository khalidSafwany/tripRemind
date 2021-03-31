package com.example.tripremainder.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tripremainder.DataBase.Daos.TripDaos;
import com.example.tripremainder.DataBase.Model.NewTrip;

@Database(entities = {NewTrip.class} , version = 6 ,exportSchema = false)
 public abstract class RoomDB extends RoomDatabase {

        //Define Database Instance
        public static RoomDB roomDatabase;

        //DefineDatabase name
        private static final String DATABASE_NAME = "trib_reminder";

        public synchronized static RoomDB getInstance(Context context){
            if (roomDatabase == null){
                roomDatabase = Room.databaseBuilder(context.getApplicationContext()
                        , RoomDB.class,DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
            }
            //return Database
            return roomDatabase;
        }

        public abstract TripDaos tripDaos();



}
