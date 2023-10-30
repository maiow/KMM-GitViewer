package com.mivanovskaya.gitviewer.shared
import com.mivanovskaya.gitviewer.shared.di.appModule
import com.mivanovskaya.gitviewer.shared.domain.AppRepository
import com.mivanovskaya.gitviewer.shared.domain.model.Repo
import com.mivanovskaya.gitviewer.shared.domain.model.RepoDetails
import com.mivanovskaya.gitviewer.shared.domain.model.UserInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

// Injection Boostrap Helper
class AppRepositoryHelper : KoinComponent {
    val repository: AppRepository by inject()

    //TODO: clean up
    fun getToken(): String? = repository.getToken()
    fun resetToken() = repository.resetToken()
    fun saveCredentials(login: String, token: String) = repository.saveCredentials(login, token)
    fun logout() = repository.logout()

    suspend fun getRepositories(
        limit: Int,
        page: Int
    ): List<Repo> = repository.getRepositories(limit, page)

    suspend fun getRepository(repoName: String): RepoDetails = repository.getRepository(repoName)
    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String? = repository.getRepositoryReadme(ownerName, repositoryName, branchName)

    suspend fun signIn(token: String): UserInfo = repository.signIn(token)
}
fun initKoin(){

    startKoin {
        modules(appModule())
    }
//
//    val koinApp = startKoin {
//        modules(appModule())
//    }.koin
//
//    koinApp.get<AppRepository>()
}