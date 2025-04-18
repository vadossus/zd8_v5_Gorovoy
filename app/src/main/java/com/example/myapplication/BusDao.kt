package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BusDao {
    @Insert
    suspend fun insert(bus: Bus)

    @Update
    suspend fun update(bus: Bus)

    @Delete
    suspend fun delete(bus: Bus)

    @Query("SELECT * FROM buses")
    suspend fun getAllBuses(): List<Bus>
}