package com.pbd.githubusers.retrofit

import com.pbd.githubusers.ItemSearch
import com.pbd.githubusers.ResponseDetailUser
import com.pbd.githubusers.ResponseUserSearch
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") q : String
    ): Call<ResponseUserSearch>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username : String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/following")
    fun getFollowingUser(
        @Path("username") username : String
    ): Call<List<ItemSearch>>

    @GET("users/{username}/followers")
    fun getFollowersUser(
        @Path("username") username : String
    ): Call<List<ItemSearch>>
}