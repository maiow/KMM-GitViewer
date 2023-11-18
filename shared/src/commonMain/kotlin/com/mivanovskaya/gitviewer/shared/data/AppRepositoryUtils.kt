package com.mivanovskaya.gitviewer.shared.data

import com.mivanovskaya.gitviewer.shared.data.exceptions.BadSerializationException
import com.mivanovskaya.gitviewer.shared.data.exceptions.InvalidTokenException
import com.mivanovskaya.gitviewer.shared.data.exceptions.NoInternetException
import com.mivanovskaya.gitviewer.shared.data.exceptions.ServerConnectionException
import com.mivanovskaya.gitviewer.shared.domain.model.UserInfo
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend inline fun <reified T> requestWithExceptionsCatching(
    dispatcher: CoroutineDispatcher,
    crossinline block: suspend () -> T
): T? {
    return withContext(dispatcher) {
        try {
            block()
        } catch (e: JsonConvertException) {
            Napier.d(tag = "Napier", message = "Serialization exception: ${e.message}")
            throw BadSerializationException(e.message.toString())

        } catch (e: IOException) {
            Napier.d(tag = "Napier", message = "No Internet connection: ${e.message}")
            throw NoInternetException(e.message.toString())

            //codes 300-399
        } catch (e: RedirectResponseException) {
            Napier.e("Napier: Redirect response exception: ${e.message}")
            throw ServerConnectionException(e.message)

            //codes 400-499
        } catch (e: ClientRequestException) {
            Napier.e("Napier: Client request exception: ${e.message}")
            if (T::class == String::class && (e.response.status == HttpStatusCode.NotFound)) {
                return@withContext null
            } else if (T::class == UserInfo::class && (e.response.status == HttpStatusCode.Unauthorized)) {
                throw InvalidTokenException(e.response.status.description)
            } else {
                throw ServerConnectionException(e.message)
            }

            //codes 500-599
        } catch (e: ServerResponseException) {
            Napier.e("Napier: Server response exception: ${e.message}")
            throw ServerConnectionException(e.message)

        } catch (e: Exception) {
            Napier.e("Napier: Some error: ", e)
            throw e
        }
    }
}