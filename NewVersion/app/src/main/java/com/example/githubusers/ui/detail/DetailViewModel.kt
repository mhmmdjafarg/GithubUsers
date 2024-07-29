package com.example.githubusers.ui.detail

import androidx.lifecycle.ViewModel
import com.example.githubusers.data.repository.UserRepository
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getDetailUser(username: String) = userRepository.detailUser(username)

    fun getFollowings(username: String) = userRepository.getFollowings(username)

    fun getFollowers(username: String) = userRepository.getFollowers(username)

}