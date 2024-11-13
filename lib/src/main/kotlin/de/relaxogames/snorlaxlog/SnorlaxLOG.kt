package de.relaxogames.snorlaxlog

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.util.logging.Logger
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable data class PingResponse(val status: String, val message: String)

@Serializable data class TokenResponse(val access_token: String, val token_type: String)

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
    private val client = HttpClient(CIO)

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

    private fun token(): String {
        return ""
    }

    fun getToken(): String? {
        return token
    }
}
