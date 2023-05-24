package com.mivanovskaya.gitviewer.androidapp.data

import com.mivanovskaya.gitviewer.androidapp.data.api.RepositoriesApi
import com.mivanovskaya.gitviewer.androidapp.data.api.UserContentApi
import com.mivanovskaya.gitviewer.androidapp.domain.AppRepository
import com.mivanovskaya.gitviewer.androidapp.domain.model.Repo
import com.mivanovskaya.gitviewer.androidapp.domain.model.RepoDetails
import com.mivanovskaya.gitviewer.androidapp.domain.model.UserInfo
import com.mivanovskaya.gitviewer.androidapp.domain.toListRepo
import com.mivanovskaya.gitviewer.androidapp.domain.toRepoDetails
import com.mivanovskaya.gitviewer.androidapp.domain.toUserInfo
import com.mivanovskaya.gitviewer.shared.KeyValueStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val repositoriesApi: RepositoriesApi,
    private val userApi: UserContentApi,
    private val ioDispatcher: CoroutineDispatcher,
) : AppRepository {

    private val keyValueStorage = KeyValueStorage

    override suspend fun getRepositories(): List<Repo> = withContext(ioDispatcher) {
        repositoriesApi.getRepositories(keyValueStorage.login ?: "", REPOS_QUANTITY, PAGES)
            .toListRepo()
    }

    override suspend fun getRepository(repoId: String): RepoDetails = withContext(ioDispatcher) {
        repositoriesApi.getRepository(keyValueStorage.login ?: "", repoId).toRepoDetails()
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String = withContext(ioDispatcher) {
        userApi.getRepositoryReadme(ownerName, repositoryName, branchName)
    }

    override suspend fun signIn(token: String): UserInfo = withContext(ioDispatcher) {
        keyValueStorage.authToken = token
        repositoriesApi.getUserInfo().toUserInfo()
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
    }

    companion object {
        private const val REPOS_QUANTITY = 10
        private const val PAGES = 1
    }
}