package com.mivanovskaya.gitviewer.shared.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserInfoDto(
    val login: String,
    @SerialName("repos_url")
    val reposUrl: String
)