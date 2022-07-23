package com.dungeon.software.hackathon.presentation.user_details_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import com.dungeon.software.hackathon.databinding.FragmentUserDetailsBinding
import kotlin.reflect.KClass

class UserDetailsFragment : BaseVMFragment<UserDetailsViewModel, FragmentUserDetailsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_user_details
    override val viewModelClass: KClass<UserDetailsViewModel>
        get() = UserDetailsViewModel::class
}