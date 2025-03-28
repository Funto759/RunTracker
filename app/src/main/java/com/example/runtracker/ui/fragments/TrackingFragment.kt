package com.example.runtracker.ui.fragments

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import androidx.core.app.ServiceCompat.startForeground
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.runtracker.MainActivity
import com.example.runtracker.R
import com.example.runtracker.dao.Run
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentTrackingBinding
import com.example.runtracker.model.RunViewModel
import com.example.runtracker.services.Polylines
import com.example.runtracker.services.TrackingService
import com.example.runtracker.util.Constants
import com.example.runtracker.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runtracker.util.TrackingUtility
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Cap
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.round

@Suppress("SameParameterValue")
@AndroidEntryPoint
class TrackingFragment : ViewBindingFragment<FragmentTrackingBinding>() {

    override val LayoutId: Int
        get() = R.layout.fragment_tracking

    private val viewModel by viewModels<RunViewModel>()
    var isTracking =false
  private var pathPoints = mutableListOf<MutableList<LatLng>>()
    private var currentTimeInMillis = 0L

    private var map:GoogleMap? = null
    private var menu:Menu?= null



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
           runState()
            setupCancelRunMenu()
            print("heyyfyyfyfyfyfyf")
            Timber.d("services")
        }
        binding.apply {
            btnFinishRun.setOnClickListener{

                Timber.d("services")
                moveCameraToFinalRun()
                saveRunToDatabase()
            }
            mapView.getMapAsync{
                map = it
                addAllPolyLine()
            }
        }
        subscribeToObservers()
    }

    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer{
         updateTracking(it)
        })

        TrackingService.pathPoint.observe(viewLifecycleOwner, Observer{
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeInMilli.observe(viewLifecycleOwner, Observer{
            currentTimeInMillis = it
            val time = TrackingUtility.getFormattedStopwatchTime(currentTimeInMillis,true)
            binding.tvTimer.text = time
        })
    }
    fun runState(){
        if (isTracking){
            val cancelRunMenuItem = menu?.findItem(R.id.cancelRun)
            cancelRunMenuItem?.isVisible = isTracking
            startCommandService(Constants.ACTION_PAUSE_SERVICE)
        } else {
            startCommandService(Constants.ACTION_START_OR_RESUME_SERVICE)
        }
    }
private fun updateTracking(isTracking: Boolean){
    this.isTracking = isTracking
    if (!isTracking){
  binding.btnFinishRun.visibility = View.GONE
    } else {
        binding.btnFinishRun.visibility = View.VISIBLE
    }

}

    private fun moveCameraToUser(){
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    16f
                )
            )
        }
    }

    private fun moveCameraToFinalRun(){
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints){
            for (pos in polyline){
                bounds.include(pos)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun saveRunToDatabase(){
        map?.snapshot{bm->
            var distanceInMeters = 0
            val weight = 80f
            for (polyline in pathPoints){
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()

            }
            val avgSpeed = round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 60 / 60)*10)/10f
            val dateTimestamp = java.util.Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
            val run = Run(bm,dateTimestamp,avgSpeed,currentTimeInMillis,distanceInMeters,caloriesBurned)
            viewModel.insertRun(run)
            Snackbar.make(requireActivity().findViewById(R.id.rootView), "Run saved successfully", Snackbar.LENGTH_LONG).show()
            stopRun()
        }
    }
    private fun addAllPolyLine(){
        for (polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(Color.BLUE)
                .width(12f)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline(){
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()

            val polylineOptions = PolylineOptions()
                .color(Color.BLUE)
                .width(12f)
                .geodesic(true)
                .pattern(listOf(Dot(),Gap(10f)))
                .add(preLastLatLng)
                .add(lastLatLng)
                .jointType(JointType.ROUND) // For rounded joints
            map?.addPolyline(polylineOptions)


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


    private fun setupCancelRunMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.tracking_toolbar_menu, menu)
            }
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val cancelRunMenuItem = menu.findItem(R.id.cancelRun)
                cancelRunMenuItem.isVisible = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.cancelRun -> {
                      createCancelRunAlertDialog()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.STARTED) // Consider using STARTED
    }

    fun createCancelRunAlertDialog(){

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel a Run")
            .setMessage("Are you sure you want to cancel a run")
            .setPositiveButton("Yes"){
                _,_ ->
                stopRun()
            }
            .setNegativeButton("No"){
                dialog,_ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopRun() {
        startCommandService(Constants.ACTION_STOP_SERVICE)
        findNavController().navigate(TrackingFragmentDirections.actionTrackingFragmentToRunFragment())
    }
}