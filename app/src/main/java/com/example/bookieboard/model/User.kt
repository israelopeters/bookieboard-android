package com.example.bookieboard.model

data class User(
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val bookieScore: Int? = null,
    val bookieRank: UserRank? = null,
    val roles: List<Role>? = null
)