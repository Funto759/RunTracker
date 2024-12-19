package com.example.runtracker.model

import com.example.runtracker.dao.Run
import com.example.runtracker.dao.RunDAO
import javax.inject.Inject

class RunRepository @Inject constructor(val runDAO: RunDAO) {

    fun getRuns() = runDAO.getAllRuns()

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    suspend fun insertRun(run: Run) = runDAO.insertRun(run)

    fun getTotalAvgSpeed() = runDAO.getTotalAvgSpeed()

    fun getTotalDistance()= runDAO.getTotalDistance()

    fun getTotalCaloriesBurned() = runDAO.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = runDAO.getTotalTimeInMillis()

    fun getAllRunsSortedByDistance() = runDAO.getAllRunsSortedByDistance()

    fun getAllRunsSortedByAvgSpeed() = runDAO.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByCaloriesBurned() = runDAO.getAllRunsSortedByCaloriesBurned()

    fun getAllRunsSortedByTimeInMillis() = runDAO.getAllRunsSortedByTimeInMillis()








}