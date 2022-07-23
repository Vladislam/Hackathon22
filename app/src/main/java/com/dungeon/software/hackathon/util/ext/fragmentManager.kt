package com.dangeon.software.notes.util

import androidx.fragment.app.FragmentTransaction
import com.dungeon.software.hackathon.R

fun FragmentTransaction.applyAnimation() = this.setCustomAnimations(
    R.anim.slide_in_left,
    R.anim.slide_out_right,
    R.anim.pop_enter,
    R.anim.pop_exit
)