package com.example.bookieboard.service

sealed class UserUiState<out T> {
    data object Loading : UserUiState<Nothing>()
    data class Success<T>(val data: T) : UserUiState<T>()
    data class Error(val message: String) : UserUiState<Nothing>()
}