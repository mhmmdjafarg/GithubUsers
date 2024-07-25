package com.example.githubusers.data.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    suspend fun searchUsers(@Query("q") username: String): Response<UserSearchResponse>

    @GET("/users/{username}")
    suspend fun detailUser(
        @Path("username") username: String
    ) : Response<UserDetailResponse>
}