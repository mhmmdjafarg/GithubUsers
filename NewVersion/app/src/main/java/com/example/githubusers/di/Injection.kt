package com.example.githubusers.di


import android.content.Context
import com.example.githubusers.data.remote.ApiConfig
import com.example.githubusers.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}