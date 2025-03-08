package com.example.bookieboard.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookieboard.data.ApiRepository
import com.example.bookieboard.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    var userEmail: String by mutableStateOf("")
    var userPassword: String by mutableStateOf("")
    var authenticatedUser by mutableStateOf(
        User(null, null, null, null, null)
    )

    fun updateEmail(input: String) {
        userEmail = input
    }

    fun updatePassword(input: String) {
        userPassword = input
    }

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            safelyCall {
                authenticatedUser = apiRepository.getUser(
                    listOf(userEmail, userPassword)
                )
            }
        }
    }

    suspend fun <T> safelyCall(execute: suspend () -> T): Result<T> = try {
        Result.success(execute())
    } catch (e: Exception) {
        Result.failure(Throwable(message = e.message ?: "Network request error!"))
    }
}