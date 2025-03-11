package com.example.bookieboard.model

data class Question(
    val id: Long,
    val detail: String,
    val options: List<String>,
    val difficultyLevel: DifficultyLevel
)
