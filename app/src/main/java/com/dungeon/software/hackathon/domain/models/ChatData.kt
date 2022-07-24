package com.dungeon.software.hackathon.domain.models

interface ChatData {
    fun getChatImageUrl(): String?
    fun getChatName(): String
    fun getChatLastMessageTime(): Long
    fun getChatLastMessage(): String
}