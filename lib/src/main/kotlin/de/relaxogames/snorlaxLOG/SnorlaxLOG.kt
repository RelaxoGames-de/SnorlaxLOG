package de.relaxogames.snorlaxLOG

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody

data class SnorlaxLOGConfig(val url: String, val username: String, val password: String)
data class User(val name: String, val password: String, val role: RGDBRole)
enum class RGDBRole(val value: String) {
    ADMIN("admin"),
    CREATOR("creator"),
    USER("user")
}

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
                install(Auth) {
                    basic {
                        credentials { 
                            BasicAuthCredentials(config.username, config.password)
                        }
                        realm = "RGDB Realm"
                    }
                }
            }

    suspend fun testConnection(): Boolean {
        val url = config.url + "/ping"
        val response = client.get(url).body<String>()
        return response == "pong"
    }

    private suspend fun getSelf(): User {
        val url = config.url + "/user/self"
        val response = client.get(url).body<User>()
        return response
    }

    suspend fun changePassword(newPassword: String): Boolean {
        val url = config.url + "/user/self/password"
        val response = client.put(url) {
            setBody(newPassword)
        }
        return response.status == HttpStatusCode.NoContent
    }
}