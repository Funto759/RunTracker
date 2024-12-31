package com.example.runtracker.dao

import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val name: String,
    val weight: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1
)
