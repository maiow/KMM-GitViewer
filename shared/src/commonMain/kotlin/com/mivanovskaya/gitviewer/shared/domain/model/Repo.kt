package com.mivanovskaya.gitviewer.shared.domain.model

data class Repo(
    val description: String?,
    val id: Int,
    val language: String?,
    val name: String
)

data class RepoDetails(
    val description: String?,
    val forksCount: Int,
    val htmlUrl: String,
    val id: Int,
    val language: String?,
    val license: License?,
    val name: String,
    val stargazersCount: Int,
    val watchersCount: Int,
    val owner: String,
    val defaultBranch: String
)

class License(val name: String)
