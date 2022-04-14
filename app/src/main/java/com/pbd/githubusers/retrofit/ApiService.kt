package com.pbd.githubusers.retrofit

import com.pbd.githubusers.ItemSearch
import com.pbd.githubusers.ResponseDetailUser
import com.pbd.githubusers.ResponseUserSearch
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUsers(
        @Query("q") q : String
    ): Call<ResponseUserSearch>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username : String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowingUser(
        @Path("username") username : String
    ): Call<List<ItemSearch>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowersUser(
        @Path("username") username : String
    ): Call<List<ItemSearch>>
}