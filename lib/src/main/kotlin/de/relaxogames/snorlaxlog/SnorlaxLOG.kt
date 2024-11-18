package de.relaxogames.snorlaxlog

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.logging.Logger
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

@Serializable
data class PingResponse(val status: String, val message: String)

@Serializable
data class TokenResponse(val access_token: String, val token_type: String)

@Serializable
data class GetUserResponse(val id: Int, val username: String, val role: String)

enum class APIRole() {
    ADMIN,
    CREATOR,
    USER,
    GUEST;

    fun getName(role: APIRole): String {
        return when (role) {
            ADMIN -> "admin"
            CREATOR -> "creator"
            USER -> "user"
            GUEST -> "guest"
        }
    }

    fun getRole(roleName: String): APIRole? {
        return when (roleName) {
            "admin" -> return ADMIN
            "creator" -> return CREATOR
            "user" -> return USER
            "guest" -> return GUEST
            else -> return null
        }
    }
}

class SnorlaxLOG {
    private val username: String
    private val password: String
    private val url: String
    private var token: String? = null
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val interval = 1000 * 60 * 5 // 5 minutes
    private var refreshing = true
    private val logger = Logger.getLogger(SnorlaxLOG::class.java.name)
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    constructor(username: String, password: String, url: String) {
        this.username = username
        this.password = password
        this.url = url
    }

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
        this.url = "https://rgdb.relaxogames.de"
    }

    init {
        startUpdating()
    }

    private fun startUpdating() {
        job =
            scope.launch {
                while (refreshing) {
                    token = token()
                    logger.info("Token refreshed")
                    delay(interval.toLong())
                }
            }
    }

    fun stopUpdating() {
        refreshing = false
        job?.cancel()
    }

    suspend fun ping(): Boolean {
        val response = Json.decodeFromString<PingResponse>(client.get("$url/ping").bodyAsText())
        return response.status == "ok"
    }

    suspend fun getUsers(): List<GetUserResponse> {
        return Json.decodeFromString<List<GetUserResponse>>(client.get("$url/users") {
            header("Authorization", "Bearer $token")
        }.bodyAsText())
    }

    suspend fun getUser(id: Int): GetUserResponse? {
        val users = getUsers()
        for (user in users)
            if (user.id == id) return user
        return null
    }

    suspend fun getUser(username: String): GetUserResponse? {
        val users = getUsers()
        for (user in users)
            if (user.username == username) return user
        return null
    }

    suspend fun getUserId(username: String): Int? {
        val user = getUser(username)
        return user?.id
    }

    suspend fun addUser(username: String, password: String, role: APIRole) {
        
    }

    private suspend fun token(): String {
        val response = client.post("$url/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("username", username)
                append("password", password)
            }))
        }
        return Json.decodeFromString<TokenResponse>(response.bodyAsText()).access_token
    }

    fun getToken(): String? {
        return token
    }
}