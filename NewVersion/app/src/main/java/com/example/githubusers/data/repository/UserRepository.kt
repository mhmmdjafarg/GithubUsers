package com.example.githubusers.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.githubusers.data.Result
import com.example.githubusers.data.remote.ApiService
import com.example.githubusers.data.remote.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun searchUsers(query: String) : LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchUsers(query)
            val userList = response.items.map { user ->
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