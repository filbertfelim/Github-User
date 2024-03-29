package com.example.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.data.remote.response.GithubResponse
import com.example.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _githubSearch = MutableLiveData<GithubResponse>()
    val githubSearch: LiveData<GithubResponse> = _githubSearch
    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isFailed = MutableLiveData<Boolean>()
    val isFailed: LiveData<Boolean> = _isFailed
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery
    companion object{
        private const val TAG = "MainViewModel"
    }
    init {
        findUsers("arif")
    }
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun findUsers(query : String) {
        _isLoading.value = true
        _isFailed.value = false
        val client = ApiConfig.getApiService().searchUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isFailed.value = false
                    _githubSearch.value = response.body()
                    _listUsers.value = response.body()?.items as List<ItemsItem>?
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isFailed.value = true
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _isFailed.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}