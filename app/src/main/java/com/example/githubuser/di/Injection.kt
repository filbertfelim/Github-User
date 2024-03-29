package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.FavoriteUserRepository
import com.example.githubuser.data.local.room.FavoriteUserDatabase

object Injection {
    fun provideRepository(context: Context): FavoriteUserRepository {
        val database = FavoriteUserDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()
        return FavoriteUserRepository.getInstance(dao)
    }
}