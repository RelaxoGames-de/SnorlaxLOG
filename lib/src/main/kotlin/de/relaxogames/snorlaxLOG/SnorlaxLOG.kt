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
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class SnorlaxLOGConfig(val url: String, val username: String, val password: String)

@Serializable
data class RGDBUser(val name: String, val password: String, val role: RGDBRole)

@Serializable
enum class RGDBRole(val value: String) {
    ADMIN("admin"),
    CREATOR("creator"),
    USER("user")
}

@Serializable
data class RGDBStorage(val name: String)

@Serializable
data class RGDBStorageObject(val key: String, val value: String, val isPrivate: Boolean = false)

class UnauthorizedError(message: String = "Unauthorized") : Exception(message)

class SnorlaxLOG(
    private val config: SnorlaxLOGConfig,
    private val logLevel: LogLevel = LogLevel.NONE
) {
    private val client =
            HttpClient(CIO) {
                install(ContentNegotiation) { json() }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = logLevel
                    sanitizeHeader { header -> header == HttpHeaders.Authorization }
                }
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(config.username, config.password)
                        }
                        sendWithoutRequest { true }
                    }
                }
            }

    suspend fun testConnection(): Boolean {
        val url = config.url + "/ping"
        val response = client.get(url).body<String>()
        return response == "pong"
    }

    fun syncTestConnection(): Boolean {
        return runBlocking {
            testConnection()
        }
    }

    suspend fun getSelf(): RGDBUser {
        val url = config.url + "/user/self"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        return response.body<RGDBUser>()
    }

    fun syncGetSelf(): RGDBUser {
        return runBlocking {
            getSelf()
        }
    }

    suspend fun changePassword(newPassword: String) {
        val url = config.url + "/user/self/password"
        val response = client.put(url) { setBody(newPassword) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.NoContent) throw Exception("Failed to change password")
    }

    fun syncChangePassword(newPassword: String) {
        return runBlocking {
            changePassword(newPassword)
        }
    }

    suspend fun getUsers(): List<RGDBUser> {
        val url = config.url + "/admin/users"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        return response.body<List<RGDBUser>>()
    }

    fun syncGetUsers(): List<RGDBUser> {
        return runBlocking {
            getUsers()
        }
    }

    // Admin only
    suspend fun createUser(user: RGDBUser) {
        val url = config.url + "/admin/users"
        val response = client.post(url) { setBody(user) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to create user")
    }

    fun syncCreateUser(user: RGDBUser) {
        return runBlocking {
            createUser(user)
        }
    }

    suspend fun deleteUser(name: String) {
        val url = config.url + "/admin/users/$name"
        val response = client.delete(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.NoContent) throw Exception("Failed to delete user")
    }

    fun syncDeleteUser(name: String) {
        return runBlocking {
            deleteUser(name)
        }
    }

    suspend fun updateUserRole(name: String, role: RGDBRole) {
        val url = config.url + "/admin/users/$name/role"
        val response = client.put(url) { setBody(role) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to update user role")
    }

    fun syncUpdateUserRole(name: String, role: RGDBRole) {
        return runBlocking {
            updateUserRole(name, role)
        }
    }

    suspend fun updateUserPassword(name: String, newPassword: String) {
        val url = config.url + "/admin/users/$name/password"
        val response = client.put(url) { setBody(newPassword) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to update user password")
    }

    fun syncUpdateUserPassword(name: String, newPassword: String) {
        return runBlocking {
            updateUserPassword(name, newPassword)
        }
    }

    suspend fun getUser(name: String): RGDBUser {
        val url = config.url + "/admin/users/$name"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get user")
        return response.body<RGDBUser>()
    }

    fun syncGetUser(name: String): RGDBUser {
        return runBlocking {
            getUser(name)
        }
    }

    // Creator only
    suspend fun createStorage(name: String) {
        val url = config.url + "/creator/storages"
        val storage = RGDBStorage(name)
        val response = client.post(url) { setBody(storage) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to create storage")
    }

    fun syncCreateStorage(name: String) {
        return runBlocking {
            createStorage(name)
        }
    }

    // General Storage Paths
    suspend fun getStorage(name: String): RGDBStorage {
        val url = config.url + "/storage/$name"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get storage")
        return response.body<RGDBStorage>()
    }

    fun syncGetStorage(name: String): RGDBStorage {
        return runBlocking {
            getStorage(name)
        }
    }

    suspend fun getSharedTable(dbName: String): List<RGDBStorageObject> {
        val url = config.url + "/storage/shared/$dbName"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get shared table")
        return response.body<List<RGDBStorageObject>>()
    }

    fun syncGetSharedTable(dbName: String): List<RGDBStorageObject> {
        return runBlocking {
            getSharedTable(dbName)
        }
    }

    suspend fun getSharedEntry(dbName: String, key: String): String {
        val url = config.url + "/storage/shared/$dbName/$key"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get shared entry")
        return response.body<String>()
    }

    fun syncGetSharedEntry(dbName: String, key: String): String {
        return runBlocking {
            getSharedEntry(dbName, key)
        }
    }

    suspend fun setSharedEntry(dbName: String, key: String, value: String) {
        val url = config.url + "/storage/shared/$dbName/$key"
        val response = client.post(url) { setBody(value) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to set shared entry")
    }

    fun syncSetSharedEntry(dbName: String, key: String, value: String) {
        return runBlocking {
            setSharedEntry(dbName, key, value)
        }
    }

    suspend fun getPrivateTable(dbName: String): List<RGDBStorageObject> {
        val url = config.url + "/storage/private/$dbName"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get private table")
        return response.body<List<RGDBStorageObject>>()
    }

    fun syncGetPrivateTable(dbName: String): List<RGDBStorageObject> {
        return runBlocking {
            getPrivateTable(dbName)
        }
    }

    suspend fun getPrivateEntry(dbName: String, key: String): String {
        val url = config.url + "/storage/private/$dbName/$key"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get private entry")
        return response.body<String>()
    }

    fun syncGetPrivateEntry(dbName: String, key: String): String {
        return runBlocking {
            getPrivateEntry(dbName, key)
        }
    }

    suspend fun setPrivateEntry(dbName: String, key: String, value: String) {
        val url = config.url + "/storage/private/$dbName/$key"
        val response = client.post(url) { setBody(value) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to set private entry")
    }

    fun syncSetPrivateEntry(dbName: String, key: String, value: String) {
        return runBlocking {
            setPrivateEntry(dbName, key, value)
        }
    }

    fun functionToJavaAsync(function: suspend () -> Unit): () -> Unit {
        return {
            runBlocking {
                function()
            }
        }
    }
}
