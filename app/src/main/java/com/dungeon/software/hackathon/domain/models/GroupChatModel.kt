package com.dungeon.software.hackathon.domain.models

import com.dungeon.software.hackathon.data.models.MessageGroup

data class GroupChatModel(
    val message: List<MessageGroup>,
    val uid: String,
    val name: String,
    val opponents: List<UserModel>,

)