package com.example.runtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runtracker.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToTrackingFragment(intent)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
//        setSupportActionBar(findViewById(R.id.toolbar))
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navHostFragment.findNavController().addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.runFragment,R.id.settingsFragment,R.id.statisticsFragment ->
                    bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }

    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragment(intent)
    }

    private fun navigateToTrackingFragment(intent: Intent?){
        if (this.intent?.action == Constants.ACTION_SHOW_TRACKING_FRAGMENT){
            navController.navigate(R.id.action_global_trackingFragment)
        }
    }
}
