package com.example.music_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_app.ui.quiz.Question
import com.example.music_app.ui.quiz.Quiz
import kotlinx.coroutines.launch

sealed class QuizState {
    data object Idle : QuizState()
    data object Loading : QuizState()
    data class Success(val result: List<Quiz>) : QuizState()
    data class Error(val message: String) : QuizState()
}

class QuizViewModel : ViewModel() {
    private val _quizState = MutableLiveData<QuizState>(QuizState.Idle)
    val quizState: MutableLiveData<QuizState> = this._quizState

    private val quizzes = mutableListOf<Quiz>()

    fun getQuizzes() {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
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
                quizzes.add(quiz)
                _quizState.value = QuizState.Success(quizzes)
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to add quiz: ${e.message}")
            }
        }
    }

    fun removeQuiz(quiz: Quiz) {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
                quizzes.remove(quiz)
                _quizState.value = QuizState.Success(quizzes)
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to remove quiz: ${e.message}")
            }
        }
    }

    fun updateQuiz(quiz: Quiz) {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
                val index = quizzes.indexOfFirst { it.id == quiz.id }
                if (index != -1) {
                    quizzes[index] = quiz
                    _quizState.value = QuizState.Success(quizzes)
                } else {
                    _quizState.value = QuizState.Error("Quiz not found")
                }
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to update quiz: ${e.message}")
            }
        }
    }

    fun addQuestionToQuiz(question: Question, quizId: Int) {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
                val index = quizzes.indexOfFirst { it.id == quizId }
                if (index != -1) {
                    val quiz = quizzes[index]
                    quiz.questions.add(question)
                    _quizState.value = QuizState.Success(quizzes)
                } else {
                    _quizState.value = QuizState.Error("Quiz not found")
                }
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to add question: ${e.message}")
            }
        }
    }

    fun removeQuestionFromQuiz(question: Question, quizId: Int) {
        _quizState.value = QuizState.Loading
        viewModelScope.launch {
            try {
                val index = quizzes.indexOfFirst { it.id == quizId }
                if (index != -1) {
                    val quiz = quizzes[index]
                    quiz.questions.remove(question)
                    _quizState.value = QuizState.Success(quizzes)
                } else {
                    _quizState.value = QuizState.Error("Quiz not found")
                }
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to remove question: ${e.message}")
            }
        }
    }
}
