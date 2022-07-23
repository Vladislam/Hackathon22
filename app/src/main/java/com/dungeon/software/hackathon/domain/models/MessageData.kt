package com.dungeon.software.hackathon.domain.models

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface MessageData {

    fun getMessageUid(): String
    fun getMessageText(): String?
    fun getMessageImageUrl(): String?
    fun getMessageVideoUrl(): String?
    fun getMessageTimeSent(): Long
    fun getIsMessageSeen(): Boolean
    fun getSender(): User
    fun getMessageTimeSeen(): Long?
    fun isMine(): Boolean = getSender().uid == Firebase.auth.currentUser?.uid
}