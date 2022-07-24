package com.dungeon.software.hackathon.presentation.friend_search_screen

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentFriendSearchBinding
import com.dungeon.software.hackathon.domain.models.Chat
import com.dungeon.software.hackathon.domain.models.GroupChat
import com.dungeon.software.hackathon.domain.models.User
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class FriendSearchFragment : BaseVMFragment<FriendSearchViewModel, FragmentFriendSearchBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_friend_search
    override val viewModelClass: KClass<FriendSearchViewModel>
        get() = FriendSearchViewModel::class

    private var currentUser: User? = null

    private val adapter = FriendsSearchAdapter {
        findNavController().navigate(
            FriendSearchFragmentDirections.actionFriendSearchFragmentToOpponentUserDetailsFragment(
                it
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFriends.adapter = adapter

        setupListeners()
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUser.collect {
                    currentUser = it
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingFlow.collect {
                    if (it) showLoading()
                    else hideLoading()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatCreated.collect {
                    findNavController().navigate(
                        when (it) {
                            is Chat -> FriendSearchFragmentDirections.actionFriendSearchFragmentToChatFragment2(
                                it
                            )
                            is GroupChat -> FriendSearchFragmentDirections.actionFriendSearchFragmentToGroupChatFragment2(
                                it
                            )
                            else -> {
                                return@collect
                            }
                        }
                    )
                }
            }
        }
        adapter.submitList(
            mutableListOf(
                User(
                    "",
                    "Monkey",
                    "monkey@gmail.com",
                    "https://media.npr.org/assets/img/2017/09/12/macaca_nigra_self-portrait-3e0070aa19a7fe36e802253048411a38f14a79f8-s800-c85.webp",
                    emptyList()
                ),
                User(
                    "",
                    "Cat",
                    "cat@gmail.com",
                    "https://cdn.pixabay.com/photo/2020/03/23/08/45/cat-4959941_960_720.jpg",
                    emptyList()
                ),
                User(
                    "", "Monkey", "monkey@gmail.com", null, emptyList()
                ),
                User(
                    "", "Monkey", "monkey@gmail.com", null, emptyList()
                ),
            )
        )
    }

    private fun setupListeners() = with(binding) {
        fabCreateChat.setOnClickListener {
            val selected = adapter.itemsSelected
            viewModel.createChat(
                when (selected.size) {
                    0 -> {
                        showMessage("Select one or more users")
                        return@setOnClickListener
                    }
                    1 -> Chat(emptyList(), "", selected[0])
                    else -> GroupChat(
                        emptyList(),
                        "",
                        "Group of ${currentUser?.name ?: return@setOnClickListener}",
                        selected,
                        null
                    )
                }
            )
        }
    }
}