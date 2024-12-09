package com.example.music_app.ui.quiz

import android.os.Parcelable
import com.example.music_app.ui.playlist.Playlist
import kotlinx.parcelize.Parcelize


@Parcelize
data class Quiz(
    val id: Int = System.currentTimeMillis().toInt(),
    var name: String,
    var associatedPlaylists: List<Playlist>,
    val questions: List<Question> = listOf(),
    var timeLimit: Long
) : Parcelable

@Parcelize
data class Question(
    val id: Int = System.currentTimeMillis().toInt(),
    val questionText: String,
    val trackPreview: String,
    val options: List<String>,
    val correctAnswer: String,
) : Parcelable
