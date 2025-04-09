package com.example.bookieboard.service

import com.example.bookieboard.model.User

data class UserUiState(
    val email: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val authMode: AuthMode = AuthMode.SIGNED_OUT,
    val userData: User? = User(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun isEntryValid(): Boolean {
        return email?.isNotEmpty() == true && password?.isNotEmpty() == true
    }
}

data class UserCreationState(
    val email: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val signUpMode: SignUpMode = SignUpMode.INACTIVE,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun isEntryValid(): Boolean {
        return email?.isNotEmpty() == true &&
                password?.isNotEmpty() == true &&
                firstName?.isNotEmpty() == true &&
                lastName?.isNotEmpty() == true
    }
}

enum class AuthMode{
    SIGNED_IN,
    SIGNED_OUT
}

enum class SignUpMode{
    ACTIVE,
    INACTIVE
}