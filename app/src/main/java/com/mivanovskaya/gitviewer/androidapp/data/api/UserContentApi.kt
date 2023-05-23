package com.mivanovskaya.gitviewer.androidapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface UserContentApi {
    @GET("{ownerName}/{repositoryName}/{branchName}/README.md")
    suspend fun getRepositoryReadme(
        @Path("ownerName") ownerName: String,
        @Path("repositoryName") repositoryName: String,
        @Path("branchName") branchName: String
    ): String
}