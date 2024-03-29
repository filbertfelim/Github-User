package com.example.githubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite")
@Parcelize
class FavoriteUserEntity(
    @field:ColumnInfo(name = "username")
    @PrimaryKey(autoGenerate = false)
    var username: String,

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String?,
) : Parcelable