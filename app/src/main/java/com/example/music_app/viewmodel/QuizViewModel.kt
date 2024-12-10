package com.example.music_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_app.data.local.entity.toQuiz
import com.example.music_app.data.repository.MusicRepository
import com.example.music_app.ui.quiz.Quiz
import com.example.music_app.ui.quiz.toQuizEntity
import kotlinx.coroutines.launch

sealed class QuizState {
    data object Idle : QuizState()
    data object Loading : QuizState()
    data class Success(val result: List<Quiz>) : QuizState()
    data class Error(val message: String) : QuizState()
}

class QuizViewModel(private val repository: MusicRepository) : ViewModel() {
    private val _quizState = MutableLiveData<QuizState>(QuizState.Idle)
    val quizState: MutableLiveData<QuizState> = _quizState

    fun getQuizzes() {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
                val quizzes = repository.getAllQuizzes().map { it.toQuiz() }
                _quizState.value = QuizState.Success(quizzes)
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to load quizzes: ${e.message}")
            }
        }
    }

    fun addQuiz(quiz: Quiz) {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
                val quizEntity = quiz.toQuizEntity()
                repository.insertQuiz(quizEntity)
                getQuizzes()
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to add quiz: ${e.message}")
            }
        }
    }

    fun removeQuiz(quiz: Quiz) {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
                val quizEntity = quiz.toQuizEntity()
                repository.deleteQuiz(quizEntity)
                getQuizzes()
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to remove quiz: ${e.message}")
            }
        }
    }

    fun updateQuiz(quiz: Quiz) {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
                val quizEntity = quiz.toQuizEntity()
                repository.updateQuiz(quizEntity)
                getQuizzes()
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to update quiz: ${e.message}")
            }
        }
    }
}


