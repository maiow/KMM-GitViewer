package com.mivanovskaya.gitviewer.shared.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RepoDto(
    val description: String?,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Int,
    val language: String?,
    val license: LicenseDto?,
    val name: String,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    val owner: OwnerDto,
    @SerialName("default_branch")
    val defaultBranch: String
)

@Serializable
internal data class LicenseDto(val name: String)

@Serializable
internal data class OwnerDto(val login: String)