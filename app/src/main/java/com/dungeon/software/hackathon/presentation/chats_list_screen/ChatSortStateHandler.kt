package com.dungeon.software.hackathon.presentation.chats_list_screen

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.dungeon.software.hackathon.util.ext.onTextChange
import com.dungeon.software.hackathon.databinding.FragmentChatsListBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatSortStateHandler {

    private var binding: FragmentChatsListBinding? = null

    private var lifecycle: Lifecycle? = null

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null

    private var byNameDesc: Boolean = true
    private var byLastMessageTimeDesc: Boolean = true
    private var currentSort: SortType = SortType.Name(byNameDesc)
    private val sortState: MutableStateFlow<SortState> =
        MutableStateFlow(SortState("", SortType.Name(true)))

    private val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            destroy()
        }
    }

    fun attach(binding: FragmentChatsListBinding, lifecycle: Lifecycle): StateFlow<SortState> {
        this.binding = binding
        this.lifecycle = lifecycle
        lifecycle.addObserver(observer)

        currentSort.handleState()
        initBinding()
        return sortState
    }

    private fun initBinding() = binding?.apply {
        includeSort.btnByName.setOnClickListener {
            scope.launch {
                SortType.Name(if (currentSort is SortType.Name) !byNameDesc else byNameDesc)
                    .emit()
            }
        }
        includeSort.btnByTimeLastMessage.setOnClickListener {
            scope.launch {
                SortType.LastMessageTime(if (currentSort is SortType.LastMessageTime) !byLastMessageTimeDesc else byLastMessageTimeDesc)
                    .emit()
            }
        }
        includeSort.etSearch.onTextChange { text ->
            job?.cancel()
            job = scope.launch {
                delay(425)
                sortState.emit(sortState.value.copy(query = text))
            }
        }
    }

    private suspend fun SortType.emit() {
        sortState.emit(sortState.value.copy(sortType = this))
        withContext(Dispatchers.Main) { handleState() }
    }

    private fun SortType.handleState() {
        changeVisibility()
        setCurrentState()
        setRotation()
        when (this) {
            is SortType.LastMessageTime -> {
                byLastMessageTimeDesc = this.desc
            }
            is SortType.Name -> {
                byNameDesc = this.desc
            }
        }
    }

    private fun SortType.changeVisibility() {
        binding?.includeSort?.apply {
            ivByNameIcon.setVisible(this@changeVisibility is SortType.LastMessageTime)
            ivByTimeLastMessage.setVisible(this@changeVisibility is SortType.LastMessageTime)
        }
    }

    private fun SortType.setCurrentState() {
        if (currentSort::class.simpleName != this::class.simpleName) {
            currentSort = this
        }
    }

    private fun SortType.setRotation() {
        getTriangleView()?.animate()?.rotation(
            if (desc) {
                0f
            } else {
                -180f
            }
        )
    }

    private fun SortType.getTriangleView() = when (this) {
        is SortType.LastMessageTime -> binding?.includeSort?.ivByTimeLastMessage
        is SortType.Name -> binding?.includeSort?.ivByNameIcon
    }

    private fun View.setVisible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun destroy() {
        scope.cancel()
        binding = null
    }
}