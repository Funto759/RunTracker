package com.example.runtracker.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentTrackingBinding

class TrackingFragment : ViewBindingFragment<FragmentTrackingBinding>() {

    override val LayoutId: Int
        get() = com.example.runtracker.R.layout.fragment_tracking

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrackingBinding {
       return FragmentTrackingBinding.inflate(inflater,container,false)
    }

    override fun run() {
        TODO("Not yet implemented")
    }
}