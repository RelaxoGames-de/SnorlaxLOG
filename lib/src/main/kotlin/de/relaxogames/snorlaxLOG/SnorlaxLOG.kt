package de.relaxogames.snorlaxLOG

import SnorlaxLOGConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.get

class SnorlaxLOG(private val config: SnorlaxLOGConfig) {
    private val client =
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json()
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.NONE
                    sanitizeHeader { header -> header == HttpHeaders.Authorization }
                }
            }

    suspend fun testConnection(): Boolean {
        val url = config.getUrl() + "/ping"
        val response = client.get(url).body<String>()
        return response == "pong"
    }
}
