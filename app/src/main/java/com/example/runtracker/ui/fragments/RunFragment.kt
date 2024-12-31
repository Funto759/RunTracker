package com.example.runtracker.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runtracker.R
import com.example.runtracker.adapter.RunViewAdapter
import com.example.runtracker.util.ViewBindingFragment
import com.example.runtracker.databinding.FragmentRunBinding
import com.example.runtracker.model.RunViewModel
import com.example.runtracker.util.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : ViewBindingFragment<FragmentRunBinding>(), EasyPermissions.PermissionCallbacks {

private val viewmodel by viewModels<RunViewModel>()
    private lateinit var runViewAdapter: RunViewAdapter

    override val LayoutId: Int
        get() = R.layout.fragment_run

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRunBinding {
       return FragmentRunBinding.inflate(inflater,container,false)
    }

    override fun run() {
        viewLifecycleOwner.lifecycleScope.launch{
                viewmodel.stateRun.observe(viewLifecycleOwner){ state ->
                   when(state){
                       is RunViewModel.RunViewState.LOADING -> {
                           print("loading")
                       }

                       is RunViewModel.RunViewState.ERROR -> {
                           print("Error Occurred")
                       }
                       is RunViewModel.RunViewState.GETRUNS -> {
                          runViewAdapter.submitList(state.run)
                       }

                       else -> {}
                   }
                }
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions()
        }
        setupRecyclerView()
        viewmodel.getAllRuns()
        binding.apply {
            spFilter.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedOption = parent?.getItemAtPosition(position).toString()
                    handleSpinnerSelection(selectedOption)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            fab.setOnClickListener{
                findNavController().navigate(RunFragmentDirections.actionRunFragmentToTrackingFragment())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissions(){
        if (TrackingUtility.hasPermissionLocation(requireContext())){
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permission to use the App.",
            0,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use the App.",
                0,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(
        requestCode: Int,
        perms: List<String?>
    ) {}

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onPermissionsDenied(
        requestCode: Int,
        perms: List<String?>
    ) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        } else{
            requestPermissions()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    private fun setupRecyclerView() = binding.rvRuns.apply {
        runViewAdapter = RunViewAdapter()
        adapter = runViewAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    fun handleSpinnerSelection(option : String){
        when(option){
            "Date" -> {viewmodel.getAllRuns()}
            "Running Time" -> {viewmodel.getAllRunsByTimeInMilli()}
            "Distance" -> {viewmodel.getAllRunsByDistance()}
            "Average Speed" -> {viewmodel.getAllRunsByAvgSpeed()}
            "Calories Burned" -> {viewmodel.getAllRunsByCaloriesBurned()}
        }
    }
}