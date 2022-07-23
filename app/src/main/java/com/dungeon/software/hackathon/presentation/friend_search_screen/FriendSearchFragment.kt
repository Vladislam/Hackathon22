package com.dungeon.software.hackathon.presentation.friend_search_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import com.dungeon.software.hackathon.databinding.FragmentFriendSearchBinding
import kotlin.reflect.KClass

class FriendSearchFragment : BaseVMFragment<FriendSearchViewModel, FragmentFriendSearchBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_friend_search
    override val viewModelClass: KClass<FriendSearchViewModel>
        get() = FriendSearchViewModel::class
}