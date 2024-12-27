package com.example.runtracker.model

import androidx.lifecycle.LiveData
import com.example.runtracker.dao.Run
import com.example.runtracker.dao.RunDAO
import com.example.runtracker.dao.User
import com.example.runtracker.dao.UserDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RunRepository @Inject constructor(val runDAO: RunDAO, val userDAO: UserDAO) {

    suspend fun getRuns(): List<Run> {
        Timber.d("Fetching runs from database...")
        return withContext(Dispatchers.IO) {
            runDAO.getAllRuns()
        }
    }
    suspend fun getUserDetails(): User {
        Timber.d("Fetching runs from database...")
        return withContext(Dispatchers.IO) {
            userDAO.getUserDetails()
        }
    }
    suspend fun getRunsByDistance(): List<Run> {
        Timber.d("Fetching runs from database...")
        return withContext(Dispatchers.IO) {
            runDAO.getAllRunsSortedByDistance()
        }
    }
    suspend fun getRunsByAvgSpeed(): List<Run> {
        Timber.d("Fetching runs from database...")
        return withContext(Dispatchers.IO) {
            runDAO.getAllRunsSortedByAvgSpeed()
        }
    }
 suspend fun getRunsByCaloriesBurned(): List<Run> {
        Timber.d("Fetching runs from database...")
        return withContext(Dispatchers.IO) {
            runDAO.getAllRunsSortedByCaloriesBurned()
        }
    }
    suspend fun getRunsByTimeInMilli(): List<Run> {
        Timber.d("Fetching runs from database...")
        return withContext(Dispatchers.IO) {
            runDAO.getAllRunsSortedByTimeInMillis()
        }
    }

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    suspend fun deleteUser(user: User) = userDAO.deleteRun(user)

    suspend fun insertRun(run: Run) = runDAO.insertRun(run)

    suspend fun insertUser(user: User) = userDAO.insertUser(user)

    fun getTotalAvgSpeed() = runDAO.getTotalAvgSpeed()

    fun getTotalDistance()= runDAO.getTotalDistance()

    fun getTotalCaloriesBurned() = runDAO.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = runDAO.getTotalTimeInMillis()

    fun getAllRunsSortedByDistance() = runDAO.getAllRunsSortedByDistance()

    fun getAllRunsSortedByAvgSpeed() = runDAO.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByCaloriesBurned() = runDAO.getAllRunsSortedByCaloriesBurned()

    fun getAllRunsSortedByTimeInMillis() = runDAO.getAllRunsSortedByTimeInMillis()








}