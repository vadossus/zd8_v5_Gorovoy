package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DriverDao {
    @Insert
    suspend fun insert(driver: Driver)

    @Update
    suspend fun update(driver: Driver)

    @Delete
    suspend fun delete(driver: Driver)

    @Query("SELECT * FROM drivers")
    suspend fun getAllDrivers(): List<Driver>
}