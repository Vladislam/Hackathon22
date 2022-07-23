package com.dungeon.software.hackathon.presentation.group_chat_details_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import com.dungeon.software.hackathon.databinding.FragmentGroupChatDetailsBinding
import kotlin.reflect.KClass

class GroupChatDetailsFragment : BaseVMFragment<GroupChatDetailsViewModel, FragmentGroupChatDetailsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_group_chat_details
    override val viewModelClass: KClass<GroupChatDetailsViewModel>
        get() = GroupChatDetailsViewModel::class
}