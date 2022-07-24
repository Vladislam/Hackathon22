package com.dungeon.software.hackathon.presentation.chats_list_screen

data class SortState(val sortType: SortType)

sealed class SortType(val desc: Boolean) {
    class Name(desc: Boolean): SortType(desc)
    class LastMessageTime(desc: Boolean): SortType(desc)
}
