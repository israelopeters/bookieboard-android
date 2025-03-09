package com.example.bookieboard.data

import com.example.bookieboard.model.User
import com.example.bookieboard.model.UserCreation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class ApiRepository @Inject constructor(private val client: HttpClient) {
    val BASE_URL = "http://bookieboardapi-env.eba-2imwjb2j.eu-west-2.elasticbeanstalk.com"
    var userCredentials: List<String> = listOf()

    suspend fun addNewUser(user: UserCreation): User = client
        .post("${BASE_URL}/api/v1/users/add").body()

    suspend fun getUser(credentials: List<String>): User {
        val response = client.get("${BASE_URL}/api/v1/users/email?email=${credentials[0]}") {
            basicAuth(
                username = credentials[0],
                password = credentials[1]
            )
        }
        userCredentials = credentials
        return response.processBody()

    }

    // An extension function to handle the response body and exceptions when getting a user
    suspend inline fun <reified T> HttpResponse.processBody(): T {
        if (this.status == HttpStatusCode.OK) {
            return body<T>()
        } else if (this.status.value == 404) {
            throw Exception("User not found!")
        } else if (this.status.value == 401) {
            throw Exception("Unauthorized access!")
        } else {
            throw Exception("Something went wrong")
        }
    }

}