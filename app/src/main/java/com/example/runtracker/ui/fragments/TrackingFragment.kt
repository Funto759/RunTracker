package com.example.runtracker.ui.fragments

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import androidx.core.app.ServiceCompat.startForeground
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.runtracker.MainActivity
import com.example.runtracker.R
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentTrackingBinding
import com.example.runtracker.model.RunViewModel
import com.example.runtracker.services.TrackingService
import com.example.runtracker.util.Constants
import com.example.runtracker.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@Suppress("SameParameterValue")
@AndroidEntryPoint
class TrackingFragment : ViewBindingFragment<FragmentTrackingBinding>() {

    override val LayoutId: Int
        get() = R.layout.fragment_tracking

    private val viewModel by viewModels<RunViewModel>()

    private var map:GoogleMap? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrackingBinding {
       return FragmentTrackingBinding.inflate(inflater,container,false)
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

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.btnToggleRun.setOnClickListener{
            startCommandService(ACTION_START_OR_RESUME_SERVICE)
            print("heyyfyyfyfyfyfyf")
            Timber.d("services")
        }
        binding.apply {
            btnFinishRun.setOnClickListener{
                findNavController().navigate(TrackingFragmentDirections.actionTrackingFragmentToRunFragment())
                Timber.d("services")
            }
            mapView.getMapAsync{
                map = it
            }
        }
    }



    private fun startCommandService(command: String) {
        val intent = Intent(this.requireContext(), TrackingService::class.java).apply {
            this.action = command
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }





    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}