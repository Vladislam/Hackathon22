package com.dungeon.software.hackathon.presentation.chats_list_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.recycler_view.BaseRecyclerViewAdapter
import com.dungeon.software.hackathon.databinding.ItemChatBinding
import com.dungeon.software.hackathon.domain.models.ChatData

class ChatsListAdapter(private val listener: (ChatData) -> Unit) :
    BaseRecyclerViewAdapter<ChatData, ItemChatBinding>() {
    override val layoutId: Int
        get() = R.layout.item_chat

    override fun onBind(binding: ItemChatBinding, position: Int) = with(binding) {
        val currentChat = getItem(position)
        chat = currentChat
        clRoot.setOnClickListener {
            listener.invoke(currentChat)
        }
    }
}