package com.mivanovskaya.gitviewer.androidapp.presentation.detail_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gitviewer.shared.domain.AppRepository
import com.mivanovskaya.gitviewer.shared.domain.model.RepoDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
//import retrofit2.HttpException
import java.io.IOException

class RepositoryInfoViewModel(private val repository: AppRepository) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _readmeState = MutableStateFlow<ReadmeState>(ReadmeState.Loading)
    val readmeState = _readmeState.asStateFlow()

    fun onGettingArgument(repoId: String) = getRepoInfo(repoId)

    fun onLogoutButtonPressed() = repository.logout()

    fun onRetryButtonClick(repoId: String) = getRepoInfo(repoId)

    private fun getRepoInfo(repoId: String) {
        val job = Job()
        viewModelScope.launch(job) {
            try {
                _state.value = State.Loading
                val repo = repository.getRepository(repoId)
                _state.value = State.Loaded(repo, ReadmeState.Loading)
                getReadme(repo, repoId)

//            } catch (e: HttpException) {
//                handleHttpException(e)
            } catch (e: IOException) {
                handleNetworkException()
            } catch (e: Exception) {
                handleOtherException(e)
            }
            job.cancel()
        }
    }

    private suspend fun getReadme(repo: RepoDetails, repoId: String) {
        val readme = repository.getRepositoryReadme(
            ownerName = repo.login,
            repositoryName = repoId,
            branchName = repo.defaultBranch
        )
        if (readme.isBlank()) _readmeState.value = ReadmeState.Empty
        else _readmeState.value = ReadmeState.Loaded(readme)
    }

//    private fun handleHttpException(e: HttpException) {
//        if (_state.value is State.Loading) {
//            _state.value = State.Error(e.message.toString())
//            _readmeState.value = ReadmeState.Error(e.message.toString())
//        } else {
//            if (e.code() == 404) _readmeState.value = ReadmeState.Empty
//            else _readmeState.value = ReadmeState.Error(e.message.toString())
//        }
//    }

    private fun handleNetworkException() {
        if (_state.value is State.Loaded) _readmeState.value = ReadmeState.Error(NO_INTERNET)
        else {
            _state.value = State.Error(NO_INTERNET)
            _readmeState.value = ReadmeState.Error(NO_INTERNET)
        }
    }

    private fun handleOtherException(e: Exception) {
        if (_state.value is State.Loaded) _readmeState.value =
            ReadmeState.Error(e.message.toString())
        else {
            _state.value = State.Error(e.message.toString())
            _readmeState.value = ReadmeState.Error(e.message.toString())
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State

        data class Loaded(
            val githubRepo: RepoDetails, val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

    companion object {
        const val NO_INTERNET = "NO_INTERNET"
    }
}