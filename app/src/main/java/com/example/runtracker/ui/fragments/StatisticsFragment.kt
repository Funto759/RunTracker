package com.example.runtracker.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.runtracker.databinding.FragmentSettingsBinding
import com.example.runtracker.databinding.FragmentStatisticsBinding
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentTrackingBinding
import com.example.runtracker.model.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : ViewBindingFragment<FragmentStatisticsBinding>(){

    override val LayoutId: Int
        get() =com.example.runtracker.R.layout.fragment_statistics

    private val viewModel by viewModels<StatisticsViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStatisticsBinding{
        return FragmentStatisticsBinding.inflate(inflater,container,false)
    }

    override fun run() {
        print("heyyy")
    }
}