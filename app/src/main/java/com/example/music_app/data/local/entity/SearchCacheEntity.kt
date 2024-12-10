package com.example.music_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_cache")
data class SearchCacheEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String,
    val results: List<Any>
)
