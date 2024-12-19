package com.example.runtracker.model

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

    val stateRun = MutableLiveData<RunViewState>()

     fun getAllRuns(run: Run){
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


    sealed class RunViewState{
        data class GETRUNS(val run: List<Run>): RunViewState()
        data class LOADING(val loading: Boolean = false): RunViewState()
        data class ERROR(val ErrorMessage: String): RunViewState()
        object SUCCESS: RunViewState()
    }
}