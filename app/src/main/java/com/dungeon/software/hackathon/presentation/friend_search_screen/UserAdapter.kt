package com.dungeon.software.hackathon.presentation.friend_search_screen

import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.recycler_view.BaseRecyclerViewAdapter
import com.dungeon.software.hackathon.databinding.ItemUserBinding
import com.dungeon.software.hackathon.domain.models.User

class UserAdapter constructor(private val listener: (User) -> Unit) :
    BaseRecyclerViewAdapter<User, ItemUserBinding>() {
    override val layoutId: Int
        get() = R.layout.item_user

    override fun onBind(binding: ItemUserBinding, position: Int) = with(binding) {
        val currentUser = getItem(position)
        user = currentUser
        clRoot.setOnClickListener {
            listener.invoke(currentUser)
        }
    }
}