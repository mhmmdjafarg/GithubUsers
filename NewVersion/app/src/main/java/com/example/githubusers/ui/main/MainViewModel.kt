package com.example.githubusers.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.repository.UserRepository

class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    // Function to update the search query
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchUsers(query: String) = userRepository.searchUsers(query)

}