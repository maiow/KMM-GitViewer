package com.mivanovskaya.gitviewer.shared.data

import com.mivanovskaya.gitviewer.shared.data.exceptions.BadSerializationException
import com.mivanovskaya.gitviewer.shared.data.exceptions.NoInternetException
import io.github.aakira.napier.Napier
import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.errors.IOException

//TODO: попробовать обобщить работу с ошибками в AppRepository
suspend fun <T> requestWithExceptionsCatching(
    block: suspend () -> T
    // additionalException: (Exception) -> Unit
) {
    try {
        block()
    } catch (e: JsonConvertException) {
        Napier.d(tag = "Napier", message = "Serialization exception: ${e.message}")
        throw BadSerializationException(e.message.toString())
    } catch (e: IOException) {
        Napier.d(tag = "Napier", message = "No Internet connection: ${e.message}")
        throw NoInternetException(e.message.toString())
    }
//    if (additionalException != null) {
//catch (e: additionalException) {
//
//}
//    } else
//    catch (e: Exception) {
//        Napier.e("Napier: Some error: ", e)
//        throw e
//    }
}