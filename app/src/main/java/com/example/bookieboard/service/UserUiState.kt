package com.example.bookieboard.service

import com.example.bookieboard.model.User
import com.example.bookieboard.model.UserRank

data class UserUiState(
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val authMode: AuthMode = AuthMode.SIGNED_OUT,
    val bookieScore: Int? = null,
    val bookieRank: UserRank? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

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
    SIGNING_IN,
    SIGNED_OUT
}

enum class SignUpMode{
    ACTIVE,
    INACTIVE
}