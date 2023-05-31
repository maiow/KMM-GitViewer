package com.mivanovskaya.gitviewer.shared.data

import com.mivanovskaya.gitviewer.shared.KeyValueStorage
import com.mivanovskaya.gitviewer.shared.data.dto.UserInfoDto
import com.mivanovskaya.gitviewer.shared.domain.AppRepository
import com.mivanovskaya.gitviewer.shared.domain.model.UserInfo
import com.mivanovskaya.gitviewer.shared.domain.toUserInfo
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class AppRepositoryImpl : AppRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val keyValueStorage = KeyValueStorage
    private val client = HttpClient(CIO) {
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(keyValueStorage.authToken ?: "", "")
                }
                sendWithoutRequest { request ->
                    request.url.host == "https://api.github.com/"
                }
            }
        }
        headers {
            append("X-GitHub-Api-Version", "2022-11-28")
            append(HttpHeaders.Accept, "application/vnd.github+json")
        }
        install(ContentNegotiation) {
            json(Json {
                ContentType.Application.Json
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v("HTTP Client", null, message)
                }
            }
            level = LogLevel.HEADERS
        }
    }.also { Napier.base(DebugAntilog()) }

    private fun updateBearerCredentials(newToken: String) {
        client.plugin(Auth).bearer {
            loadTokens { BearerTokens(newToken, "") }
        }
    }

    override suspend fun signIn(token: String): UserInfo = withContext(ioDispatcher) {
        keyValueStorage.authToken = token
        updateBearerCredentials(token)
        val userInfoDto: UserInfoDto = client.get(USER_URL).body()
        userInfoDto.toUserInfo()
    }

    override fun getToken() = keyValueStorage.authToken

    override fun resetToken() {
        keyValueStorage.authToken = null
    }

    override fun saveLogin(login: String) {
        keyValueStorage.login = login
    }

    override fun logout() {
        keyValueStorage.login = null
        keyValueStorage.authToken = null
        client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>().firstOrNull()
            ?.clearToken()
    }

    //TODO: close client on app exit
    //client.close()

    companion object {
        private const val USER_URL = "https://api.github.com/user"
        private const val REPOS_QUANTITY = 10
        private const val PAGES = 1
    }
}