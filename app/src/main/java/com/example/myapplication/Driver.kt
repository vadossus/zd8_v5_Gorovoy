package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers") // Specify the table name if needed
data class Driver(
    @PrimaryKey(autoGenerate = true) // Auto-generate the ID
    val id: Long = 0, // Default value for id
    val name: String,
    val sname: String,
    val experience: Int,
    val login: String,
    val password: String
)