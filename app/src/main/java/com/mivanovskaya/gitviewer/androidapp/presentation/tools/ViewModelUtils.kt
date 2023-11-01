package com.mivanovskaya.gitviewer.androidapp.presentation.tools

import com.mivanovskaya.gitviewer.androidapp.R
import com.mivanovskaya.gitviewer.shared.data.exceptions.BadSerializationException
import com.mivanovskaya.gitviewer.shared.data.exceptions.NoInternetException

suspend fun <T> requestWithErrorHandling(
    block: suspend () -> Unit,
    errorFactory: (Boolean, StringValue) -> T,
    setState: (T) -> Unit
) {
    try {
        block()
    } catch (e: NoInternetException) {
        setState(errorFactory(true, StringValue.StringResource(R.string.connection_error)))
    } catch (e: BadSerializationException) {
        setState(errorFactory(false, StringValue.StringResource(R.string.serialization_error)))
    } catch (e: Exception) {
        setState(errorFactory(false, StringValue.StringResource(R.string.server_connection_error)))
    }
}