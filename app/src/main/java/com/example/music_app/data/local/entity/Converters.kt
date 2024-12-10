package com.example.music_app.data.local.entity

import androidx.room.TypeConverter
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.quiz.Question
import com.example.music_app.ui.quiz.Quiz
import com.example.music_app.ui.search.data.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {

    private val gson = Gson()


    @TypeConverter
    fun fromTrackList(value: List<Track>): String = gson.toJson(value)

    @TypeConverter
    fun toTrackList(value: String): List<Track> =
        gson.fromJson(value, object : TypeToken<List<Track>>() {}.type)

    @TypeConverter
    fun fromQuestionList(value: List<Question>): String = gson.toJson(value)

    @TypeConverter
    fun toQuestionList(value: String): List<Question> =
        gson.fromJson(value, object : TypeToken<List<Question>>() {}.type)

    @TypeConverter
    fun fromPlaylistList(value: List<Playlist>): String = gson.toJson(value)

    @TypeConverter
    fun toPlaylistList(value: String): List<Playlist> =
        gson.fromJson(value, object : TypeToken<List<Playlist>>() {}.type)

    @TypeConverter
    fun fromAnyList(value: List<Any>): String = gson.toJson(value)

    @TypeConverter
    fun toAnyList(value: String): List<Any> =
        gson.fromJson(value, object : TypeToken<List<Any>>() {}.type)

    @TypeConverter
    fun fromQuiz(quiz: Quiz): String = gson.toJson(quiz)

    @TypeConverter
    fun toQuiz(value: String): Quiz = gson.fromJson(value, Quiz::class.java)

    @TypeConverter
    fun fromQuizList(value: List<Quiz>): String = gson.toJson(value)

    @TypeConverter
    fun toQuizList(value: String): List<Quiz> =
        gson.fromJson(value, object : TypeToken<List<Quiz>>() {}.type)
}

