package com.example.bookieboard.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.model.DifficultyLevel
import com.example.bookieboard.model.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {
    private var _questionsList: List<Question> = mutableListOf(
        Question(
            0,
            "This is a test question",
            listOf("Option 1", "Option 2", "Option 3", "Option 4"),
            DifficultyLevel.EASY
        )
    )
    private var _currentQuestionIndex by mutableIntStateOf(0)
    private var _currentQuestion by mutableStateOf(
        _questionsList[_currentQuestionIndex]
    )

    fun getCurrentQuestion(): Question = _currentQuestion

    fun getQuestions(difficultyLevel: DifficultyLevel) {
        viewModelScope.launch(Dispatchers.IO) {
            _questionsList = apiRepository.getQuestions(difficultyLevel)
        }
    }

    fun updateCurrentQuestion() {
        if (_questionsList.size > _currentQuestionIndex) {
            _currentQuestionIndex++
        }
    }

    fun isLastQuestion(): Boolean = _questionsList.size == _currentQuestionIndex + 1
}