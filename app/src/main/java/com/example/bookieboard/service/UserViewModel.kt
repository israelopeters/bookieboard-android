package com.example.bookieboard.service

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.model.UserCreation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG: String = "BookieBoardActivity"

@HiltViewModel
class UserViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    var userEmail by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var currentUser by mutableStateOf(UserUiState())
    var addedUser by mutableStateOf(UserCreationState())

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
            currentUser = currentUser.copy(authMode = AuthMode.SIGNING_IN)
            try {
                safelyCall {
                    currentUser = apiRepository.getUser(
                        listOf(userEmail, userPassword)
                    )
                }
                Log.v(TAG, "ViewModel - After logging in: $currentUser")
            } catch (e: Exception) {
                currentUser = currentUser.copy(
                    authMode = AuthMode.SIGNED_OUT,
                    error = e.message
                )
                Log.v(TAG, "ViewModel - Error after logging in: $currentUser")
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

    fun addNewUser(newUser: UserCreation): UserCreationState {
        viewModelScope.launch(Dispatchers.IO) {
            addedUser = addedUser.copy(signUpMode = SignUpMode.PROGRESS)
            try {
                safelyCall {
                    addedUser = apiRepository.addNewUser(newUser)
                }
            } catch (e: Exception) {
                addedUser = addedUser.copy(error = e.message)
            }
        }
        return addedUser
    }

    fun clearAddedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            addedUser = UserCreationState()
        }
    }

    suspend fun <T> safelyCall(execute: suspend () -> T): Result<T> = try {
        Result.success(execute())
    } catch (e: Exception) {
        Result.failure(Throwable(message = e.message ?: "Network request error!"))
    }
}