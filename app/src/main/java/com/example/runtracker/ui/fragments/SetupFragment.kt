package com.example.runtracker.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
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
        TODO("Not yet implemented")
    }
}