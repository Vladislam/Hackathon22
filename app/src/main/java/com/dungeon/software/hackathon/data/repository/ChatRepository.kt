package com.dungeon.software.hackathon.data.repository

import com.dungeon.software.hackathon.data.data_source.ChatDataSource
import com.dungeon.software.hackathon.data.data_source.MessageDataSource
import com.dungeon.software.hackathon.data.models.MessageDataDto
import com.dungeon.software.hackathon.domain.models.Chat
import com.dungeon.software.hackathon.domain.models.Message
import com.dungeon.software.hackathon.domain.models.MessageData
import java.util.concurrent.Flow

interface ChatRepository {

//    suspend fun getChat(chatId: String): Chat
//
//    suspend fun sendMessage()
//
//    suspend fun getMessages(): Flow<MessageData>

    class Base(private val messageDataSource: MessageDataSource, private val chatDataSource: ChatDataSource): ChatRepository {



    }

}