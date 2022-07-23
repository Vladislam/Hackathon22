package com.dungeon.software.hackathon.presentation

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.activity.BaseBindingActivity
import com.dungeon.software.hackathon.databinding.ActivityMainBinding

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bottomNavigation.setupWithNavController((supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment).navController)
    }

    fun changeBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigation.visibility = visibility
    }

}