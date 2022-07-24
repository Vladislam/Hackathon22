package com.dungeon.software.hackathon.presentation.chat_screen

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import com.dungeon.software.hackathon.domain.models.Chat
import com.dungeon.software.hackathon.domain.models.Message
import com.dungeon.software.hackathon.domain.models.User
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class ChatFragment : BaseVMFragment<ChatViewModel, FragmentChatBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_chat

    override val viewModelClass: KClass<ChatViewModel>
        get() = ChatViewModel::class

    private var currentChat: Chat? = null
    private var currentUser: User? = null

    private val adapter = ChatAdapter {}

    companion object {
        const val GROUP_CHAT_BUNDLE_TAG = "chat"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChat.adapter = adapter

        viewModel.getCurrentUser()
        initObservers()
        setupListeners()
        handleArguments()
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
            viewModel.getChat(currentChat?.uid ?: return)
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
                        Message(
                            "",
                            etEnterMessage.text.toString(),
                            null,
                            null,
                            System.currentTimeMillis(),
                            false,
                            currentUser ?: return@setOnClickListener,
                            null
                        ), it.uid
                    )
                    etEnterMessage.setText("")
                }
            } else {
                showMessage(getString(R.string.enter_message_without_dots))
            }
        }
        btnSendFile.setOnClickListener {

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