package com.example.githubusers.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.githubusers.data.Result
import com.example.githubusers.data.model.GithubUser
import com.example.githubusers.data.remote.ApiService
import com.example.githubusers.data.remote.response.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    private lateinit var githubUser: GithubUser

    fun searchUsers(query: String): LiveData<Result<List<User>?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchUsers(query)
            val userList = response.body()?.items?.map { user ->
                User(
                    user.login,
                    user.avatarUrl,
                )
            }
            emit(Result.Success(userList))
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun detailUser(username: String): LiveData<Result<GithubUser>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.detailUser(username)
            val user = response.body()
            if (user != null) {
                githubUser = GithubUser(
                    username = user.login ?: "-",
                    name = user.name ?: "-",
                    company = user.company ?: "-",
                    following = user.following?.toString() ?: "-",
                    follower = user.followers?.toString() ?: "-",
                    repository = user.publicRepos?.toString() ?: "-",
                    avatarURL = user.avatarUrl ?: "-",
                    location = user.location ?: "-"
                )
            }
            emit(Result.Success(githubUser))
        } catch (e: Exception) {
            Log.d("detailUser", "Get detail user error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}