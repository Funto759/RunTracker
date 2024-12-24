package com.example.runtracker.dao

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "running_table")
data class Run(
    val img: Bitmap? =null,
    val timestamp: Long= 0L,
    val avgSpeedInKMH: Float =0f ,
    val timeInMilliSec:Long = 0L,
    val distanceInMeters: Int =0,
    val caloriesBurned: Int =0,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0
)
