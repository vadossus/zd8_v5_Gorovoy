package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buses") // Specify the table name if needed
data class Bus(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val yearOfManufacture: Int,
    val numberOfSeats: Int,
    val fuelType: String,
    val maxServiceLife: Int,
    val lastMaintenanceDate: String? = null
)

