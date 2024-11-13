package de.relaxogames.snorlaxlog

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.URI
import java.util.logging.Logger

@Serializable
data class PingResponse(val status: String, val message: String)

@Serializable
data class TokenResponse(val access_token: String, val token_type: String)

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
        job = scope.launch {
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

    fun ping(): Boolean {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$url/ping"))
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build()
        val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
        val pingResponse = Json.decodeFromString<PingResponse>(response.body())
        return pingResponse.status == "ok"
    }

    private fun token(): String {
        val formData = "username=$username&password=$password"
        
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$url/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .method("POST", HttpRequest.BodyPublishers.ofString(formData))
            .build()
            
        val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
        val tokenResponse = Json.decodeFromString<TokenResponse>(response.body())
        return tokenResponse.access_token
    }
}
