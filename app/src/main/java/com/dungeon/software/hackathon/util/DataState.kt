package com.dungeon.software.hackathon.util

sealed class DataState<T> {
    class Data<T>(val data: T): DataState<T>()
    class Error<T>(val error: Throwable): DataState<T>()
}