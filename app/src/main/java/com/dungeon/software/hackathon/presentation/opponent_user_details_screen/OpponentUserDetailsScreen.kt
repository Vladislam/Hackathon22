package com.dungeon.software.hackathon.presentation.opponent_user_details_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import com.dungeon.software.hackathon.databinding.FragmentOpponentUserDetailsBinding
import kotlin.reflect.KClass

class OpponentUserDetailsScreen : BaseVMFragment<OpponentUserDetailsViewModel, FragmentOpponentUserDetailsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_opponent_user_details
    override val viewModelClass: KClass<OpponentUserDetailsViewModel>
        get() = OpponentUserDetailsViewModel::class
}