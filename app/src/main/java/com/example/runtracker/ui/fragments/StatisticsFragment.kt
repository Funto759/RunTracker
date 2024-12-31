package com.example.runtracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.runtracker.databinding.FragmentSettingsBinding
import com.example.runtracker.databinding.FragmentStatisticsBinding
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentTrackingBinding
import com.example.runtracker.model.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      viewModel.totalDistance
        viewModel.totalTime
        viewModel.totalCalories
        viewModel.totalAvgSpeed
    }

    override fun run() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.totalTime.observe(viewLifecycleOwner, Observer{
                Timber.d(it.toString())
                binding.tvTotalTime.text = convertMilliToTime(it)
            })
        }
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer{
                Timber.d(it.toString())
                binding.tvAverageSpeed.text = String.format(Locale.getDefault(),"%.2f",it.toFloat())
            })
        }
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.totalDistance.observe(viewLifecycleOwner, Observer{
                Timber.d(it.toString())
                binding.tvTotalDistance.text = String.format(Locale.getDefault(),"%.2f",it.toFloat())
            })
        }
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.totalCalories.observe(viewLifecycleOwner, Observer{
                Timber.d(it.toString())
                binding.tvTotalCalories.text = String.format(Locale.getDefault(),"%.2f",it.toFloat())
            })
        }
    }

    fun convertMilliToTime(milli : Long):String{
        if (milli == null){
            return "00:00:00"
        }else{
            val hours = milli / (1000 *60*60)
            val minutes = (milli % (1000 *60*60)) / (1000*60)
            val seconds =  (milli % (1000 *60)) / 1000
            return String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds)
        }
    }
}