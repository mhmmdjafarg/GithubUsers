package com.pbd.githubusers.room

import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite : Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorites ORDER BY uid ASC")
    suspend fun getAllFavorites() : List<Favorite>

    @Query("SELECT * FROM favorites WHERE username = (:username) LIMIT 1")
    suspend fun getFavoriteByUsername(username: String): Favorite




}
