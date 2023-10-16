package com.mivanovskaya.gitviewer.androidapp.presentation.detail_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.StringValue
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.requestWithErrorHandling
import com.mivanovskaya.gitviewer.shared.domain.AppRepository
import com.mivanovskaya.gitviewer.shared.domain.model.RepoDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoryInfoViewModel(
    private val repository: AppRepository,
    private val repoName: String
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private var job: Job? = null

    init {
        getRepoInfo(repoName)
    }

    fun onLogoutButtonPressed(): Unit = repository.logout()

    fun onRetryButtonClick(): Unit = getRepoInfo(repoName)

    private fun getRepoInfo(repoName: String) {
        job?.cancel()
        job = viewModelScope.launch {
            requestWithErrorHandling(
                block = {
                    _state.value = State.Loading
                    val repo: RepoDetails = repository.getRepository(repoName)
                    val newState = State.Loaded(repo, ReadmeState.Loading)
                    _state.value = newState
                    getReadme(newState)
                },
                errorFactory = State::Error,
                setState = { _state.value = it }
            )
        }
    }

    private suspend fun getReadme(state: State.Loaded) {
        requestWithErrorHandling(
            block = {
                val readme: String? = repository.getRepositoryReadme(
                    ownerName = state.githubRepo.owner,
                    repositoryName = state.githubRepo.name,
                    branchName = state.githubRepo.defaultBranch
                )

                if (readme.isNullOrBlank()) {
                    _state.value = state.copy(readmeState = ReadmeState.Empty)
                } else {
                    _state.value = state.copy(readmeState = ReadmeState.Loaded(readme))
                }
            },
            errorFactory = ReadmeState::Error,
            setState = { _state.value = state.copy(readmeState = it) }
        )
    }


    sealed interface State {
        object Loading : State
        data class Error(val isNetworkError: Boolean, val error: StringValue) : State
        data class Loaded(
            val githubRepo: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val isNetworkError: Boolean, val error: StringValue) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }
}