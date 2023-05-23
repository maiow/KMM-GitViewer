package com.mivanovskaya.gitviewer.androidapp.data.api

import com.mivanovskaya.gitviewer.androidapp.data.dto.RepoDto
import com.mivanovskaya.gitviewer.androidapp.data.dto.UserInfoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepositoriesApi {

    @GET("user")
    suspend fun getUserInfo(): UserInfoDto

    @GET("users/{user}/repos")
    suspend fun getRepositories(
        @Path("user") user: String,
        @Query("per_page") limit: Int,
        @Query("page") page: Int
    ): List<RepoDto>

    @GET("repos/{user}/{repoId}")
    suspend fun getRepository(
        @Path("user") user: String,
        @Path("repoId") repoId: String
    ): RepoDto
}