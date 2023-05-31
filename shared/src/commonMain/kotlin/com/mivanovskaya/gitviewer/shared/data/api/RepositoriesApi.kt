//package com.mivanovskaya.gitviewer.shared.data.api
//
//import com.mivanovskaya.gitviewer.shared.data.dto.RepoDto
//import com.mivanovskaya.gitviewer.shared.data.dto.UserInfoDto
//import retrofit2.http.GET
//import retrofit2.http.Path
//import retrofit2.http.Query
//
//interface RepositoriesApi {
//
//    @GET("users/{user}/repos")
//    suspend fun getRepositories(
//        @Path("user") user: String,
//        @Query("per_page") limit: Int,
//        @Query("page") page: Int
//    ): List<RepoDto>
//
//    @GET("repos/{user}/{repoId}")
//    suspend fun getRepository(
//        @Path("user") user: String,
//        @Path("repoId") repoId: String
//    ): RepoDto
//}