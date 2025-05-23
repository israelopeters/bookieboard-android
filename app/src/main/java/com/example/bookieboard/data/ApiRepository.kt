package com.example.bookieboard.data

import android.util.Log
import com.example.bookieboard.model.DifficultyLevel
import com.example.bookieboard.model.Question
import com.example.bookieboard.model.User
import com.example.bookieboard.model.UserCreation
import com.example.bookieboard.service.AuthMode
import com.example.bookieboard.service.SignUpMode
import com.example.bookieboard.service.UserCreationState
import com.example.bookieboard.service.UserUiState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject

private const val TAG: String = "BookieBoardActivity"

class ApiRepository @Inject constructor(private val client: HttpClient) {
    private var userCredentials: List<String> = listOf("", "")

    suspend fun addNewUser(user: UserCreation): UserCreationState {
        try {
            val response: User = client.post("/api/v1/users/add") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }.processBody()
            Log.v(TAG, "API repository - raw response --- $response")
            Log.v(TAG, "Api Repository - mapped response --- ${mapToUserCreationState(response)}")
            return mapToUserCreationState(response)
        } catch (e: Exception) {
            return UserCreationState(error = e.message)
        }
    }

    suspend fun getUser(credentials: List<String>): UserUiState {
        val response = client.get(
            "/api/v1/users/email?email=${credentials[0]}"
        ) {
            basicAuth(
                username = credentials[0],
                password = credentials[1]
            )
        }
        var currentUser = UserUiState()
        if (response.status == HttpStatusCode.OK) {
            userCredentials = credentials
            currentUser = mapToUserUiState(response.processBody())
            currentUser = currentUser.copy(authMode = AuthMode.SIGNED_IN)
        }
        return currentUser
    }

    suspend fun getQuestions(difficultyLevel: DifficultyLevel): List<Question> {
        val response = client.get(
            "/api/v1/questions/difficulty?difficultyLevel=${difficultyLevel}"
        ) {
            basicAuth(
                username = userCredentials[0],
                password = userCredentials[1]
            )
        }
        return response.processBody()
    }

    suspend fun updateUserScore(newScore: Int): UserUiState {
        val updates: HashMap<String, Any> = HashMap()
        updates["email"] = userCredentials[0]
        updates["bookieScore"] = newScore

        Log.v("BookieBoardActivity", "Updates object: $updates")
        val response = client.patch("/api/v1/users/update") {
            contentType(ContentType.Application.Json)
            setBody(updates)
            basicAuth(
                username = userCredentials[0],
                password = userCredentials[1]
            )
        }
        Log.v("BookieBoardActivity", "Server response: ${response.body<User>()}")
        return mapToUserUiState(response.processBody())
    }

    // An extension function to handle the response body and exceptions when getting a user
    suspend inline fun <reified T> HttpResponse.processBody(): T {
        if (
            this.status == HttpStatusCode.OK ||
            this.status == HttpStatusCode.Accepted ||
            this.status == HttpStatusCode.Created) {
            // Accepted status code caters to updateUserScore server response success code
            return body<T>()
        } else if (this.status.value == 404) {
            throw Exception("User not found!")
        } else if (this.status.value == 401) {
            throw Exception("Unauthorized access!")
        } else {
            throw Exception("Something went wrong")
        }
    }

    private fun mapToUserUiState(user: User): UserUiState {
        val userUiState: UserUiState = UserUiState(
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            bookieRank = user.bookieRank,
            bookieScore = user.bookieScore
        )
        return userUiState
    }

    private fun mapToUserCreationState(user: User): UserCreationState {
        val userCreationState: UserCreationState = UserCreationState(
            email = user.email,
            signUpMode = SignUpMode.ACTIVE,
            isLoading = false,
            error = null
        )
        return userCreationState
    }

}