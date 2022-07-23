package com.dungeon.software.hackathon.data.repository

import com.dungeon.software.hackathon.data.data_source.ChatDataSource
import com.dungeon.software.hackathon.data.data_source.MessageDataSource
import com.dungeon.software.hackathon.data.models.ChatDto
import com.dungeon.software.hackathon.data.models.GroupChatDto
import com.dungeon.software.hackathon.data.models.MessageDto
import com.dungeon.software.hackathon.data.models.MessageGroupDto
import com.dungeon.software.hackathon.domain.models.*
import com.dungeon.software.hackathon.util.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

interface ChatRepository {

    suspend fun createChat(chat: ChatData)

    suspend fun getChat(chatId: String): Flow<ChatData>

//    suspend fun getChats(): Flow<ChatData>
//
//    suspend fun sendMessage()

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

//        override suspend fun getChats(): Flow<ChatData> {
//            chatDataSource.getChats().mapNotNull {
//                if (it is DataState.Data) {
//                    it
//                } else {
//                    null
//                }
//            }.map {
//                it.data.map {
//                    when (it.data) {
//                        is ChatDto -> {
//                            getGroupChat(chatModel)
//                        }
//                        is GroupChatDto -> {
//                            getPeerToPeerChat(chatModel)
//                        }
//                        else -> null
//                    }
//                }
//            }
//        }
//
//        override suspend fun sendMessage() {
//            TODO("Not yet implemented")
//        }

        private fun ChatData.toDto() = when (this) {
            is Chat -> {
                ChatDto(this)
            }
            is GroupChat -> {
                GroupChatDto(this)
            }
            else -> throw NullPointerException()
        }

    }

}