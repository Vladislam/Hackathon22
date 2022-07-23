package com.dungeon.software.hackathon.presentation.friend_search_screen

import android.graphics.drawable.TransitionDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.recycler_view.BaseRecyclerViewAdapter
import com.dungeon.software.hackathon.databinding.ItemUserBinding
import com.dungeon.software.hackathon.domain.models.User

class FriendsSearchAdapter constructor(private val listener: (User) -> Unit) :
    BaseRecyclerViewAdapter<User, ItemUserBinding>() {
    override val layoutId: Int
        get() = R.layout.item_user

    var itemsSelected: MutableList<User> = mutableListOf()

    override fun onBind(binding: ItemUserBinding, position: Int) = with(binding) {
        val currentUser = getItem(position)
        user = currentUser
        clRoot.setOnClickListener {
            if(itemsSelected.isEmpty()) {
                listener.invoke(currentUser)
            } else {

            }
        }
        clRoot.setOnLongClickListener {
            clRoot.handleSelection(currentUser)
            true
        }
    }

    private fun ConstraintLayout.handleSelection(user: User) {
        if (itemsSelected.contains(user)) {
            itemsSelected.remove(user)
        } else {
            itemsSelected.add(user)
        }
        (background as TransitionDrawable).reverseTransition(200)
    }
}