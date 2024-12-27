package com.example.runtracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.runtracker.R
import com.example.runtracker.dao.User
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentSetupBinding
import com.example.runtracker.model.RunViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        print("heyyy")
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

        binding.apply {
           val name = etName.text
            val weight = etWeight.text
            val user = User(
                name.toString(), weight.toString(),1
            )
            tvContinue.setOnClickListener {
                viewmodel.insertUser(user)
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToRunFragment())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}