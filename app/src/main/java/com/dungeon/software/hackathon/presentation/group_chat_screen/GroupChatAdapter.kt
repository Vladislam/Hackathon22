package com.dungeon.software.hackathon.presentation.group_chat_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.recycler_view.BaseRecyclerViewAdapter
import com.dungeon.software.hackathon.databinding.ItemMessageBinding
import com.dungeon.software.hackathon.domain.models.MessageGroup

class GroupChatAdapter(private val listener: (MessageGroup) -> Unit) :
    BaseRecyclerViewAdapter<MessageGroup, ItemMessageBinding>() {
    override val layoutId: Int
        get() = R.layout.item_message

    override fun onBind(binding: ItemMessageBinding, position: Int) = with(binding) {
        val currentMessage = getItem(position)
        message = currentMessage
        llRoot.setOnClickListener {
            listener.invoke(currentMessage)
        }
    }
}