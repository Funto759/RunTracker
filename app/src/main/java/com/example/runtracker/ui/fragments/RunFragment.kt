package com.example.runtracker.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.runtracker.R
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentRunBinding
import com.example.runtracker.model.RunViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RunFragment : ViewBindingFragment<FragmentRunBinding>(){

private val viewmodel by viewModels<RunViewModel>()

    override val LayoutId: Int
        get() = R.layout.fragment_run

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRunBinding {
       return FragmentRunBinding.inflate(inflater,container,false)
    }

    override fun run() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.stateRun.observe(viewLifecycleOwner){ state ->
                   when(state){
                       is RunViewModel.RunViewState.LOADING -> {
                           print("loading")
                       }

                       is RunViewModel.RunViewState.ERROR -> {
                           print("Error Occurred")
                       }
                       is RunViewModel.RunViewState.GETRUNS -> {
                           print("print all ${state.run.size} ")
                       }
                   }
                }
            }
        }
    }


}