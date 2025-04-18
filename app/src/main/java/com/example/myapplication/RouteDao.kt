package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RouteDao {
    @Insert
    suspend fun insert(route: Route)

    @Update
    suspend fun update(route: Route)

    @Delete
    suspend fun delete(route: Route)

    @Query("SELECT * FROM routes")
    suspend fun getAllRoutes(): List<Route>
}