package com.example.bookieboard.service

import com.example.bookieboard.model.UserRank

data class UserUiState(
    var isLoggedIn: Boolean =  false,
    var firstName: String = "",
    var lastName: String = "",
    var bookieScore: Int = 0,
    var bookieRank: UserRank = UserRank.ROOKIE,
)
