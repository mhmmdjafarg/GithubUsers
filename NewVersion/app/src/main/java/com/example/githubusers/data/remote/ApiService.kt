package com.example.githubusers.data.remote

import com.example.githubusers.data.remote.response.UserDetailResponse
import com.example.githubusers.data.remote.response.UserSearchResponse
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