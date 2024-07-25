package com.example.githubusers.di

import com.example.githubusers.ui.detail.DetailActivity
import com.example.githubusers.ui.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(detailActivity: DetailActivity)
}
