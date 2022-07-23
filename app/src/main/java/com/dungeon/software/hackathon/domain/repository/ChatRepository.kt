package com.dungeon.software.hackathon.domain.repository

import com.dungeon.software.hackathon.data.data_source.ChatDataSource
import com.dungeon.software.hackathon.data.data_source.MessageDataSource
import com.dungeon.software.hackathon.data.models.*
import com.dungeon.software.hackathon.data.repository.UserDataSource
import com.dungeon.software.hackathon.domain.models.*
import com.dungeon.software.hackathon.util.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull

interface ChatRepository {

    suspend fun createChat(chat: ChatData)

    suspend fun getChat(chatId: String): Flow<ChatData>

    suspend fun getChats(): Flow<List<ChatData>>

    suspend fun sendMessage(message: MessageData, chatId: String)

    class Base(private val messageDataSource: MessageDataSource, private val chatDataSource: ChatDataSource, private val userDataSource: UserDataSource) : ChatRepository {

        private val scope = CoroutineScope(Dispatchers.IO)

        override suspend fun createChat(chat: ChatData) {
            chatDataSource.createChat(chat.toDto())
        }

        override suspend fun getChat(chatId: String): Flow<ChatData> {
            val chatModel = chatDataSource.getChat(chatId)
            return when (chatModel) {
                is ChatDto -> {
                    getGroupChat(chatModel)
                }
                is GroupChatDto -> {
                    getPeerToPeerChat(chatModel)
                }
                else -> throw IllegalStateException()
            }
        }

        private suspend fun getGroupChat(chat: ChatDto): Flow<Chat> {
            val user = User(userDataSource.fetchUser(chat.opponent)!!)
            return messageDataSource.getMessages(chat.uid!!).mapNotNull {
                if (it is DataState.Data) {
                    Chat(it.data.map { Message(it as MessageDto, user) }, chat, user)
                } else {
                    null
                }

            }
        }

        private suspend fun getPeerToPeerChat(chat: GroupChatDto): Flow<GroupChat> {
            val users = chat.opponents.map { scope.async { userDataSource.fetchUser(it) } }.awaitAll().mapNotNull { it }.map {
                User(it)
            }
            return messageDataSource.getMessages(chat.uid!!).mapNotNull { messageData ->
                if (messageData is DataState.Data) {
                    GroupChat(messageData.data.map { currentMessage -> MessageGroup(currentMessage as MessageGroupDto, users) }, chat, users)
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
                        getLastGroupChat(it)
                    }
                    is GroupChatDto -> {
                        getPeerToPeerChat(it)
                    }
                    else -> null
                }
            }.toSingleFlow()
        }

        private suspend fun getLastGroupChat(chat: ChatDto): Flow<Chat> {
            val user = User(userDataSource.fetchUser(chat.opponent)!!)
            return messageDataSource.getLastMessage(chat.uid!!).mapNotNull {
                if (it is DataState.Data) {
                    Chat(listOf(Message(it as MessageDto, user)), chat, user)
                } else {
                    null
                }

            }
        }

        override suspend fun sendMessage(message: MessageData, chatId: String) {
            messageDataSource.sendMessage(message.toDto(), chatId)
        }

        private fun List<Flow<ChatData>>.toSingleFlow(): Flow<ArrayList<ChatData>> {
            val firstFlow = first()
            var combinedFlow: Flow<ArrayList<ChatData>> = flow { }
            forEachIndexed { index, flow ->
                if (index == 0) {
                    return@forEachIndexed
                }
                if (index == 1) {
                    combinedFlow = firstFlow.combine(flow) { first, second ->
                        arrayListOf(first, second)
                    }
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