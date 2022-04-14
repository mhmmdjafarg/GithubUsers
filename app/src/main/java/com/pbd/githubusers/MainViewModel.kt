package com.pbd.githubusers

import android.util.Log
import androidx.lifecycle.*
import com.pbd.githubusers.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    private val _responseUserSearch = MutableLiveData<ResponseUserSearch>()
    val responseUserSearch: LiveData<ResponseUserSearch> = _responseUserSearch

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getUserFromQueryApi(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(username)
        client.enqueue(object : Callback<ResponseUserSearch> {
            override fun onResponse(
                call: Call<ResponseUserSearch>,
                response: Response<ResponseUserSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.e(TAG, "OnSuccess: ${responseBody.totalCount}")
                        _responseUserSearch.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseUserSearch>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}