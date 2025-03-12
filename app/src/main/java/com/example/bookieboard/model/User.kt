package com.example.bookieboard.model

data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val bookieScore: Int,
    val bookieRank: UserRank,
    val roles: List<Role>
)