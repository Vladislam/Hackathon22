package com.dungeon.software.hackathon.presentation.chats_list_screen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatsListBinding
import com.dungeon.software.hackathon.domain.models.Chat
import com.dungeon.software.hackathon.domain.models.GroupChat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
class ChatsListFragment : BaseVMFragment<ChatsListViewModel, FragmentChatsListBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_chats_list
    override val viewModelClass: KClass<ChatsListViewModel>
        get() = ChatsListViewModel::class

    private val adapter = ChatsListAdapter {
        when (it) {
            is Chat -> ChatsListFragmentDirections.actionChatsListFragmentToChatFragment(it)
            is GroupChat -> ChatsListFragmentDirections.actionChatsListFragmentToGroupChatFragment(
                it
            )
        }
    }

    private val sortState: ChatSortStateHandler = ChatSortStateHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChats.adapter = adapter

        setupListeners()
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatsState.collect {
                    adapter.submitList(it.toMutableList())
                }
            }
        }
    }

    private fun setupListeners() = with(binding) {
        viewModel.initSortState(sortState.attach(binding, lifecycle))

        includeSort.llSortContainer.apply {
            post {
                translationY = -includeSort.llSortContainer.height.toFloat()
                visibility = View.GONE
            }
            rootContainer.setLayoutTransitionChanging()
            setLayoutTransitionChanging()
            ibSort.setOnClickListener {

                if (includeSort.isVisible == true) {
                    animate()
                        .translationY(-includeSort.llSortContainer.height.toFloat())
                        .setDuration(500)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                includeSort.isVisible = false
                                visibility = View.GONE
                            }
                        })
                } else {
                    animate()
                        .translationY(0f)
                        .setDuration(500)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationStart(animation: Animator?) {
                                super.onAnimationStart(animation)
                                includeSort.isVisible = true
                                visibility = View.VISIBLE
                            }
                        })
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.disposeChatsListeners()
    }

    private fun ViewGroup.setLayoutTransitionChanging() {
        layoutTransition = LayoutTransition().apply {
            this.enableTransitionType(LayoutTransition.CHANGING)
        }
    }
}