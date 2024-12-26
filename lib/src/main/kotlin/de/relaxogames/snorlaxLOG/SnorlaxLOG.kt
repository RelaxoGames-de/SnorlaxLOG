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
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.cbor.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.protobuf.*
import io.ktor.serialization.kotlinx.xml.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.XML
import java.util.concurrent.CompletableFuture
import java.io.IOException

/**
 * Configuration for the [SnorlaxLOG] client
 * 
 * @param url The URL of the RGDB Backend
 * @param username The username of the user
 * @param password The password of the user
 * 
 * @see SnorlaxLOG
 * @since 1.0
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
@Serializable
data class SnorlaxLOGConfig(val url: String, val username: String, val password: String)

/**
 * User object for the RGDB Backend. Users can have different [RGDBRole]s that determine their permissions.
 * 
 * @param name The name of the user
 * @param password The password of the user
 * @param role The [RGDBRole] of the user
 * 
 * @see RGDBRole
 * @since 1.0
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
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
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
@Serializable
@Suppress("UNUSED")
enum class RGDBRole(private val value: String) {
    /**
     * Admin role (Has full access to the RGDB Backend)
     * 
     * @since 1.0
     * 
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
    ADMIN("admin"),
    /**
     * Creator role (Has access to the RGDB Backend but can only create and delete storages)
     * 
     * @since 1.0
     * 
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("unused")
    CREATOR("creator"),
    /**
     * User role (Has access to the RGDB Backend but can only read and write to their own and shared storages)
     * 
     * @since 1.0
     * 
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("unused")
    USER("user")
}

/**
 * Storage object for the RGDB Backend (Creator only). Contains [RGDBStorageObject]s.
 * 
 * @param name The name of the storage
 * 
 * @see RGDBStorageObject
 * @since 1.0
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
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
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
@Serializable
data class RGDBStorageObject(val key: String, val value: String, val isPrivate: Boolean = false)

/**
 * Statistics returned by the Server, that are meant to give the user
 * an overview on different important factors such as the Filesystem-Size.
 *
 * @param count The amount of user Storages currently in use
 * @param size The Filesystem-Size in Bytes
 *
 * @since 1.6
 *
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
@Serializable
data class StorageStatistic(var count: Int, var size: Long)

/**
 * Statistics returned by the Server, that are meant to give the user
 * an overview on different important factors such as the amount of Users.
 *
 * @param count The amount of Users currently in the System
 * @param adminCount The amount of Admins in the System
 * @param creatorCount The amount of Creators in the System
 * @param userCount The amount of Users (non Creator/Admins) in the System
 *
 * @since 1.6
 *
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
@Serializable
data class UserStatistic(var count: Int, var adminCount: Int, var creatorCount: Int, var userCount: Int)

/**
 * Base exception class for SnorlaxLOG errors
 *
 * @param message The error message
 * @param cause The cause of the error
 * 
 * @since 1.7
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
open class SnorlaxLOGException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Thrown when authentication fails (401)
 * 
 * @param message The error message
 * 
 * @see SnorlaxLOGException
 * @since 1.7
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
class UnauthorizedError(message: String = "Unauthorized") : SnorlaxLOGException(message)

/**
 * Thrown when a resource is not found (404)
 * 
 * @param message The error message
 * 
 * @see SnorlaxLOGException
 * @since 1.7
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
class NotFoundException(message: String) : SnorlaxLOGException(message)

/**
 * Thrown when the server returns an error (500)
 * 
 * @param message The error message
 * 
 * @see SnorlaxLOGException
 * @since 1.7
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
class ServerError(message: String) : SnorlaxLOGException(message)

/**
 * Thrown when a request fails due to network issues
 * 
 * @param message The error message
 * @param cause The cause of the error
 * 
 * @see SnorlaxLOGException
 * @since 1.7
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
class NetworkError(message: String, cause: Throwable? = null) : SnorlaxLOGException(message, cause)

/**
 * Thrown when a request fails due to invalid input
 * 
 * @param message The error message
 * 
 * @see SnorlaxLOGException
 * @since 1.7
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
class InvalidInputError(message: String) : SnorlaxLOGException(message)

/**
 * Main client for interacting with the RGDB Backend. Uses [SnorlaxLOGConfig] for configuration.
 * 
 * @param config The [SnorlaxLOGConfig] for the client
 * @param loggingEnabled Whether logging is enabled
 *
 * @see SnorlaxLOGConfig
 * @since 1.0
 * 
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
class SnorlaxLOG(
    /**
     * The configuration for the client
     * 
     * @see SnorlaxLOGConfig
     */
    private val config: SnorlaxLOGConfig,

    /**
     * Whether logging is enabled
     */
    private val loggingEnabled: Boolean = false
) {
    /**
     * The HTTP client for the SnorlaxLOG client
     * 
     * @see HttpClient
     * @since 1.0
     * 
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @OptIn(ExperimentalSerializationApi::class)
    private val client =
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                    xml(format = XML {
                        xmlDeclMode = XmlDeclMode.Auto
                    })
                    cbor(Cbor {
                        ignoreUnknownKeys = true
                    })
                    protobuf(ProtoBuf {
                        encodeDefaults = true
                    })
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = if (loggingEnabled) LogLevel.INFO else LogLevel.NONE
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun testConnection(): Boolean {
        val url = config.url + "/ping"
        try {
            val response = client.get(url)
            handleResponse(response, "testing connection")
            return response.body<String>() == "pong"
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while testing connection", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while testing connection", e)
            }
        }
    }

    /**
     * Tests the connection to the RGDB Backend synchronously
     * 
     * @return Whether the connection was successful
     * @throws UnauthorizedError If the connection was not successful
     * 
     * @since 1.2
     * 
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getSelf(): RGDBUser {
        val url = config.url + "/user/self"
        try {
            val response = client.get(url)
            handleResponse(response, "getting self user")
            return response.body<RGDBUser>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting self user", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting self user", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun changePassword(newPassword: String) {
        if (newPassword.isBlank()) {
            throw InvalidInputError("New password cannot be blank")
        }

        val url = config.url + "/user/self/password"
        try {
            val response = client.put(url) { setBody(newPassword) }
            handleResponse(response, "changing password")
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while changing password", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while changing password", e)
            }
        }
    }

    /**
     * Changes the password of the user the snorlaxLOG client is authenticated as synchronously (User only)
     * 
     * @param newPassword The new password
     * @throws UnauthorizedError If the user was not found
     * 
     * @since 1.2
     * 
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getUsers(): List<RGDBUser> {
        val url = config.url + "/admin/users"
        try {
            val response = client.get(url)
            handleResponse(response, "getting users list")
            return response.body<List<RGDBUser>>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting users", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting users", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun createUser(user: RGDBUser) {
        if (user.name.isBlank() || user.password.isBlank()) {
            throw InvalidInputError("User name and password cannot be blank")
        }

        val url = config.url + "/admin/users"
        try {
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            handleResponse(response, "creating user '${user.name}'")
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while creating user", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while creating user", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun deleteUser(name: String) {
        if (name.isBlank()) {
            throw InvalidInputError("User name cannot be blank")
        }

        val url = config.url + "/admin/users/$name"
        try {
            val response = client.delete(url)
            handleResponse(response, "deleting user '$name'")
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while deleting user", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while deleting user", e)
            }
        }
    }

    /**
     * Deletes a user synchronously (Admin only)
     * 
     * @param name The name of the user to delete
     * @throws UnauthorizedError If the user was not found
     * 
     * @since 1.2
     * 
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate", "UNUSED")
    suspend fun updateUserRole(name: String, role: RGDBRole) {
        if (name.isBlank()) {
            throw InvalidInputError("User name cannot be blank")
        }

        val url = config.url + "/admin/users/$name/role"
        try {
            val response = client.put(url) { setBody(role) }
            handleResponse(response, "updating role for user '$name'")
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while updating user role", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while updating user role", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun updateUserPassword(name: String, newPassword: String) {
        val url = config.url + "/admin/users/$name/password"
        try {
            val response = client.put(url) { setBody(newPassword) }
            handleResponse(response, "updating password for user '$name'")
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while updating user password", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while updating user password", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getUser(name: String): RGDBUser {
        val url = config.url + "/admin/users/$name"
        try {
            val response = client.get(url)
            handleResponse(response, "getting user '$name'")
            return response.body<RGDBUser>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting user", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting user", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun createStorage(name: String) {
        if (name.isBlank()) {
            throw InvalidInputError("Storage name cannot be blank")
        }
        
        val url = config.url + "/creator/storages"
        val storage = RGDBStorage(name)
        try {
            val response = client.post(url) { setBody(storage) }
            handleResponse(response, "creating storage '$name'")
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while creating storage", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while creating storage", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
    fun syncCreateStorage(name: String) {
        return runBlocking {
            createStorage(name)
        }
    }

    // USER ONLY
    /**
     * Gets all storage names (User only)
     *
     * @return A list of all the names (in form of RGDB Storages)
     * @throws UnauthorizedError If the user was not found
     * @throws Exception If there was another unidentified Error
     *
     * @see RGDBStorage
     * @since 1.6
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getStorages(): List<RGDBStorage> {
        val url = config.url + "/storage"
        try {
            val response = client.get(url)
            handleResponse(response, "getting storage list")
            return response.body<List<RGDBStorage>>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting storages", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting storages", e)
            }
        }
    }

    /**
     * Gets all storage names synchronously (User only)
     *
     * @return A list of all the names (in form of RGDB Storages)
     * @throws UnauthorizedError If the user was not found
     * @throws Exception If there was another unidentified Error
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
    fun syncGetStorages(): List<RGDBStorage> {
        return runBlocking {
            getStorages()
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getStorage(name: String): RGDBStorage {
        val url = config.url + "/storage/$name"
        try {
            val response = client.get(url)
            handleResponse(response, "getting storage '$name'")
            return response.body<RGDBStorage>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting storage", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting storage", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getSharedTable(dbName: String): List<RGDBStorageObject> {
        if (dbName.isBlank()) {
            throw InvalidInputError("Database name cannot be blank")
        }

        val url = config.url + "/storage/shared/$dbName"
        try {
            val response = client.get(url)
            handleResponse(response, "getting shared table for '$dbName'")
            return response.body<List<RGDBStorageObject>>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting shared table", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting shared table", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getSharedEntry(dbName: String, key: String): String {
        if (dbName.isBlank() || key.isBlank()) {
            throw InvalidInputError("Database name and key cannot be blank")
        }

        val url = config.url + "/storage/shared/$dbName/$key"
        try {
            val response = client.get(url)
            handleResponse(response, "getting shared entry '$key' from '$dbName'")
            return response.body<String>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting shared entry", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting shared entry", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun setSharedEntry(dbName: String, key: String, value: String) {
        val url = config.url + "/storage/shared/$dbName/$key"
        val response = client.post(url) { setBody(value) }
        handleResponse(response, "setting shared entry '$key' in '$dbName'")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getPrivateTable(dbName: String): List<RGDBStorageObject> {
        val url = config.url + "/storage/private/$dbName"
        try {
            val response = client.get(url)
            handleResponse(response, "getting private table for '$dbName'")
            return response.body<List<RGDBStorageObject>>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting private table", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting private table", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun getPrivateEntry(dbName: String, key: String): String {
        val url = config.url + "/storage/private/$dbName/$key"
        try {
            val response = client.get(url)
            handleResponse(response, "getting private entry '$key' from '$dbName'")
            return response.body<String>()
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting private entry", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting private entry", e)
            }
        }
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun setPrivateEntry(dbName: String, key: String, value: String) {
        val url = config.url + "/storage/private/$dbName/$key"
        val response = client.post(url) { setBody(value) }
        handleResponse(response, "setting private entry '$key' in '$dbName'")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
    fun syncSetPrivateEntry(dbName: String, key: String, value: String) {
        return runBlocking {
            setPrivateEntry(dbName, key, value)
        }
    }

    // Statistic Endpoints
    /**
     * Gets the statistics of the current RGDB Instance
     * regarding Storage, such as Filesystem-size etc. (User only)
     *
     * @return The Storage Statistics of the current RGDB Server
     * @throws Exception If the instance can't retrieve the info from the Server
     *
     * @see StorageStatistic
     * @since 1.6
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    suspend fun getStorageStatistics(): StorageStatistic {
        val url = config.url + "/statistic/storages"
        val response = client.get(url)
        handleResponse(response, "getting storage statistics")
        return response.body<StorageStatistic>()
    }

    /**
     * Gets the statistics of the current RGDB Instance in a blocking (synchronous)
     * way regarding Storage, such as Filesystem-size etc. (User only)
     *
     * @return The Storage Statistics of the current RGDB Server
     * @throws Exception If the instance can't retrieve the info from the Server
     *
     * @see StorageStatistic
     * @since 1.6
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
    fun syncGetStorageStatistics(): StorageStatistic {
        return runBlocking {
            getStorageStatistics()
        }
    }

    /**
     * Gets the statistics of the current RGDB Instance the info
     * regarding Users, such as currently existing etc. (User only)
     *
     * @return The User Statistics of the current RGDB Server
     * @throws Exception If the instance can't retrieve the info from the Server
     *
     * @see UserStatistic
     * @since 1.6
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    suspend fun getUserStatistics(): UserStatistic {
        val url = config.url + "/statistic/users"
        val response = client.get(url)
        handleResponse(response, "getting user statistics")
        return response.body<UserStatistic>()
    }

    /**
     * Gets the statistics of the current RGDB Instance in a blocking (synchronous)
     * way, the statistics are regarding Users, such as currently existing etc. (User only)
     *
     * @return The User Statistics of the current RGDB Server
     * @throws Exception If the instance can't retrieve the info from the Server
     *
     * @see UserStatistic
     * @since 1.6
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
    fun syncGetUserStatistics(): UserStatistic {
        return runBlocking {
            getUserStatistics()
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
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
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("UNUSED")
    fun <T> kotlinFunctionToJavaFuture(action: () -> T): CompletableFuture<T> {
        return CompletableFuture.supplyAsync {
            action()
        }
    }

    /**
     * Handles HTTP response status codes and throws appropriate exceptions
     *
     * @param response The HTTP response to handle
     * @param context Additional context for error messages
     * @throws SnorlaxLOGException if the response indicates an error
     *
     * @since 1.7
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    private fun handleResponse(response: HttpResponse, context: String) {
        when (response.status.value) {
            in 200..299 -> return // Success
            401 -> throw UnauthorizedError("Unauthorized access: $context")
            404 -> throw NotFoundException("Resource not found: $context")
            400 -> throw InvalidInputError("Invalid input for: $context")
            in 500..599 -> throw ServerError("Server error while $context")
            else -> throw SnorlaxLOGException("Unexpected error (${response.status.value}) while $context")
        }
    }
}
