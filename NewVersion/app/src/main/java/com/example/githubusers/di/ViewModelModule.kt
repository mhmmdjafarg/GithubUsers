package com.example.githubusers.di

import androidx.lifecycle.ViewModelProvider
import com.example.githubusers.data.repository.UserRepository
import com.example.githubusers.ui.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(userRepository: UserRepository): ViewModelProvider.Factory {
        return ViewModelFactory(userRepository)
    }
}