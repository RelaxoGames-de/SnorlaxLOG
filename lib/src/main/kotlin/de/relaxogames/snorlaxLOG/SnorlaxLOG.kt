package de.relaxogames.snorlaxLOG

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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
import java.io.Closeable
import java.io.File
import java.io.IOException

/**
 * Configuration for the [SnorlaxLOG] client
 *
 * @param url The URL of the RGDB Backend
 * @param username The username of the user
 * @param _password The password of the user
 * @param requestTimeoutMillis The timeout for requests in milliseconds
 * @param connectTimeoutMillis The timeout for connections in milliseconds
 * @param socketTimeoutMillis The timeout for sockets in milliseconds
 *
 * @see SnorlaxLOG
 * @since 1.0
 *
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
@Serializable
data class SnorlaxLOGConfig @JvmOverloads constructor(
    val url: String, 
    val username: String, 
    private val _password: String,
    val requestTimeoutMillis: Long = 10_000,
    val connectTimeoutMillis: Long = 5_000,
    val socketTimeoutMillis: Long = 5_000
) {
    /**
     * Gets the password securely
     * 
     * @return The password
     */
    fun getPassword(): String = _password

    /**
     * Prevents password from appearing in toString() output
     * 
     * @return String representation of the config without the password
     */
    override fun toString(): String = "SnorlaxLOGConfig(url=$url, username=$username, password=****, requestTimeoutMillis=$requestTimeoutMillis, connectTimeoutMillis=$connectTimeoutMillis, socketTimeoutMillis=$socketTimeoutMillis)"
}

/**
 * User object for the RGDB Backend. Users can have different [RGDBRole]s that determine their
 * permissions.
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
@Serializable data class RGDBUser(val name: String, val password: String, val role: RGDBRole)

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
    @Suppress("UNUSED") ADMIN("admin"),
    /**
     * Creator role (Has access to the RGDB Backend but can only create and delete storages)
     *
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("unused") CREATOR("creator"),
    /**
     * User role (Has access to the RGDB Backend but can only read and write to their own and shared
     * storages)
     *
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Suppress("unused") USER("user")
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
@Serializable data class RGDBStorage(val name: String)

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
data class RGDBStorageObject @JvmOverloads constructor(val key: String, val value: String, val isPrivate: Boolean = false)

/**
 * Statistics returned by the Server that are meant to give the user an overview on different
 * important factors such as the Filesystem-Size.
 *
 * @param count The number of user Storages currently in use
 * @param size The Filesystem-Size in Bytes
 *
 * @since 1.6
 *
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
@Serializable data class StorageStatistic(var count: Int, var size: Long)

/**
 * Statistics returned by the Server that are meant to give the user an overview on different
 * important factors such as the number of Users.
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
data class UserStatistic(
        var count: Int,
        var adminCount: Int,
        var creatorCount: Int,
        var userCount: Int
)

/**
 * Statistics returned by the Server, that are meant to give the user an overview on different
 * important factors such as the uptime of the server.
 *
 * @param upTime The uptime of the server in milliseconds
 *
 * @since 1.7
 *
 * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
 * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
 */
