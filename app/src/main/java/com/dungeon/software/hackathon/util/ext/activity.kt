package com.dungeon.software.hackathon.util.ext

import android.view.View
import androidx.fragment.app.Fragment
import com.dungeon.software.hackathon.presentation.MainActivity

fun Fragment.showBottomNav() = (requireActivity() as MainActivity).changeBottomNavigationVisibility(View.VISIBLE)

fun Fragment.hideBottomNav() = (requireActivity() as MainActivity).changeBottomNavigationVisibility(View.GONE)