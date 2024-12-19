package com.example.music_app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.music_app.data.local.dao.PlaylistDao
import com.example.music_app.data.local.dao.QuizDao
import com.example.music_app.data.local.entity.Converters
import com.example.music_app.data.local.entity.PlaylistEntity
import com.example.music_app.data.local.entity.QuizEntity


@Database(
    entities = [QuizEntity::class, PlaylistEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun playlistDao(): PlaylistDao

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
