package com.dungeon.software.hackathon.presentation.friend_search_screen

import android.graphics.drawable.TransitionDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.recycler_view.BaseRecyclerViewAdapter
import com.dungeon.software.hackathon.databinding.ItemUserBinding
import com.dungeon.software.hackathon.domain.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsSearchAdapter constructor(private val listener: (User) -> Unit) :
    BaseRecyclerViewAdapter<User, ItemUserBinding>() {
    override val layoutId: Int
        get() = R.layout.item_user

    lateinit var me: User

    private val scope = CoroutineScope(Dispatchers.IO)

    val itemsSelected: MutableList<User> = mutableListOf()

    override fun onBind(binding: ItemUserBinding, position: Int) = with(binding) {
        val currentUser = getItem(position)
        user = currentUser
        clRoot.setOnClickListener {
            clRoot.handleSelection(currentUser)
        }
        includeImage.ivChatImage.setOnClickListener {
            listener.invoke(currentUser)
        }
//        TODO: Check if user is my friend
    }

    private fun ConstraintLayout.handleSelection(user: User) = scope.launch {
        itemsSelected.apply {
            if (contains(user)) {
                remove(user)
            } else {
                add(user)
            }
        }
        (background as TransitionDrawable).reverseTransition(200)
    }
}