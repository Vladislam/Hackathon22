package com.dungeon.software.hackathon.presentation.chat_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.recycler_view.BaseRecyclerViewAdapter
import com.dungeon.software.hackathon.base.view_holder.BindingHolder
import com.dungeon.software.hackathon.databinding.ItemMessageBinding
import com.dungeon.software.hackathon.databinding.ItemMessageImageBinding
import com.dungeon.software.hackathon.domain.models.Message

class ChatAdapter(private val listener: (Message) -> Unit) :
    BaseRecyclerViewAdapter<Message, ViewDataBinding>() {
    override val layoutId: Int
        get() = R.layout.item_message

    companion object {
        const val PHOTO_VIEW_TYPE = 56
        const val TEXT_MESSAGE_VIEW_TYPE = 57
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ViewDataBinding> {
        return when (viewType) {
            PHOTO_VIEW_TYPE -> {
                BindingHolder(
                    ItemMessageImageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                BindingHolder(
                    ItemMessageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = getItem(position)
        return if (currentMessage.imageUrl != null) {
            PHOTO_VIEW_TYPE
        } else {
            TEXT_MESSAGE_VIEW_TYPE
        }
    }

    override fun onBind(binding: ViewDataBinding, position: Int) = with(binding) {
        val currentMessage = getItem(position)
        when (this) {
            is ItemMessageBinding -> {
                message = currentMessage
                llRoot.setOnClickListener {
                    listener.invoke(currentMessage)
                }
            }
            is ItemMessageImageBinding -> {
                message = currentMessage
                llRoot.setOnClickListener {
                    listener.invoke(currentMessage)
                }
            }
        }
    }
}