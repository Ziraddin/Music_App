package com.example.music_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.music_app.data.local.entity.SearchCacheEntity

@Dao
interface SearchCacheDao {
    @Query("SELECT * FROM search_cache WHERE query = :query")
    suspend fun getCachedSearch(query: String): SearchCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheSearch(search: SearchCacheEntity)

    @Query("DELETE FROM search_cache")
    suspend fun clearCache()
}
