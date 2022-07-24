package com.dungeon.software.hackathon.domain.repository

import com.dungeon.software.hackathon.data.data_source.ChatDataSource
import com.dungeon.software.hackathon.data.data_source.MessageDataSource
import com.dungeon.software.hackathon.data.data_source.UserDataSource
import com.dungeon.software.hackathon.data.models.*
import com.dungeon.software.hackathon.domain.models.*
import com.dungeon.software.hackathon.util.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*

interface ChatRepository {

    suspend fun createChat(chat: ChatData): ChatData

    suspend fun getChat(chatId: String): Flow<ChatData>

    suspend fun getChats(): Flow<List<ChatData>>

    suspend fun sendMessage(message: MessageData, chatId: String)

    fun disposeChatsListeners()
    fun disposeChatListener()

    class Base(
        private val messageDataSource: MessageDataSource,
        private val chatDataSource: ChatDataSource,
        private val userDataSource: UserDataSource
    ) : ChatRepository {

        private val scope = CoroutineScope(Dispatchers.IO)

        override suspend fun createChat(chat: ChatData): ChatData {
            return if (chat is Chat) {
                val createdChat = chatDataSource.getPeerToPeerChatByOpponent(chat.opponent.uid)
                return if (createdChat == null) {
                    chatDataSource.createChat(chat.toDto())
                    chat
                } else {
                    Chat(chat.messages, createdChat.getUid().orEmpty(), chat.opponent)
                }
            } else {
                chatDataSource.createChat(chat.toDto())
                chat
            }
        }

        private fun ChatDataDto.getUid() = when (this) {
            is ChatDto -> {
                this.uid
            }
            is GroupChatDto -> {
                this.uid
            }
            else -> throw IllegalStateException()
        }

        override suspend fun getChat(chatId: String): Flow<ChatData> {
            val chatModel = chatDataSource.getChat(chatId)
            return when (chatModel) {
                is ChatDto -> {
                    getPeerToPeerChat(chatModel)
                }
                is GroupChatDto -> {
                    getGroupChat(chatModel)
                }
                else -> throw IllegalStateException()
            }
        }

        private suspend fun getPeerToPeerChat(chat: ChatDto): Flow<Chat> {
            val user = User(userDataSource.getUser(chat.opponent)!!)
            return messageDataSource.getMessages(chat.uid!!).mapNotNull {
                if (it is DataState.Data) {
                    Chat(it.data.map { Message(it as MessageDto, user) }, chat, user)
                } else {
                    null
                }

            }
        }

        private suspend fun getGroupChat(chat: GroupChatDto): Flow<GroupChat> {
            val users = chat.opponents.map { scope.async { userDataSource.getUser(it) } }.awaitAll()
                .mapNotNull { it }.map {
                    User(it)
                }
            return messageDataSource.getMessages(chat.uid!!).mapNotNull { messageData ->
                if (messageData is DataState.Data) {
                    GroupChat(messageData.data.map { currentMessage ->
                        MessageGroup(
                            currentMessage as MessageGroupDto,
                            users
                        )
                    }, chat, users)
                } else {
                    null
                }

            }
        }

        override suspend fun getChats(): Flow<List<ChatData>> {
            val chats = chatDataSource.getChats()

            return chats.mapNotNull {
                when (it) {
                    is ChatDto -> {
                        getLastPeerToPeerChat(it)
                    }
                    is GroupChatDto -> {
                        getLastGroupChat(it)
                    }
                    else -> null
                }
            }.toSingleFlow()
        }

        private suspend fun getLastGroupChat(chat: GroupChatDto): Flow<GroupChat> {
            val users = chat.opponents.map { scope.async { userDataSource.getUser(it) } }.awaitAll().mapNotNull { it }.map { User(it) }
            return messageDataSource.getLastMessage(chat.uid!!).mapNotNull {
                if (it is DataState.Data) {
                    val data = (it.data as MessageGroupDto?)?.run { listOf(MessageGroup(this, users)) } ?: listOf()
                    GroupChat(data, chat, users)
                } else {
                    null
                }

            }
        }

        private suspend fun getLastPeerToPeerChat(chat: ChatDto): Flow<Chat> {
            val user = User(userDataSource.getUser(chat.opponent)!!)
            return messageDataSource.getLastMessage(chat.uid!!).mapNotNull {
                if (it is DataState.Data) {
                    val data = (it.data as MessageDto?)?.run { listOf(Message(this, user)) } ?: listOf()
                    Chat(data, chat, user)
                } else {
                    null
                }

            }
        }

        override suspend fun sendMessage(message: MessageData, chatId: String) {
            messageDataSource.sendMessage(message.toDto(), chatId)
        }

        override fun disposeChatsListeners() {
            messageDataSource.cancelLastMessagesSubscription()
        }

        override fun disposeChatListener() {
            messageDataSource.cancelMessageSubscription()
        }

        private fun List<Flow<ChatData>>.toSingleFlow(): Flow<ArrayList<ChatData>> {
            val firstFlow = firstOrNull() ?: return flow {}
            var combinedFlow: Flow<ArrayList<ChatData>> = firstFlow.map { arrayListOf(it) }
            forEachIndexed { index, flow ->
                if (index == 0) {
                    return@forEachIndexed
                }
                combinedFlow = combinedFlow.combine(flow) { first, second ->
                    first.apply { add(second) }
                }
            }
            return combinedFlow
        }

        private fun MessageData.toDto() = when (this) {
            is Message -> {
                MessageDto(this)
            }
            is MessageGroup -> {
                MessageGroupDto(this)
            }
            else -> throw IllegalStateException()
        }

        private fun ChatData.toDto() = when (this) {
            is Chat -> {
                ChatDto(this)
            }
            is GroupChat -> {
                GroupChatDto(this)
            }
            else -> throw IllegalStateException()
        }

    }

}