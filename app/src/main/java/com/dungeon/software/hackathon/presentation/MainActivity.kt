package com.dungeon.software.hackathon.presentation

import android.os.Bundle
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.activity.BaseBindingActivity
import com.dungeon.software.hackathon.databinding.ActivityMainBinding

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun changeBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigation.visibility = visibility
    }

}