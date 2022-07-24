package com.dungeon.software.hackathon.presentation.friend_search_screen

import android.graphics.drawable.TransitionDrawable
import android.view.View
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

    lateinit var addToFriends: ((User) -> Unit)

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

        if (this@FriendsSearchAdapter::me.isInitialized) {
            ibAddFriend.visibility = if (me.friends.contains(currentUser.uid)) View.GONE else View.VISIBLE
            ibAddFriend.setOnClickListener {
                ibAddFriend.visibility = View.GONE
                me.friends = me.friends.toCollection(ArrayList()).apply { add(currentUser.uid) }
                addToFriends.invoke(me)
            }
        } else {
            ibAddFriend.visibility = View.GONE
        }
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