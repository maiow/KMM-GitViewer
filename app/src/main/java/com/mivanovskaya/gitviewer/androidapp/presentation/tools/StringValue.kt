package com.mivanovskaya.gitviewer.androidapp.presentation.tools

import android.content.Context
import androidx.annotation.StringRes

sealed class StringValue {
    data class DynamicString(val value: String) : StringValue()
    object Empty : StringValue()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : StringValue()

    fun asString(context: Context?): String {
        return when (this) {
            is Empty -> EMPTY
            is DynamicString -> value
            is StringResource -> context?.getString(resId, *args).orEmpty()
        }
    }

    companion object {
        const val EMPTY = ""
    }
}
