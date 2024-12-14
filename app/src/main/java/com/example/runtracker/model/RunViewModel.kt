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
class RunViewModel @Inject constructor(
    val runDAO: RunDAO
) : ViewModel(){

    val stateRun = MutableLiveData<RunViewState>()

     fun getRun(run: Run){
         RunViewState.LOADING(true)
        viewModelScope.launch{
            try {
                val result = runDAO.getAllRuns()
                stateRun.postValue(RunViewState.GETRUNS(result))
                RunViewState.LOADING(false)
            }catch (e: Exception){
                RunViewState.ERROR(e.message.toString())
            }
        }
    }

    sealed class RunViewState{
        data class GETRUNS(val run: List<Run>): RunViewState()
        data class LOADING(val loading: Boolean = false): RunViewState()
        data class ERROR(val ErrorMessage: String): RunViewState()
    }
}