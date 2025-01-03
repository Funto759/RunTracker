package com.example.runtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.runtracker.R
import com.example.runtracker.dao.User
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentSetupBinding
import com.example.runtracker.model.RunViewModel
import com.example.runtracker.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import dagger.internal.InjectedFieldSignature
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class SetupFragment : ViewBindingFragment<FragmentSetupBinding>() {

    override val LayoutId: Int
        get() = R.layout.fragment_setup

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
//                        if (true){
//                            findNavController().navigate(
//                                SetupFragmentDirections.actionSetupFragmentToRunFragment()
//                            )
//                        }

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

        if (!sharedPreferences.getBoolean(Constants.KEY_TOGGLE_FIRST_TIME_TOGGLE,true)){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment,true)
                .build()
            findNavController().navigate(R.id.action_setupFragment_to_runFragment,savedInstanceState,navOptions)

        }
        binding.apply {
            tvContinue.setOnClickListener {
                etName.clearFocus()
                etWeight.clearFocus()
                addUserDetailsToSharedPref()

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

    fun addUserDetailsToSharedPref(): Boolean{
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if (name.isEmpty() ||weight.isEmpty()){
            return false
        }
        sharedPreferences.edit()
            .putString(Constants.KEY_NAME,name)
            .putFloat(Constants.KEY_WEIGHT,weight.toFloat())
            .putBoolean(Constants.KEY_TOGGLE_FIRST_TIME_TOGGLE,false)
            .apply()
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}