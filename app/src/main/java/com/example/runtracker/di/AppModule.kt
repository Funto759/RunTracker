package com.example.runtracker.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.runtracker.dao.RunDAO
import com.example.runtracker.dao.RunDatabase
import com.example.runtracker.dao.UserDAO
import com.example.runtracker.dao.UserDatabase
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

    @Provides
    @Singleton
    fun provideRunDatabase(@ApplicationContext context: Context): RunDatabase {
       return Room.databaseBuilder(
            context, RunDatabase::class.java, "Run_Database"
        ).build()
    }
    @Provides
    @Singleton
    fun provideUsersDatabase(@ApplicationContext context: Context): UserDatabase{
       return Room.databaseBuilder(
            context, UserDatabase::class.java, "Users_Database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRunDao(database: RunDatabase): RunDAO{
        return database.getDAO()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDatabase): UserDAO{
        return database.getDAO()
    }
}