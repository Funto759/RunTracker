package com.example.runtracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.runtracker.R
import com.example.runtracker.dao.User
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentSetupBinding
import com.example.runtracker.model.RunViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.getValue

@AndroidEntryPoint
class SetupFragment : ViewBindingFragment<FragmentSetupBinding>() {

    override val LayoutId: Int
        get() = R.layout.fragment_setup

    private val viewmodel by viewModels<RunViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSetupBinding {
      return  FragmentSetupBinding.inflate(inflater,container
        ,false)
    }

    override fun run() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewmodel.UserState.observe(viewLifecycleOwner){ state ->
                when(state){
                    is RunViewModel.RunViewState.LOADING -> {
                        print("loading")
                    }

                    is RunViewModel.RunViewState.ERROR -> {
                        print("Error Occurred")
                    }
                    is RunViewModel.RunViewState.USERDETAILS-> {
                        if (true){
                            findNavController().navigate(
                                SetupFragmentDirections.actionSetupFragmentToRunFragment()
                            )
                        }

                    }

                    else -> {}
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getUserDetails(1)
        binding.apply {
            tvContinue.setOnClickListener {
                etName.clearFocus()
                etWeight.clearFocus()

                val name = etName.text.toString().trim()
                val weight = etWeight.text.toString().trim()

                if (name.isEmpty() || weight.isEmpty()) {
                    Timber.d("Please fill in both name and weight")
                    return@setOnClickListener
                }

                val user = User(name, weight)
                Timber.d("name = $name")
                Timber.d("weight = $weight")
                Timber.d("user = $user")
                viewmodel.insertUser(user)

                findNavController().navigate(
                    SetupFragmentDirections.actionSetupFragmentToRunFragment()
                )
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}