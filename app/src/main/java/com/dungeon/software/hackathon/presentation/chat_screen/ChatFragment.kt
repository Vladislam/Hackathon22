package com.dungeon.software.hackathon.presentation.chat_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseBindingFragment
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import kotlin.reflect.KClass

class ChatFragment : BaseVMFragment<ChatViewModel, FragmentChatBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_chat

    override val viewModelClass: KClass<ChatViewModel>
        get() = ChatViewModel::class
}