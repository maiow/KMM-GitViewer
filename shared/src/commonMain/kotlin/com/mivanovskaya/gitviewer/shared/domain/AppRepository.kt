package com.mivanovskaya.gitviewer.shared.domain

import com.mivanovskaya.gitviewer.shared.domain.model.Repo
import com.mivanovskaya.gitviewer.shared.domain.model.RepoDetails
import com.mivanovskaya.gitviewer.shared.domain.model.UserInfo

/** сделано по ТЗ, в нем AppRepository отвечает и за авторизацию,
 * и за получение данных по репозиториям*/

interface AppRepository {

    @Throws(Exception::class)
    suspend fun getRepositories(): List<Repo>

    @Throws(Exception::class)
    suspend fun getRepository(repoId: String): RepoDetails

    @Throws(Exception::class)
    suspend fun getRepositoryReadme(
        ownerName: String, repositoryName: String,
        branchName: String
    ): String

    @Throws(Exception::class)
    suspend fun signIn(token: String): UserInfo

    fun getToken(): String?
    fun resetToken()
    fun saveLogin(login: String)
    fun logout()
}