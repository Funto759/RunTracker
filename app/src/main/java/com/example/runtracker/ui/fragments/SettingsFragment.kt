package com.example.runtracker.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentSettingsBinding

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {
    override val LayoutId: Int
        get()= com.example.runtracker.R.layout.fragment_settings





    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater,container,false)
    }

    override fun run() {
        print("heyyy")
    }
}