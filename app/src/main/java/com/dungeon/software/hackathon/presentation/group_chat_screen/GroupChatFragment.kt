package com.dungeon.software.hackathon.presentation.group_chat_screen

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentGroupChatBinding
import com.dungeon.software.hackathon.domain.models.GroupChat
import com.dungeon.software.hackathon.domain.models.MessageGroup
import com.dungeon.software.hackathon.domain.models.User
import com.dungeon.software.hackathon.presentation.MainActivity
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class GroupChatFragment : BaseVMFragment<GroupChatViewModel, FragmentGroupChatBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_group_chat
    override val viewModelClass: KClass<GroupChatViewModel>
        get() = GroupChatViewModel::class

    private var currentChat: GroupChat? = null
    private var currentUser: User? = null

    private val adapter = GroupChatAdapter {}

    companion object {
        const val GROUP_CHAT_BUNDLE_TAG = "groupChat"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChat.adapter = adapter

        viewModel.getCurrentUser()
        initObservers()
        handleArguments()
        setupListeners()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messageState.collect {
                    adapter.submitList(it.sortedBy { message -> message.timeSent }
                        .toMutableList())
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUser.collect {
                    currentUser = it
                }
            }
        }
    }

    private fun handleArguments() {
        currentChat = arguments?.getParcelable(GROUP_CHAT_BUNDLE_TAG)
        currentChat?.let {
            binding.chat = it
            viewModel.getChat(currentChat?.uid ?: return@let)
        }
    }

    private fun setupListeners() = with(binding) {
        adapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                val msgCount = adapter.itemCount
                val lastVisiblePosition =
                    (rvChat.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                if (lastVisiblePosition != -1 || positionStart < msgCount - 1 &&
                    lastVisiblePosition != positionStart - 1
                ) {
                    scrollToBottom()
                }
            }
        })
        btnSend.setOnClickListener {
            if (etEnterMessage.text.toString().isNotBlank()) {
                currentChat?.let {
                    viewModel.sendMessage(
                        MessageGroup(
                            "",
                            etEnterMessage.text.toString(),
                            null,
                            null,
                            System.currentTimeMillis(),
                            emptyList(),
                            currentUser ?: return@setOnClickListener,
                            emptyMap()
                        ), it.uid
                    )
                    etEnterMessage.setText("")
                }
            } else {
                showMessage(getString(R.string.enter_message_without_dots))
            }
        }
        btnSendFile.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                currentChat?.let {
                    val uri = (requireActivity() as MainActivity).filePicker.getTakeImageFile()
                    viewModel.sendImage(uri.toUri(), it.uid, currentUser ?: return@launch)
                }
            }
        }
        ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun scrollToBottom() {
        if (adapter.itemCount > 3)
            binding.rvChat.smoothScrollToPosition(adapter.itemCount - 1)
    }
}