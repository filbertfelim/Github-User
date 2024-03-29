package com.example.githubuser.data.remote.retrofit

import com.example.githubuser.BuildConfig
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.GithubResponse
import com.example.githubuser.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}