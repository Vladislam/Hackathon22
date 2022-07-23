package com.dungeon.software.hackathon.domain.models

import com.dungeon.software.hackathon.data.models.MessageGroupDto

data class GroupChat(
    val message: List<MessageGroupDto>,
    val uid: String,
    val name: String,
    val opponents: List<User>,

    ): ChatData