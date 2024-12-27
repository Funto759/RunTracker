package com.example.runtracker.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runtracker.dao.Run
import com.example.runtracker.dao.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    val repository: RunRepository
) : ViewModel(){

    val stateRun = MutableLiveData<RunViewState>()
    val UserState = MutableLiveData<RunViewState>()

     fun getAllRuns(){
         RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                val result = repository.getRuns()
                stateRun.postValue(RunViewState.GETRUNS(result))
                RunViewState.LOADING(false)
            }catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }
    fun getUserDetails(){
         RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                val result = repository.getUserDetails()
                UserState.postValue(RunViewState.USERDETAILS(result))
                RunViewState.LOADING(false)
            }catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }

    fun getAllRunsByDistance(){
         RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                val result = repository.getAllRunsSortedByDistance()
                stateRun.postValue(RunViewState.GETRUNS(result))
                RunViewState.LOADING(false)
            }catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }

    fun getAllRunsByAvgSpeed(){
         RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                val result = repository.getAllRunsSortedByAvgSpeed()
                stateRun.postValue(RunViewState.GETRUNS(result))
                RunViewState.LOADING(false)
            }catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }

    fun getAllRunsByCaloriesBurned(){
         RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                val result = repository.getAllRunsSortedByCaloriesBurned()
                stateRun.postValue(RunViewState.GETRUNS(result))
                RunViewState.LOADING(false)
            }catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }

    fun getAllRunsByTimeInMilli(){
         RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                val result = repository.getAllRunsSortedByTimeInMillis()
                stateRun.postValue(RunViewState.GETRUNS(result))
                RunViewState.LOADING(false)
            }catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }

 fun deleteRun(run: Run){
     RunViewState.LOADING(true)
     viewModelScope.launch{
         try {
             val result = repository.deleteRun(run)
             RunViewState.SUCCESS
             RunViewState.LOADING(false)
         } catch (e: Exception){
             RunViewState.ERROR(e.message.toString())
         }
     }
 }

    fun insertRun(run: Run){
        RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                repository.insertRun(run)
            } catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }

    fun insertUser( user: User){
        RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                repository.insertUser(user)
            } catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }


    sealed class RunViewState{
        data class GETRUNS(val run: List<Run>): RunViewState()
        data class USERDETAILS(val user : User): RunViewState()
        data class LOADING(val loading: Boolean = false): RunViewState()
        data class ERROR(val ErrorMessage: String): RunViewState()
        object SUCCESS: RunViewState()
    }
}