package com.example.music_app.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.music_app.data.local.dao.PlaylistDao
import com.example.music_app.data.local.dao.QuizDao
import com.example.music_app.data.local.dao.SearchCacheDao
import com.example.music_app.data.local.entity.Converters
import com.example.music_app.data.local.entity.PlaylistEntity
import com.example.music_app.data.local.entity.QuizEntity
import com.example.music_app.data.local.entity.SearchCacheEntity


@Database(
    entities = [QuizEntity::class, PlaylistEntity::class, SearchCacheEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun searchCacheDao(): SearchCacheDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "music_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
