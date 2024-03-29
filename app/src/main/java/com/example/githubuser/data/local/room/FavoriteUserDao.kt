package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuser.data.local.entity.FavoriteUserEntity

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite ORDER BY username ASC")
    fun getFavoriteUsers(): LiveData<List<FavoriteUserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewFavorite(news: FavoriteUserEntity)

    @Query("DELETE FROM favorite WHERE username = :username")
    suspend fun deleteFromFavorite(username: String)

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE username = :username)")
    fun getFavoriteUserByUsername(username: String): Boolean
}