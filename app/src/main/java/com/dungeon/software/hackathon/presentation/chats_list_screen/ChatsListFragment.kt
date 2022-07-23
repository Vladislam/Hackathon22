package com.dungeon.software.hackathon.presentation.chats_list_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import kotlin.reflect.KClass

class ChatsListFragment : BaseVMFragment<ChatsListViewModel, FragmentChatBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_chats_list
    override val viewModelClass: KClass<ChatsListViewModel>
        get() = ChatsListViewModel::class
}