package com.example.runtracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentSetupBinding

class SetupFragment : ViewBindingFragment<FragmentSetupBinding>() {

    override val LayoutId: Int
        get() =com.example.runtracker.R.layout.fragment_setup

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
            tvContinue.setOnClickListener {
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToRunFragment())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}