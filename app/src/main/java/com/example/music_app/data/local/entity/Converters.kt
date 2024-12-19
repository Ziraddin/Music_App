package com.example.music_app.data.local.entity

import androidx.room.TypeConverter
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.quiz.data.Question
import com.example.music_app.ui.search.data.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromMutableTrackList(value: MutableList<Track>): String = gson.toJson(value)

    @TypeConverter
    fun toMutableTrackList(value: String): MutableList<Track> =
        gson.fromJson(value, object : TypeToken<MutableList<Track>>() {}.type)

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

}

