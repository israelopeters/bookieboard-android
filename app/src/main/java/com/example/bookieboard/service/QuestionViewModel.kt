package com.example.bookieboard.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
    private var _selectedDifficultyLevel: DifficultyLevel by mutableStateOf(DifficultyLevel.EASY)
    private var _questionsList: List<Question> = mutableStateListOf(
        Question(
            0,
            "This is a test question",
            listOf("Option 1", "Option 2", "Option 3", "Option 4"),
            2,
            DifficultyLevel.EASY
        )
    )
    private var _currentQuestionIndex by mutableIntStateOf(0)

    private var _currentPlayScore = 0

    fun getCurrentQuestion(): Question = _questionsList[_currentQuestionIndex]

    fun getQuestionCount() = _questionsList.size

    fun getCurrentQuestionIndex() = _currentQuestionIndex

    fun getQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            _questionsList = apiRepository.getQuestions(_selectedDifficultyLevel)
        }
    }

    fun getCurrentPlayScore() = _currentPlayScore

    fun updateCurrentQuestion() {
        if (_questionsList.size - 1 > _currentQuestionIndex) {
            _currentQuestionIndex++
        }
    }

    fun setDifficultyLevel(difficultyLevel: DifficultyLevel) {
        _selectedDifficultyLevel = difficultyLevel
    }

    fun getSelectedDifficultyLevel() = _selectedDifficultyLevel

    fun updateCurrentPlayScore() {
        _currentPlayScore++
    }

    fun resetCurrentQuestionIndex() {
        _currentQuestionIndex = 0
    }

    fun resetCurrentPlayScore() {
        _currentPlayScore = 0
    }

    fun isLastQuestion(): Boolean = _questionsList.size == _currentQuestionIndex + 1
}