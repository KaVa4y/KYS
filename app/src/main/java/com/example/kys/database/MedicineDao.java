package com.example.kys.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kys.model.Medicine;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    long insert(Medicine medicine);

    @Update
    void update(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Query("SELECT * FROM medicines WHERE isActive = 1 ORDER BY name")
    LiveData<List<Medicine>> getAllActive();

    @Query("SELECT * FROM medicines")
    List<Medicine> getAllSync();
}