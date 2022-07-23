package com.dungeon.software.hackathon.presentation.splash_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import com.dungeon.software.hackathon.databinding.FragmentSplashBinding
import kotlin.reflect.KClass

class SplashFragment : BaseVMFragment<SplashViewModel, FragmentSplashBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_splash
    override val viewModelClass: KClass<SplashViewModel>
        get() = SplashViewModel::class
}