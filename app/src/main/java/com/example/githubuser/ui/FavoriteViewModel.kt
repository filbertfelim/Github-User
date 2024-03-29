package com.example.githubuser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.FavoriteUserRepository
import com.example.githubuser.data.local.entity.FavoriteUserEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteUserRepository: FavoriteUserRepository) : ViewModel() {


    fun getFavoriteUsers(): LiveData<List<FavoriteUserEntity>> {
        return favoriteUserRepository.getFavoriteUsers()
    }

    fun insertNewFavorite(username : String, avatarUrl : String) {
        viewModelScope.launch {
            favoriteUserRepository.insertNewFavorite(username, avatarUrl)
        }
    }
    fun deleteFromFavorite(username : String) {
        viewModelScope.launch {
            favoriteUserRepository.deleteFromFavorite(username)
        }
    }
    fun getFavoriteUserByUsername(username : String) : Boolean {
        return favoriteUserRepository.getFavoriteUserByUsername(username)
    }
}