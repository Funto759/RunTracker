package com.example.runtracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.runtracker.dao.User
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentSettingsBinding
import com.example.runtracker.model.RunViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {
    override val LayoutId: Int
        get()= com.example.runtracker.R.layout.fragment_settings

    private val viewmodel by viewModels<RunViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getUserDetails()
        binding.apply{
           val name = etName.text
            val weight = etWeight.text
            val user = User(name.toString(),weight.toString(),1)
            btnApplyChanges.setOnClickListener {
                viewmodel.insertUser(user)
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater,container,false)
    }

    override fun run() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewmodel.stateRun.observe(viewLifecycleOwner){ state ->
                when(state){
                    is RunViewModel.RunViewState.LOADING -> {
                        print("loading")
                    }

                    is RunViewModel.RunViewState.ERROR -> {
                        print("Error Occurred")
                    }
                    is RunViewModel.RunViewState.USERDETAILS-> {
                        binding.apply {
                            tilName.editText?.setText(state.user.name)
                            tilWeight.editText?.setText(state.user.weight)
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}