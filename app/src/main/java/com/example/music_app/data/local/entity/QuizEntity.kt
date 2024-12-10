package com.example.music_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.quiz.Question
import com.example.music_app.ui.quiz.Quiz


@Entity(tableName = "quizzes")
data class QuizEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val associatedPlaylist: List<Playlist>,
    val questions: List<Question>,
    val timeLimit: Long
)

fun QuizEntity.toQuiz(): Quiz {
    return Quiz(
        id = this.id,
        name = this.name,
        associatedPlaylists = this.associatedPlaylist,
        questions = this.questions,
        timeLimit = this.timeLimit
    )
}
