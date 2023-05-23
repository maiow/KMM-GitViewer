package com.mivanovskaya.gitviewer.androidapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDto(
    val login: String,
    @SerialName("repos_url")
    val reposUrl: String
)