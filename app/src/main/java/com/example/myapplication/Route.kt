package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes") // Specify the table name if needed
data class Route(
    @PrimaryKey(autoGenerate = true) // Auto-generate the ID
    val id: Long = 0, // Default value for id
    val driver: String,
    val bus: Int,
    val start: String,
    val end: String
)