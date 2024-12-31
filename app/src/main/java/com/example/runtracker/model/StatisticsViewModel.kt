package com.example.runtracker.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runtracker.dao.Run
import com.example.runtracker.dao.RunDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val repository: RunRepository
) : ViewModel(){

    val totalTime : LiveData<Long> = repository.getTotalTimeInMillis()
    val totalCalories: LiveData<Int> = repository.getTotalCaloriesBurned()
    val totalDistance: LiveData<Int> = repository.getTotalDistance()
    val totalAvgSpeed: LiveData<Float> = repository.getTotalAvgSpeed()


}