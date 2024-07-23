package com.example.githubusers.di

import com.example.githubusers.ui.main.MainActivity
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}
