package com.example.githubusers.di

import com.example.githubusers.data.remote.ApiService
import com.example.githubusers.data.repository.UserRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepository(apiService)
    }
}