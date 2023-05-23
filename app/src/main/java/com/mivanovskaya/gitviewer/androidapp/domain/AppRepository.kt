package com.mivanovskaya.gitviewer.androidapp.domain

import com.mivanovskaya.gitviewer.androidapp.domain.model.Repo
import com.mivanovskaya.gitviewer.androidapp.domain.model.RepoDetails
import com.mivanovskaya.gitviewer.androidapp.domain.model.UserInfo

/** сделано по ТЗ, в нем AppRepository отвечает и за авторизацию,
 * и за получение данных по репозиториям*/

interface AppRepository {

    suspend fun getRepositories(): List<Repo>
    suspend fun getRepository(repoId: String): RepoDetails
    suspend fun getRepositoryReadme(
        ownerName: String, repositoryName: String,
        branchName: String
    ): String

    suspend fun signIn(token: String): UserInfo
    fun getToken(): String?
    fun resetToken()
    fun saveLogin(login: String)
    fun logout()
}