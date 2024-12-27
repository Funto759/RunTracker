package com.example.runtracker.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val name:String,
    val weight : String,
    @PrimaryKey(autoGenerate = false)
    var id: Int = 1
)
