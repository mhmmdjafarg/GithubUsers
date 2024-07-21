package com.pbd.githubusers.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Favorite::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDB : RoomDatabase() {
    abstract fun favoriteDao() : FavoriteDao
    companion object {

        @Volatile private var instance : FavoriteDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            FavoriteDB::class.java,
            "favorites.db"
        ).build()

    }



}
