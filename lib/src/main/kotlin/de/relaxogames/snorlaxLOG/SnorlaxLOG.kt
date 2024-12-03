package de.relaxogames.snorlaxLOG

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.post
import io.ktor.client.request.delete
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

data class SnorlaxLOGConfig(val url: String, val username: String, val password: String)
data class RGDBUser(val name: String, val password: String, val role: RGDBRole)
enum class RGDBRole(val value: String) {
    ADMIN("admin"),
    CREATOR("creator"),
    USER("user")
}
class UnauthorizedError(message: String = "Unauthorized") : Exception(message)

class SnorlaxLOG(private val config: SnorlaxLOGConfig) {
    private val client =
            HttpClient(CIO) {
                install(ContentNegotiation) { json() }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.NONE
                    sanitizeHeader { header -> header == HttpHeaders.Authorization }
                }
                install(Auth) {
                    basic {
                        credentials { BasicAuthCredentials(config.username, config.password) }
                        realm = "RGDB Realm"
                    }
                }
            }

    suspend fun testConnection(): Boolean {
        val url = config.url + "/ping"
        val response = client.get(url).body<String>()
        return response == "pong"
    }

    private suspend fun getSelf(): RGDBUser {
        val url = config.url + "/user/self"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        return response.body<RGDBUser>()
    }

    suspend fun changePassword(newPassword: String) {
        val url = config.url + "/user/self/password"
        val response = client.put(url) { setBody(newPassword) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.NoContent) throw Exception("Failed to change password")
    }

    suspend fun getUsers(): List<RGDBUser> {
        val url = config.url + "/admin/users"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        return response.body<List<RGDBUser>>()
    }

    suspend fun createUser(user: RGDBUser) {
        val url = config.url + "/admin/users"
        val response = client.post(url) { setBody(user) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to create user")
    }

    suspend fun deleteUser(name: String) {
        val url = config.url + "/admin/users/$name"
        val response = client.delete(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.NoContent) throw Exception("Failed to delete user")
    }

    suspend fun updateUserRole(name: String, role: RGDBRole) {
        val url = config.url + "/admin/users/$name/role"
        val response = client.put(url) { setBody(role) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to update user role")
    }

    suspend fun updateUserPassword(name: String, newPassword: String) {
        val url = config.url + "/admin/users/$name/password"
        val response = client.put(url) { setBody(newPassword) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to update user password")
    }

    suspend fun getUser(name: String): RGDBUser {
        val url = config.url + "/admin/users/$name"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get user")
        return response.body<RGDBUser>()
    }
}
