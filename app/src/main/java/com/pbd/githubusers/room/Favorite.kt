package com.pbd.githubusers.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid") val uid : Int,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "image_url") var imageUrl: String,
) : Parcelable

