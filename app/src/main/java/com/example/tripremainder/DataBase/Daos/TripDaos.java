package com.example.tripremainder.DataBase.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tripremainder.DataBase.Model.NewTrip;

import java.util.List;

@Dao
public interface TripDaos {

    @Insert
    void insertTrip(NewTrip trip);

    @Delete
    void delete(NewTrip trip);

    @Query("UPDATE newtrip set tripName = :name and startPoint = :startPoint and endPoint = :endPoint and tripTime =:tripTime and tripDate =:tripDate WHERE id = :id")
    void updateTrip(String name , String startPoint , String endPoint , String tripTime , String tripDate ,int id);

    @Query("UPDATE newtrip set tripName = :name WHERE id = :id")
    void updateTripName(int id ,String name);

    @Query("UPDATE newtrip set startPoint = :startPoint WHERE id = :id")
    void updateTripStartPoint(int id ,String startPoint);

    @Query("UPDATE newtrip set endPoint = :endPoint WHERE id = :id")
    void updateTripEndPoint(int id ,String endPoint);

    @Query("UPDATE newtrip set tripTime = :tripTime WHERE id = :id")
    void updateTripTime(int id ,String tripTime);

    @Query("UPDATE newtrip set tripDate = :date WHERE id = :id")
    void updateTripDate(int id ,String date);


    @Query("UPDATE newtrip set state = :state WHERE id = :id")
    void updateTripState(int id ,int state);

    @Query("select * from NewTrip where state = 0")
    List<NewTrip> getUpcomingTrips();

    @Query("select * from NewTrip where state != 0")
    List<NewTrip> getHistoryTrips();

    @Query("select * from NewTrip where email = :mail ")
    List<NewTrip> getAllTripsWithMail(String mail);



}
