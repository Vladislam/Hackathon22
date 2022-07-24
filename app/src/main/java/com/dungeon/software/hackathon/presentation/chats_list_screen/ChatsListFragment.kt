package com.dungeon.software.hackathon.presentation.chats_list_screen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatsListBinding
import kotlin.reflect.KClass

class ChatsListFragment : BaseVMFragment<ChatsListViewModel, FragmentChatsListBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_chats_list
    override val viewModelClass: KClass<ChatsListViewModel>
        get() = ChatsListViewModel::class

    private val sortState: ChatSortStateHandler = ChatSortStateHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortState.attach(binding, viewLifecycleOwner.lifecycle)
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
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

    private fun ViewGroup.setLayoutTransitionChanging() {
        layoutTransition = LayoutTransition().apply {
            this.enableTransitionType(LayoutTransition.CHANGING)
        }
    }
}