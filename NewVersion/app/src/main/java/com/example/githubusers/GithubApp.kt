package com.example.githubusers

import android.app.Application
import com.example.githubusers.di.AppComponent
import com.example.githubusers.di.AppModule
import com.example.githubusers.di.DaggerAppComponent

class GithubApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
    }
}