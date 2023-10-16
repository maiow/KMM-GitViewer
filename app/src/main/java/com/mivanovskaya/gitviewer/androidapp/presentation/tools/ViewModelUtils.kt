package com.mivanovskaya.gitviewer.androidapp.presentation.tools

import com.mivanovskaya.gitviewer.androidapp.R
import java.io.IOException
import java.nio.channels.UnresolvedAddressException

suspend fun <T> requestWithErrorHandling(
    block: suspend () -> Unit,
    errorFactory: (Boolean, StringValue) -> T,
    setState: (T) -> Unit
) {
    try {
        block()
    } catch (e: UnresolvedAddressException) {
        setState(errorFactory(true, StringValue.StringResource(R.string.connection_error)))
    } catch (e: IOException) {
        setState(errorFactory(true, StringValue.StringResource(R.string.connection_error)))
    } catch (e: Exception) {
        setState(errorFactory(false, StringValue.DynamicString(e.message.toString())))
    }
}