package com.example.githubusers.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    suspend fun searchUsers(@Query("q") username: String): UserSearchResponse
}