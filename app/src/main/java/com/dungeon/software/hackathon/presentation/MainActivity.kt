package com.dungeon.software.hackathon.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.activity.BaseBindingActivity
import com.dungeon.software.hackathon.databinding.ActivityMainBinding
import com.dungeon.software.hackathon.util.FilePicker

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    val filePicker = FilePicker(this)

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment).navController
        navController.addOnDestinationChangedListener { controller, destination, argument ->
            destination.setVisibilityBottomNav()
        }
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun changeBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigation.visibility = visibility
    }

    private fun NavDestination.setVisibilityBottomNav() {
        changeBottomNavigationVisibility(
            when (id) {
                R.id.chatsListFragment -> View.VISIBLE
                R.id.friendSearchFragment -> View.VISIBLE
                R.id.userDetailsFragment3 -> View.VISIBLE
                else -> View.GONE
            }
        )
    }

}