package com.example.runtracker.ui.fragments

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {
    override val LayoutId: Int
        get()= R.layout.fragment_settings

    private val viewmodel by viewModels<RunViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getUserDetails(1)
        binding.apply{


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
                    val weight = etWeight.text.toString().trim()
                    val user = User(name,weight,1)
                    Snackbar.make(requireView(),"User details changed successfully", Snackbar.LENGTH_SHORT).show()
                    viewmodel.insertUser(user)
                }
            }
            .setNegativeButton("No"){dialog,_ ->
                dialog.cancel()
            }.show()
        dialog.show()
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
                        binding.apply {
                            etName.setText(state.user.name)
                            etWeight.setText(state.user.weight)
                            Timber.d("name = ${state.user.name}")
                            Timber.d("weight = ${state.user.weight}")
//                            tilName.editText?.setText(state.user.name)
//                            tilWeight.editText?.setText(state.user.weight)
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}