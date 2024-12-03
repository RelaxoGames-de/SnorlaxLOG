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
import java.util.concurrent.CompletableFuture

/**
 * Configuration for the SnorlaxLOG client
 * 
 * @param url The URL of the RGDB Backend
 * @param username The username of the user
 * @param password The password of the user
 * 
 * @see SnorlaxLOG
 * @since 1.0
 * 
 * @author Johannes (Jotrorox) Müller
 * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
 */
@Serializable
data class SnorlaxLOGConfig(val url: String, val username: String, val password: String)

/**
 * User object for the RGDB Backend
 * 
 * @param name The name of the user
 * @param password The password of the user
 * @param role The role of the user
 * 
 * @see RGDBRole
 * @since 1.0
 * 
 * @author Johannes (Jotrorox) Müller
 * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
 */
@Serializable
data class RGDBUser(val name: String, val password: String, val role: RGDBRole)

/**
 * Role object for the RGDB Backend
 * 
 * @param value The value of the role
 * 
 * @see RGDBUser
 * @since 1.0
 * 
 * @author Johannes (Jotrorox) Müller
 * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
 */
@Serializable
enum class RGDBRole(val value: String) {
    ADMIN("admin"),
    CREATOR("creator"),
    USER("user")
}

/**
 * Storage object for the RGDB Backend (Creator only)
 * 
 * @param name The name of the storage
 * 
 * @see RGDBStorageObject
 * @since 1.0
 * 
 * @author Johannes (Jotrorox) Müller
 * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
 */
@Serializable
data class RGDBStorage(val name: String)

/**
 * Storage object for the RGDB Backend (Shared and Private)
 * 
 * @param key The key of the storage object
 * @param value The value of the storage object
 * @param isPrivate Whether the storage object is private
 * 
 * @see RGDBStorage
 * @since 1.0
 * 
 * @author Johannes (Jotrorox) Müller
 * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
 */
@Serializable
data class RGDBStorageObject(val key: String, val value: String, val isPrivate: Boolean = false)

/**
 * Unauthorized error for the RGDB Backend (401)
 * 
 * @param message The message of the error
 * 
 * @since 1.0
 * 
 * @author Johannes (Jotrorox) Müller
 * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
 */
class UnauthorizedError(message: String = "Unauthorized") : Exception(message)

/**
 * SnorlaxLOG client
 * 
 * @param config The configuration for the client
 * @param logLevel The log level for the client
 * 
 * @see SnorlaxLOGConfig
 * @since 1.0
 * 
 * @author Johannes (Jotrorox) Müller
 * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
 */
