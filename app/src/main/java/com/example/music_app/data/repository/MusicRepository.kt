package com.example.music_app.data.repository

import com.example.music_app.core.database.AppDatabase
import com.example.music_app.data.local.entity.PlaylistEntity
import com.example.music_app.data.local.entity.QuizEntity
import com.example.music_app.data.local.entity.SearchCacheEntity

class MusicRepository(private val db: AppDatabase) {

    suspend fun getAllQuizzes() = db.quizDao().getAllQuizzes()
    suspend fun insertQuiz(quiz: QuizEntity) = db.quizDao().insertQuiz(quiz)
    suspend fun updateQuiz(quiz: QuizEntity) = db.quizDao().updateQuiz(quiz)
    suspend fun deleteQuiz(quiz: QuizEntity) = db.quizDao().deleteQuiz(quiz)

    suspend fun getAllPlaylists() = db.playlistDao().getAllPlaylists()
    suspend fun insertPlaylist(playlist: PlaylistEntity) = db.playlistDao().insertPlaylist(playlist)
    suspend fun updatePlaylist(playlist: PlaylistEntity) = db.playlistDao().updatePlaylist(playlist)
    suspend fun deletePlaylist(playlist: PlaylistEntity) = db.playlistDao().deletePlaylist(playlist)


    suspend fun getCachedSearch(query: String) = db.searchCacheDao().getCachedSearch(query)
    suspend fun cacheSearch(search: SearchCacheEntity) = db.searchCacheDao().cacheSearch(search)
}
