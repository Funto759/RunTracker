package com.example.runtracker.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Run::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class RunDatabase : RoomDatabase() {

    abstract fun getDAO(): RunDAO
}