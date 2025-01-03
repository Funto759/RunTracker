package com.example.runtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.runtracker.R
import com.example.runtracker.dao.User
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentSettingsBinding
import com.example.runtracker.model.RunViewModel
import com.example.runtracker.util.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.internal.InjectedFieldSignature
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {
    override val LayoutId: Int
        get()= R.layout.fragment_settings

    private val viewmodel by viewModels<RunViewModel>()

    @Inject
    lateinit var sharedPreferences: SharedPreferences



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getUserDetails(1)
        binding.apply{

           val name = sharedPreferences.getString(Constants.KEY_NAME,"") ?: ""
           val weight = sharedPreferences.getFloat(Constants.KEY_WEIGHT,80f)
            etName.setText(name)
            etWeight.setText(weight.toString())

            btnApplyChanges.setOnClickListener {
              changeUserDetailsDialog()

            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater,container,false)
    }

    private fun changeUserDetailsDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Update User Details")
            .setMessage("Are you sure you want to edit your user details")
            .setPositiveButton("Yes"){_,_ ->
                binding.apply {
                    etName.clearFocus()
                    etWeight.clearFocus()

                    val name = etName.text.toString().trim()
                    val weight = etWeight.text.toString().toFloat()
                    sharedPreferences.edit()
                        .putString(Constants.KEY_NAME,name)
                        .putFloat(Constants.KEY_WEIGHT,weight.toFloat())
                        .apply()
                }
            }
            .setNegativeButton("No"){dialog,_ ->
                dialog.cancel()
            }.show()
        dialog.show()
    }

    override fun run() {
        Timber.d("logg")
//        viewLifecycleOwner.lifecycleScope.launch{
//            viewmodel.UserState.observe(viewLifecycleOwner){ state ->
//                when(state){
//                    is RunViewModel.RunViewState.LOADING -> {
//                        print("loading")
//                    }
//
//                    is RunViewModel.RunViewState.ERROR -> {
//                        print("Error Occurred")
//                    }
//                    is RunViewModel.RunViewState.USERDETAILS-> {
//                        binding.apply {
//                            etName.setText(state.user.name)
//                            etWeight.setText(state.user.weight)
//                            Timber.d("name = ${state.user.name}")
//                            Timber.d("weight = ${state.user.weight}")
////                            tilName.editText?.setText(state.user.name)
////                            tilWeight.editText?.setText(state.user.weight)
//                        }
//                    }
//
//                    else -> {}
//                }
//            }
//        }
    }
}