@Serializable data class ServerStatistics(var upTime: Long)

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
open class SnorlaxLOGException @JvmOverloads constructor(message: String, cause: Throwable? = null) :
        Exception(message, cause)

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
class UnauthorizedError @JvmOverloads constructor(message: String = "Unauthorized") : SnorlaxLOGException(message)

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
class NetworkError @JvmOverloads constructor(message: String, cause: Throwable? = null) : SnorlaxLOGException(message, cause)

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
 * Indicates that the annotated function is unstable and in alpha stage. Use with caution as the API
 * may change in the future.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class UnstableApi(@Suppress("UNUSED") val message: String = "This API is unstable and in alpha stage.")

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
class SnorlaxLOG @JvmOverloads constructor(
        /**
         * The configuration for the client
         *
         * @see SnorlaxLOGConfig
         */
        private val config: SnorlaxLOGConfig,

        /** Whether logging is enabled */
        private val loggingEnabled: Boolean = false
) : Closeable {
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
                install(HttpTimeout) {
                    requestTimeoutMillis = config.requestTimeoutMillis
                    connectTimeoutMillis = config.connectTimeoutMillis
                    socketTimeoutMillis = config.socketTimeoutMillis
                }
                install(ContentNegotiation) {
                    json(
                            Json {
                                prettyPrint = true
                                isLenient = true
                            }
                    )
                    xml(format = XML { xmlDeclMode = XmlDeclMode.Auto })
                    cbor(Cbor { ignoreUnknownKeys = true })
                    protobuf(ProtoBuf { encodeDefaults = true })
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = if (loggingEnabled) LogLevel.INFO else LogLevel.NONE
                    sanitizeHeader { header -> header == HttpHeaders.Authorization }
                }
                install(Auth) {
                    basic {
                        credentials { BasicAuthCredentials(config.username, config.getPassword()) }
                        sendWithoutRequest { true }
                    }
                }
            }

    /**
     * Tests the connection to the RGDB Backend
     *
     * @return Whether the connection was successful
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     * 
     * @sample de.relaxogames.snorlaxLOG.samples.SnorlaxLOGSamples.testConnectionSample
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun testConnection(): Boolean {
        val url = config.url + "/ping"
        try {
            val response = runBlocking { client.get(url) }
            return safeBodyParse<String>(response, "testing connection") == "pong"
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
     * Gets the user the snorlaxLOG client is authenticated as (User only)
     *
     * @return The self user
     * @throws UnauthorizedError If the credentials are invalid or the user doesn't have sufficient permissions
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBUser
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     * 
     * @sample de.relaxogames.snorlaxLOG.samples.SnorlaxLOGSamples.getSelfSample
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getSelf(): RGDBUser {
        val url = config.url + "/user/self"
        try {
            val response = runBlocking { client.get(url) }
            return safeBodyParse<RGDBUser>(response, "getting self user")
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
     * Changes the password of the user the snorlaxLOG client is authenticated as (User only)
     *
     * @param newPassword The new password
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun changePassword(newPassword: String) {
        if (newPassword.isBlank()) {
            throw InvalidInputError("New password cannot be blank")
        }

        val url = config.url + "/user/self/password"
        try {
            val response = runBlocking { client.put(url) { setBody(newPassword) } }
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
     * Gets all users (Admin only)
     *
     * @return A list of all users
     * @throws UnauthorizedError If the credentials are invalid or the user doesn't have admin permissions
     * @throws NetworkError If there was a network issue while getting the users
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBUser
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     * 
     * @sample de.relaxogames.snorlaxLOG.samples.SnorlaxLOGSamples.getUsersSample
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getUsers(): List<RGDBUser> {
        val url = config.url + "/admin/users"
        try {
            val response = runBlocking { client.get(url) }
            return safeBodyParse<List<RGDBUser>>(response, "getting users list")
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
     * Creates a user (Admin only)
     *
     * @param user The user to create
     * @throws UnauthorizedError If the credentials are invalid or the user doesn't have admin permissions
     * @throws InvalidInputError If the user data is invalid
     * @throws NetworkError If there was a network issue while creating the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBUser
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     * 
     * @sample de.relaxogames.snorlaxLOG.samples.SnorlaxLOGSamples.createUserSample
     */
    @Throws(UnauthorizedError::class, InvalidInputError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun createUser(user: RGDBUser) {
        if (user.name.isBlank() || user.password.isBlank()) {
            throw InvalidInputError("User name and password cannot be blank")
        }

        val url = config.url + "/admin/users"
        try {
            val response = runBlocking {
                    client.post(url) {
                        contentType(ContentType.Application.Json)
                        setBody(user)
                    }
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
     * Deletes a user (Admin only)
     *
     * @param name The name of the user to delete
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun deleteUser(name: String) {
        if (name.isBlank()) {
            throw InvalidInputError("User name cannot be blank")
        }

        val url = config.url + "/admin/users/$name"
        try {
            val response = runBlocking { client.delete(url) }
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
     * Updates the role of a user (Admin only)
     *
     * @param name The name of the user to update
     * @param role The new role
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBRole
     * @see RGDBUser
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate", "UNUSED")
    fun updateUserRole(name: String, role: RGDBRole) {
        if (name.isBlank()) {
            throw InvalidInputError("User name cannot be blank")
        }

        val url = config.url + "/admin/users/$name/role"
        try {
            val response = runBlocking {
                    client.put(url) {
                        contentType(ContentType.Application.Json)
                        setBody(role)
                    }
            }
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
     * Updates the password of a user (Admin only)
     *
     * @param name The name of the user to update
     * @param newPassword The new password
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBUser
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun updateUserPassword(name: String, newPassword: String) {
        if (name.isBlank() || newPassword.isBlank()) {
            throw InvalidInputError("User name and/or new password cannot be blank")
        }

        val url = config.url + "/admin/users/$name/password"
        try {
            val response = runBlocking { client.put(url) { setBody(newPassword) } }
            handleResponse(response, "updating password for user '$name'")
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while updating user password", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else ->
                        throw SnorlaxLOGException(
                                "Unexpected error while updating user password",
                                e
                        )
            }
        }
    }

    /**
     * Gets a user (Admin only)
     *
     * @param name The name of the user to get
     * @return The user
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBUser
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getUser(name: String): RGDBUser {
        if (name.isBlank()) {
            throw InvalidInputError("User name cannot be blank")
        }

        val url = config.url + "/admin/users/$name"
        try {
            val response = runBlocking { client.get(url) }
            handleResponse(response, "getting user '$name'")
            return runBlocking { response.body<RGDBUser>() }
        } catch (e: IOException) {
            throw NetworkError("Failed to connect to server while getting user", e)
        } catch (e: Exception) {
            when (e) {
                is SnorlaxLOGException -> throw e
                else -> throw SnorlaxLOGException("Unexpected error while getting user", e)
            }
        }
    }

    // Creator only
    /**
     * Creates a storage (Creator only)
     *
     * @param name The name of the storage to create
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBStorage
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, InvalidInputError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun createStorage(name: String) {
        if (name.isBlank()) {
            throw InvalidInputError("Storage name cannot be blank")
        }

        val url = config.url + "/creator/storages"
        val storage = RGDBStorage(name)
        try {
            val response = runBlocking {
                    client.post(url) {
                        contentType(ContentType.Application.Json)
                        setBody(storage)
                    }
            }
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
     * Gets a backup of all storages (Creator only)
     *
     * @return A temporary file containing the backup
     * @throws NetworkError If there was a network issue while getting the backup
     * @throws SnorlaxLOGException If an unexpected error occurs
     * @throws UnauthorizedError If the user was not found
     *
     * @since 1.9
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    @UnstableApi
    fun getBackup(): File {
        val url = config.url + "/creator/storages/backup"
        val tempFile = runBlocking { File.createTempFile("backup", ".zip") }
        runBlocking {
            val response = client.get(url)
            handleResponse(response, "getting backup")
            tempFile.writeBytes(response.readRawBytes())
        }
        tempFile.deleteOnExit()
        return tempFile
    }

    // USER ONLY
    /**
     * Gets all storage names (User only)
     *
     * @return A list of all the names (in form of RGDB Storages)
     * @throws UnauthorizedError If the credentials are invalid or the user doesn't have sufficient permissions
     * @throws NetworkError If there was a network issue while getting the storages
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBStorage
     * @since 1.6
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     * 
     * @sample de.relaxogames.snorlaxLOG.samples.SnorlaxLOGSamples.getStoragesSample
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getStorages(): List<RGDBStorage> {
        val url = config.url + "/storage"
        try {
            val response = runBlocking { client.get(url) }
            handleResponse(response, "getting storage list")
            return runBlocking { response.body<List<RGDBStorage>>() }
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
     * Gets a shared table (User only)
     *
     * @param dbName The name of the storage to get
     * @return The shared table
     * @throws UnauthorizedError If the credentials are invalid or the user doesn't have sufficient permissions
     * @throws InvalidInputError If the database name is blank
     * @throws NetworkError If there was a network issue while getting the shared table
     * @throws NotFoundException If the storage was not found
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBStorageObject
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     * 
     * @sample de.relaxogames.snorlaxLOG.samples.SnorlaxLOGSamples.getSharedTableSample
     */
    @Throws(UnauthorizedError::class, NotFoundException::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getSharedTable(dbName: String): List<RGDBStorageObject> {
        if (dbName.isBlank()) {
            throw InvalidInputError("Database name cannot be blank")
        }

        val url = config.url + "/storage/shared/$dbName"
        try {
            val response = runBlocking { client.get(url) }
            handleResponse(response, "getting shared table for '$dbName'")
            return runBlocking { response.body<List<RGDBStorageObject>>() }
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
     * Gets a shared entry (User only)
     *
     * @param dbName The name of the storage to get
     * @param key The key of the entry to get
     * @return The shared entry
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBStorageObject
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NotFoundException::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getSharedEntry(dbName: String, key: String): String {
        if (dbName.isBlank() || key.isBlank()) {
            throw InvalidInputError("Database name and/or key cannot be blank")
        }

        val url = config.url + "/storage/shared/$dbName/$key"
        try {
            val response = runBlocking { client.get(url) }
            handleResponse(response, "getting shared entry '$key' from '$dbName'")
            return runBlocking { response.body<String>() }
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
     * Sets a shared entry (User only)
     *
     * @param dbName The name of the storage to set
     * @param key The key of the entry to set
     * @param value The value of the entry to set
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBStorageObject
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NotFoundException::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun setSharedEntry(dbName: String, key: String, value: String) {
        if (dbName.isBlank() || key.isBlank()) {
            throw InvalidInputError("Database name and/or key cannot be blank")
        }

        val url = config.url + "/storage/shared/$dbName/$key"
        val response = runBlocking { client.post(url) { setBody(value) } }
        handleResponse(response, "setting shared entry '$key' in '$dbName'")
    }

    /**
     * Gets a private table (User only)
     *
     * @param dbName The name of the storage to get
     * @return The private table
     * @throws UnauthorizedError If the credentials are invalid or the user doesn't have sufficient permissions
     * @throws InvalidInputError If the database name is blank
     * @throws NetworkError If there was a network issue while getting the private table
     * @throws NotFoundException If the storage was not found
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBStorageObject
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     * 
     * @sample de.relaxogames.snorlaxLOG.samples.SnorlaxLOGSamples.getPrivateTableSample
     */
    @Throws(UnauthorizedError::class, NotFoundException::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getPrivateTable(dbName: String): List<RGDBStorageObject> {
        if (dbName.isBlank()) {
            throw InvalidInputError("Database name cannot be blank")
        }

        val url = config.url + "/storage/private/$dbName"
        try {
            val response = runBlocking { client.get(url) }
            handleResponse(response, "getting private table for '$dbName'")
            return runBlocking { response.body<List<RGDBStorageObject>>() }
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
     * Gets a private entry (User only)
     *
     * @param dbName The name of the storage to get
     * @param key The key of the entry to get
     * @return The private entry
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBStorageObject
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NotFoundException::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getPrivateEntry(dbName: String, key: String): String {
        if (dbName.isBlank() || key.isBlank()) {
            throw InvalidInputError("Database name and/or key cannot be blank")
        }

        val url = config.url + "/storage/private/$dbName/$key"
        try {
            val response = runBlocking { client.get(url) }
            handleResponse(response, "getting private entry '$key' from '$dbName'")
            return runBlocking { response.body<String>() }
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
     * Sets a private entry (User only)
     *
     * @param dbName The name of the storage to set
     * @param key The key of the entry to set
     * @param value The value of the entry to set
     * @throws UnauthorizedError If the user was not found
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see RGDBStorageObject
     * @since 1.0
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NotFoundException::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun setPrivateEntry(dbName: String, key: String, value: String) {
        if (dbName.isEmpty() || key.isEmpty() || value.isEmpty()) {
            throw IllegalArgumentException("dbName, key and value must not be empty")
        }

        val url = config.url + "/storage/private/$dbName/$key"
        val response = runBlocking { client.post(url) { setBody(value) } }
        handleResponse(response, "setting private entry '$key' in '$dbName'")
    }

    // Statistic Endpoints
    /**
     * Gets the statistics of the current RGDB Instance regarding Storage, such as Filesystem-size
     * etc. (User only)
     *
     * @return The Storage Statistics of the current RGDB Server
     * @throws Exception If the instance can't retrieve the info from the Server
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see StorageStatistic
     * @since 1.6
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getStorageStatistics(): StorageStatistic {
        val url = config.url + "/statistic/storages"
        val response = runBlocking { client.get(url) }
        handleResponse(response, "getting storage statistics")
        return runBlocking { response.body<StorageStatistic>() }
    }

    /**
     * Gets the statistics of the current RGDB Instance the info regarding Users, such as currently
     * existing etc. (User only)
     *
     * @return The User Statistics of the current RGDB Server
     * @throws Exception If the instance can't retrieve the info from the Server
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see UserStatistic
     * @since 1.6
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getUserStatistics(): UserStatistic {
        val url = config.url + "/statistic/user"
        val response = runBlocking { client.get(url) }
        return safeBodyParse<UserStatistic>(response, "getting user statistics")
    }

    /**
     * Gets the statistics of the current RGDB Instance the info regarding the Server itself, such
     * as the UpTime (User only)
     *
     * @return The User Statistics of the current RGDB Server
     * @throws Exception If the instance can't retrieve the info from the Server
     * @throws NetworkError If there was a network issue while getting the user
     * @throws SnorlaxLOGException If an unexpected error occurs
     *
     * @see ServerStatistics
     * @since 1.7
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NetworkError::class, SnorlaxLOGException::class)
    @Suppress("MemberVisibilityCanBePrivate")
    fun getServerStatistics(): ServerStatistics {
        val url = config.url + "/statistic/server"
        val response = runBlocking { client.get(url) }
        return safeBodyParse<ServerStatistics>(response, "getting server statistics")
    }

    /**
     * Handles HTTP response status codes and throws appropriate exceptions
     *
     * @param response The HTTP response to handle
     * @param context Additional context for error messages
     * @throws SnorlaxLOGException if the response indicates an error
     * @throws UnauthorizedError If the user was not found
     * @throws NotFoundException If the resource was not found
     * @throws InvalidInputError If the input was invalid
     * @throws ServerError If the server encountered an error
     *
     * @since 1.7
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NotFoundException::class, InvalidInputError::class, ServerError::class, SnorlaxLOGException::class)
    private fun handleResponse(response: HttpResponse, context: String) {
        when (response.status.value) {
            in 200..299 -> return // Success
            401 -> throw UnauthorizedError("Unauthorized access: $context")
            404 -> throw NotFoundException("Resource not found: $context")
            400 -> throw InvalidInputError("Invalid input for: $context")
            in 500..599 -> throw ServerError("Server error while $context")
            else ->
                    throw SnorlaxLOGException(
                            "Unexpected error (${response.status.value}) while $context"
                    )
        }
    }

    /**
     * Safely parses the response body with proper error handling
     *
     * @param response The HTTP response to parse
     * @param context Additional context for error messages
     * @return The parsed body
     * @throws SnorlaxLOGException if parsing fails
     * @throws UnauthorizedError If the user was not found
     * @throws NotFoundException If the resource was not found
     * @throws InvalidInputError If the input was invalid
     * @throws ServerError If the server encountered an error
     *
     * @since 1.9
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    @Throws(UnauthorizedError::class, NotFoundException::class, InvalidInputError::class, ServerError::class, SnorlaxLOGException::class)
    private inline fun <reified T> safeBodyParse(response: HttpResponse, context: String): T {
        handleResponse(response, context)
        try {
            return runBlocking { response.body<T>() }
        } catch (e: Exception) {
            throw SnorlaxLOGException("Failed to parse response body for $context", e)
        }
    }

    /**
     * Closes the HTTP client
     *
     * @since 1.9
     *
     * @author Johannes ([Jotrorox](https://jotrorox.com)) Müller
     * @author The [RelaxoGames](https://relaxogames.de) Infrastructure Team
     */
    override fun close() {
        client.close()
    }
}
