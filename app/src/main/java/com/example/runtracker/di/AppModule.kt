package com.example.runtracker.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.runtracker.dao.RunDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, RunDatabase::class.java,"Run_Database"
    )

    @Singleton
    @Provides
    fun provideRunDao(db: RunDatabase) =db.getDAO()
}