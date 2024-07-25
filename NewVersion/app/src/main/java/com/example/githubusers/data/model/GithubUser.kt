package com.example.githubusers.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubUser(
    val username: String,
    val name: String,
    val avatarURL: String,
    val follower: String,
    val following: String,
    val company: String,
    val location: String,
    val repository: String
) : Parcelable
