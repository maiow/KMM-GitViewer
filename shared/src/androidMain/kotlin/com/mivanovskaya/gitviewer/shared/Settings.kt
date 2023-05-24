package com.mivanovskaya.gitviewer.shared

import android.content.Context
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

var appContext: Context? = null
const val SHARED_PREF_NAME = "shared_name"

actual fun getSettings(): Settings {

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        SharedPreferencesSettings(
            appContext!!.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        )
    } else {
        val masterKey = MasterKey.Builder(appContext!!)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val delegate = EncryptedSharedPreferences.create(
            appContext!!,
            SHARED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return SharedPreferencesSettings(delegate)
    }
}


