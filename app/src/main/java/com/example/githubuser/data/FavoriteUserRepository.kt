package com.example.githubuser.data

import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.entity.FavoriteUserEntity
import com.example.githubuser.data.local.room.FavoriteUserDao

class FavoriteUserRepository private constructor(
    private val favoriteUserDao: FavoriteUserDao
) {
    fun getFavoriteUsers(): LiveData<List<FavoriteUserEntity>> {
        return favoriteUserDao.getFavoriteUsers()
    }

    suspend fun insertNewFavorite(username : String, avatarUrl : String) {
        favoriteUserDao.insertNewFavorite(FavoriteUserEntity(username, avatarUrl))

    }

    suspend fun deleteFromFavorite(username : String) {
        favoriteUserDao.deleteFromFavorite(username)
    }

    fun getFavoriteUserByUsername(username: String): Boolean {
        return favoriteUserDao.getFavoriteUserByUsername(username)
    }

    companion object {
        @Volatile
        private var instance: FavoriteUserRepository? = null
        fun getInstance(
            favoriteUserDao: FavoriteUserDao
        ): FavoriteUserRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteUserRepository(favoriteUserDao)
            }.also { instance = it }
    }
}