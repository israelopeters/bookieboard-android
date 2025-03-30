package com.example.bookieboard.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.model.User
import com.example.bookieboard.model.UserCreation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    var userEmail by mutableStateOf("")
    var userPassword by mutableStateOf("")


    private val _userUiState = MutableStateFlow<UserUiState<User?>>(UserUiState.Loading)
    val userUiState: StateFlow<UserUiState<User?>> = _userUiState

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
            try {
                val response = apiRepository.getUser(
                    listOf(userEmail, userPassword)
                )
                _userUiState.value = UserUiState.Success(response)
            } catch (e: Exception) {
                _userUiState.value = UserUiState.Error(e.message ?: "Network request error!")
            }
        }
    }

    fun updateBookieBoardScore(newScore: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiRepository.updateUserScore(newScore)
                _userUiState.value = UserUiState.Success(response)
            } catch (e: Exception) {
                _userUiState.value = UserUiState.Error(e.message ?: "Network request error!")
            }
        }
    }

    fun addNewUser(newUser: UserCreation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiRepository.addNewUser(newUser)
                _userUiState.value = UserUiState.Success(response)
            } catch (e: Exception) {
                _userUiState.value = UserUiState.Error(e.message ?: "Network request error!")
            }
        }
    }

//    suspend fun <T> safelyCall(execute: suspend () -> T): Result<T> = try {
//        Result.success(execute())
//    } catch (e: Exception) {
//        Result.failure(Throwable(message = e.message ?: "Network request error!"))
//    }
}