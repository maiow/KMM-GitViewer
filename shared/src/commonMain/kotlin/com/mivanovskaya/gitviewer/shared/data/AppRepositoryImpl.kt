package com.mivanovskaya.gitviewer.shared.data

import com.mivanovskaya.gitviewer.shared.data.dto.RepoDto
import com.mivanovskaya.gitviewer.shared.data.dto.UserInfoDto
import com.mivanovskaya.gitviewer.shared.domain.AppRepository
import com.mivanovskaya.gitviewer.shared.domain.model.Repo
import com.mivanovskaya.gitviewer.shared.domain.model.RepoDetails
import com.mivanovskaya.gitviewer.shared.domain.model.UserInfo
import com.mivanovskaya.gitviewer.shared.domain.toRepo
import com.mivanovskaya.gitviewer.shared.domain.toRepoDetails
import com.mivanovskaya.gitviewer.shared.domain.toUserInfo
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
import io.ktor.http.appendPathSegments
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

internal class AppRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val keyValueStorage: KeyValueStorage,
    private val antilog: Antilog
) : AppRepository {
    private val client = HttpClient() {
        install(Auth) {
            bearer {
                if (keyValueStorage.authToken != null) {
                    loadTokens {
                        BearerTokens(keyValueStorage.authToken!!, "")
                    }
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
        expectSuccess = true
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = " Napier HTTP Client", message = message)
                }
            }
            level = LogLevel.HEADERS
        }
    }.also { Napier.base(antilog) }

    private fun updateBearerToken(newToken: String) {
        client.plugin(Auth).bearer {
            loadTokens { BearerTokens(newToken, "") }
        }
    }

    override suspend fun signIn(token: String): UserInfo =
        requestWithExceptionsCatching(dispatcher = ioDispatcher) {
            updateBearerToken(token)
            val userInfoDto: UserInfoDto = client.get(USER_URL).body()
            userInfoDto.toUserInfo()
        }

    override suspend fun getRepositories(limit: Int, page: Int): List<Repo> =
        requestWithExceptionsCatching(dispatcher = ioDispatcher) {
            val repos: List<RepoDto> = client.get(USERS_URL) {
                url {
                    appendPathSegments(
                        requireNotNull(keyValueStorage.login) {
                            "Error: authorized username not found in storage"
                        },
                        "repos"
                    )
                    parameters.append("per_page", limit.toString())
                    parameters.append("page", page.toString())
                }
            }.body()
            repos.map { it.toRepo() }
        }

    override suspend fun getRepository(repoName: String, ownerName: String): RepoDetails =
        requestWithExceptionsCatching(dispatcher = ioDispatcher) {
            val repo: RepoDto = client.get(REPOS_URL) {
                url {
                    appendPathSegments(
                        ownerName,
                        repoName
                    )
                }
            }.body()
            repo.toRepoDetails()
        }

    override suspend fun getRepositoryReadme(
        ownerName: String, repositoryName: String, branchName: String
    ): String? =
        requestWithExceptionsCatching(dispatcher = ioDispatcher) {
            val response = client.get(USER_CONTENT_URL) {
                url {
                    appendPathSegments(ownerName, repositoryName, branchName, "README.md")
                }
            }
            response.body()
        }

    override fun getToken() = keyValueStorage.authToken

    override fun resetToken() {
        keyValueStorage.authToken = null
    }

    override fun saveCredentials(login: String, token: String) {
        keyValueStorage.login = login
        keyValueStorage.authToken = token
    }

    override fun logout() {
        keyValueStorage.login = null
        keyValueStorage.authToken = null
        client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>().firstOrNull()
            ?.clearToken()
    }

    companion object {
        private const val USER_URL = "https://api.github.com/user"
        private const val USERS_URL = "https://api.github.com/users"
        private const val REPOS_URL = "https://api.github.com/repos"
        private const val USER_CONTENT_URL = "https://raw.githubusercontent.com"
    }
}
