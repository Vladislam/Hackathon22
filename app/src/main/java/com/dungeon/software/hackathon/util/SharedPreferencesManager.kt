package com.dungeon.software.hackathon.util

import android.content.Context
import androidx.core.content.edit

class SharedPreferencesManager(context: Context) {

    companion object {
        private const val preferencesKey = "core_preferences"
    }

    private val preferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)

    fun setBoolean(key: PreferencesKey.BooleanKey) = preferences.edit { putBoolean(key.key, key.value) }

    fun getBoolean(key: PreferencesKey.BooleanKey) = preferences.getBoolean(key.key, key.value)

    sealed class PreferencesKey(val key: String, open val value: Any) {
       open class BooleanKey(key: String, override val value: Boolean): PreferencesKey(key, value) {

           class IsUserSeenAnonymousLoginMessage(value: Boolean): BooleanKey("is_user_seen_anonymous_log_in_message", value)
       }
    }

}