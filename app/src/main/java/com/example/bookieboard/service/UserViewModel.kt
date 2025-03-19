package com.example.bookieboard.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookieboard.data.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    var userEmail by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var currentUser by mutableStateOf(UserUiState())

    fun updateEmail(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userEmail = input
        }
    }

    fun updatePassword(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPassword = input
        }
    }

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            safelyCall {
                currentUser = apiRepository.getUser(
                    listOf(userEmail, userPassword)
                )
            }
        }
    }

    fun updateBookieBoardScore(newScore: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            safelyCall {
                currentUser = apiRepository.updateUserScore(newScore)
            }
        }
    }

    suspend fun <T> safelyCall(execute: suspend () -> T): Result<T> = try {
        Result.success(execute())
    } catch (e: Exception) {
        Result.failure(Throwable(message = e.message ?: "Network request error!"))
    }
}