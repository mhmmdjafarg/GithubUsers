package com.example.githubusers.di

import androidx.lifecycle.ViewModelProvider
import com.example.githubusers.data.remote.ApiService
import com.example.githubusers.data.remote.AuthInterceptor
import com.example.githubusers.data.repository.UserRepository
import com.example.githubusers.ui.ViewModelFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    private val baseUrl: String = "https://api.github.com/"

    @Singleton
    @Provides
    fun providesRetrofit() : Retrofit{
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(""))
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}