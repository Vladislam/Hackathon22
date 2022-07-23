package com.dungeon.software.hackathon.presentation.group_chat_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentGroupChatBinding
import kotlin.reflect.KClass

class GroupChatFragment : BaseVMFragment<GroupChatViewModel, FragmentGroupChatBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_group_chat
    override val viewModelClass: KClass<GroupChatViewModel>
        get() = GroupChatViewModel::class
}