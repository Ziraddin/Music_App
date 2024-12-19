package com.example.music_app.ui.quiz.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val id: Int = System.currentTimeMillis().toInt(),
    val questionText: String,
    val trackPreview: String,
    val options: List<String>,
    val correctAnswer: String,
) : Parcelable

