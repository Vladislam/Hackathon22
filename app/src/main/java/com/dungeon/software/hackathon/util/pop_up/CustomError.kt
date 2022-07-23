package com.dangeon.software.notes.util.pop_up

import android.content.Context
import com.dungeon.software.hackathon.R

sealed class CustomError(
    val messageString: String? = null,
    val messageInt: Int? = null
) : Throwable() {

    object NoInternetConnection : CustomError(messageInt = R.string.no_internet_connection)

    object SomethingWentWrong : CustomError(messageInt = R.string.something_went_wrong)

    fun getMessage(context: Context) = when {
        messageInt != null -> context.getString(messageInt)
        messageString != null -> messageString
        else -> null
    }

    companion object {
        fun parse(throwable: Throwable): CustomError {
            return if (throwable is CustomError) {
                throwable
            } else {
                SomethingWentWrong
            }
        }
    }

}