package com.mivanovskaya.gitviewer.shared.domain

import com.mivanovskaya.gitviewer.shared.domain.model.Repo
import com.mivanovskaya.gitviewer.shared.domain.model.RepoDetails
import com.mivanovskaya.gitviewer.shared.domain.model.UserInfo

interface AppRepository {

    @Throws(Exception::class)
    suspend fun getRepositories(
        limit: Int,
        page: Int
    ): List<Repo>

    @Throws(Exception::class)
    suspend fun getRepository(repoName: String): RepoDetails

    @Throws(Exception::class)
    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String?

    @Throws(Exception::class)
    suspend fun signIn(token: String): UserInfo

    fun getToken(): String?
    fun resetToken()
    fun saveCredentials(login: String, token: String)
    fun logout()
}