class SnorlaxLOG(
    /**
     * The configuration for the client
     * 
     * @see SnorlaxLOGConfig
     */
    private val config: SnorlaxLOGConfig,
    
    /**
     * The log level for the client
     * 
     * @see LogLevel
     */
    private val logLevel: LogLevel = LogLevel.NONE
) {
    /**
     * The HTTP client for the SnorlaxLOG client
     * 
     * @see HttpClient
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
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

    /**
     * Tests the connection to the RGDB Backend
     * 
     * @return Whether the connection was successful
     * @throws UnauthorizedError If the connection was not successful
     * 
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun testConnection(): Boolean {
        val url = config.url + "/ping"
        val response = client.get(url).body<String>()
        return response == "pong"
    }

    /**
     * Tests the connection to the RGDB Backend synchronously
     * 
     * @return Whether the connection was successful
     * @throws UnauthorizedError If the connection was not successful
     * 
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncTestConnection(): Boolean {
        return runBlocking {
            testConnection()
        }
    }

    /**
     * Gets the user the snorlaxLOG client is authenticated as (User only)
     * 
     * @return The self user
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun getSelf(): RGDBUser {
        val url = config.url + "/user/self"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        return response.body<RGDBUser>()
    }

    /**
     * Gets the user the snorlaxLOG client is authenticated as synchronously (User only)
     * 
     * @return The self user
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncGetSelf(): RGDBUser {
        return runBlocking {
            getSelf()
        }
    }

    /**
     * Changes the password of the user the snorlaxLOG client is authenticated as (User only)
     * 
     * @param newPassword The new password
     * @throws UnauthorizedError If the user was not found
     * 
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun changePassword(newPassword: String) {
        val url = config.url + "/user/self/password"
        val response = client.put(url) { setBody(newPassword) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.NoContent) throw Exception("Failed to change password")
    }

    /**
     * Changes the password of the user the snorlaxLOG client is authenticated as synchronously (User only)
     * 
     * @param newPassword The new password
     * @throws UnauthorizedError If the user was not found
     * 
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncChangePassword(newPassword: String) {
        return runBlocking {
            changePassword(newPassword)
        }
    }

    /**
     * Gets all users (Admin only)
     * 
     * @return A list of all users
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun getUsers(): List<RGDBUser> {
        val url = config.url + "/admin/users"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        return response.body<List<RGDBUser>>()
    }

    /**
     * Gets all users synchronously (Admin only)
     * 
     * @return A list of all users
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncGetUsers(): List<RGDBUser> {
        return runBlocking {
            getUsers()
        }
    }

    /**
     * Creates a user (Admin only)
     * 
     * @param user The user to create
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun createUser(user: RGDBUser) {
        val url = config.url + "/admin/users"
        val response = client.post(url) { setBody(user) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to create user")
    }

    /**
     * Creates a user synchronously (Admin only)
     * 
     * @param user The user to create
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncCreateUser(user: RGDBUser) {
        return runBlocking {
            createUser(user)
        }
    }

    /**
     * Deletes a user (Admin only)
     * 
     * @param name The name of the user to delete
     * @throws UnauthorizedError If the user was not found
     * 
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun deleteUser(name: String) {
        val url = config.url + "/admin/users/$name"
        val response = client.delete(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.NoContent) throw Exception("Failed to delete user")
    }

    /**
     * Deletes a user synchronously (Admin only)
     * 
     * @param name The name of the user to delete
     * @throws UnauthorizedError If the user was not found
     * 
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncDeleteUser(name: String) {
        return runBlocking {
            deleteUser(name)
        }
    }

    /**
     * Updates the role of a user (Admin only)
     * 
     * @param name The name of the user to update
     * @param role The new role
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBRole
     * @see RGDBUser
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun updateUserRole(name: String, role: RGDBRole) {
        val url = config.url + "/admin/users/$name/role"
        val response = client.put(url) { setBody(role) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to update user role")
    }

    /**
     * Updates the role of a user synchronously (Admin only)
     * 
     * @param name The name of the user to update
     * @param role The new role
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBRole
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncUpdateUserRole(name: String, role: RGDBRole) {
        return runBlocking {
            updateUserRole(name, role)
        }
    }

    /**
     * Updates the password of a user (Admin only)
     * 
     * @param name The name of the user to update
     * @param newPassword The new password
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun updateUserPassword(name: String, newPassword: String) {
        val url = config.url + "/admin/users/$name/password"
        val response = client.put(url) { setBody(newPassword) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to update user password")
    }

    /**
     * Updates the password of a user synchronously (Admin only)
     * 
     * @param name The name of the user to update
     * @param newPassword The new password
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncUpdateUserPassword(name: String, newPassword: String) {
        return runBlocking {
            updateUserPassword(name, newPassword)
        }
    }

    /**
     * Gets a user (Admin only)
     * 
     * @param name The name of the user to get
     * @return The user
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun getUser(name: String): RGDBUser {
        val url = config.url + "/admin/users/$name"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get user")
        return response.body<RGDBUser>()
    }

    /**
     * Gets a user synchronously (Admin only)
     * 
     * @param name The name of the user to get
     * @return The user
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBUser
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncGetUser(name: String): RGDBUser {
        return runBlocking {
            getUser(name)
        }
    }

    // Creator only
    /**
     * Creates a storage (Creator only)
     * 
     * @param name The name of the storage to create
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorage
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun createStorage(name: String) {
        val url = config.url + "/creator/storages"
        val storage = RGDBStorage(name)
        val response = client.post(url) { setBody(storage) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to create storage")
    }

    /**
     * Creates a storage synchronously (Creator only)
     * 
     * @param name The name of the storage to create
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorage
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncCreateStorage(name: String) {
        return runBlocking {
            createStorage(name)
        }
    }

    /**
     * Gets a storage (User only)
     * 
     * @param name The name of the storage to get
     * @return The storage
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorage
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun getStorage(name: String): RGDBStorage {
        val url = config.url + "/storage/$name"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get storage")
        return response.body<RGDBStorage>()
    }

    /**
     * Gets a storage synchronously (User only)
     * 
     * @param name The name of the storage to get
     * @return The storage
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorage
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncGetStorage(name: String): RGDBStorage {
        return runBlocking {
            getStorage(name)
        }
    }

    /**
     * Gets a shared table (User only)
     * 
     * @param dbName The name of the storage to get
     * @return The shared table
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun getSharedTable(dbName: String): List<RGDBStorageObject> {
        val url = config.url + "/storage/shared/$dbName"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get shared table")
        return response.body<List<RGDBStorageObject>>()
    }

    /**
     * Gets a shared table synchronously (User only)
     * 
     * @param dbName The name of the storage to get
     * @return The shared table
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncGetSharedTable(dbName: String): List<RGDBStorageObject> {
        return runBlocking {
            getSharedTable(dbName)
        }
    }

    /**
     * Gets a shared entry (User only)
     * 
     * @param dbName The name of the storage to get
     * @param key The key of the entry to get
     * @return The shared entry
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun getSharedEntry(dbName: String, key: String): String {
        val url = config.url + "/storage/shared/$dbName/$key"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get shared entry")
        return response.body<String>()
    }

    /**
     * Gets a shared entry synchronously (User only)
     * 
     * @param dbName The name of the storage to get
     * @param key The key of the entry to get
     * @return The shared entry
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncGetSharedEntry(dbName: String, key: String): String {
        return runBlocking {
            getSharedEntry(dbName, key)
        }
    }

    /**
     * Sets a shared entry (User only)
     * 
     * @param dbName The name of the storage to set
     * @param key The key of the entry to set
     * @param value The value of the entry to set
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun setSharedEntry(dbName: String, key: String, value: String) {
        val url = config.url + "/storage/shared/$dbName/$key"
        val response = client.post(url) { setBody(value) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to set shared entry")
    }

    /**
     * Sets a shared entry synchronously (User only)
     * 
     * @param dbName The name of the storage to set
     * @param key The key of the entry to set
     * @param value The value of the entry to set
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncSetSharedEntry(dbName: String, key: String, value: String) {
        return runBlocking {
            setSharedEntry(dbName, key, value)
        }
    }

    /**
     * Gets a private table (User only)
     * 
     * @param dbName The name of the storage to get
     * @return The private table
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun getPrivateTable(dbName: String): List<RGDBStorageObject> {
        val url = config.url + "/storage/private/$dbName"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get private table")
        return response.body<List<RGDBStorageObject>>()
    }

    /**
     * Gets a private table synchronously (User only)
     * 
     * @param dbName The name of the storage to get
     * @return The private table
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncGetPrivateTable(dbName: String): List<RGDBStorageObject> {
        return runBlocking {
            getPrivateTable(dbName)
        }
    }

    /**
     * Gets a private entry (User only)
     * 
     * @param dbName The name of the storage to get
     * @param key The key of the entry to get
     * @return The private entry
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun getPrivateEntry(dbName: String, key: String): String {
        val url = config.url + "/storage/private/$dbName/$key"
        val response = client.get(url)
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.OK) throw Exception("Failed to get private entry")
        return response.body<String>()
    }

    /**
     * Gets a private entry synchronously (User only)
     * 
     * @param dbName The name of the storage to get
     * @param key The key of the entry to get
     * @return The private entry
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncGetPrivateEntry(dbName: String, key: String): String {
        return runBlocking {
            getPrivateEntry(dbName, key)
        }
    }

    /**
     * Sets a private entry (User only)
     * 
     * @param dbName The name of the storage to set
     * @param key The key of the entry to set
     * @param value The value of the entry to set
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.0
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    suspend fun setPrivateEntry(dbName: String, key: String, value: String) {
        val url = config.url + "/storage/private/$dbName/$key"
        val response = client.post(url) { setBody(value) }
        if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedError()
        if (response.status != HttpStatusCode.Created) throw Exception("Failed to set private entry")
    }

    /**
     * Sets a private entry synchronously (User only)
     * 
     * @param dbName The name of the storage to set
     * @param key The key of the entry to set
     * @param value The value of the entry to set
     * @throws UnauthorizedError If the user was not found
     * 
     * @see RGDBStorageObject
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun syncSetPrivateEntry(dbName: String, key: String, value: String) {
        return runBlocking {
            setPrivateEntry(dbName, key, value)
        }
    }

    /**
     * Converts a suspend function to a sync function
     * 
     * @param function The suspend function to convert
     * @return The sync function
     * 
     * @see runBlocking
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun asyncFunctionToSync(function: suspend () -> Unit): () -> Unit {
        return {
            runBlocking {
                function()
            }
        }
    }

    /**
     * Converts a kotlin function to a java future
     * 
     * @param action The kotlin function to convert
     * @return The java future
     * 
     * @see CompletableFuture
     * @since 1.2
     * 
     * @author Johannes (Jotrorox) Müller
     * @author RelaxoGames - Infrastructure Team (https://relaxogames.de)
     */
    fun <T> kotlinFunctionToJavaFuture(action: () -> T): CompletableFuture<T> {
        return CompletableFuture.supplyAsync {
            action()
        }
    }
}
