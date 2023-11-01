package com.mivanovskaya.gitviewer.shared.data

import com.mivanovskaya.gitviewer.shared.getSettings
import com.russhwolf.settings.Settings

internal class KeyValueStorage {
    private val settings: Settings = getSettings()

    var authToken: String?
        get() = settings.getString(AUTH_TOKEN_KEY, "")
        set(token) = settings.putString(AUTH_TOKEN_KEY, token ?: "")

    var login: String?
        get() = settings.getString(LOGIN_KEY, "")
        set(login) = settings.putString(LOGIN_KEY, login ?: "")
    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token"
        private const val LOGIN_KEY = "login"
    }
}