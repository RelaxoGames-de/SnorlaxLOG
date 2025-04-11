package de.relaxogames.snorlaxLOG.samples

import de.relaxogames.snorlaxLOG.*
import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * This file contains sample code for the SnorlaxLOG library.
 * These samples are referenced in the KDoc comments using the @sample tag.
 */
object SnorlaxLOGSamples {
    // A sample dispatcher for demonstration purposes
    private val sampleDispatcher = Dispatchers.Default

    // A coroutine scope for samples
    private val sampleScope = CoroutineScope(sampleDispatcher)

    /**
     * Sample for testing connection to the RGDB Backend
     */
    fun testConnectionSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "username", "password")
        val client = SnorlaxLOG(config)

        // Using coroutines
        sampleScope.launch {
            try {
                val isConnected = client.testConnection()
                println("Connection successful: $isConnected")
            } catch (e: UnauthorizedError) {
                println("Invalid credentials")
            } catch (e: NetworkError) {
                println("Network error: ${e.message}")
            }
        }
    }

    /**
     * Sample for getting the authenticated user
     */
    fun getSelfSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "username", "password")
        val client = SnorlaxLOG(config)

        // Using coroutines
        sampleScope.launch {
            try {
                val user = client.getSelf()
                println("Logged in as: ${user.name} with role: ${user.role}")
            } catch (e: UnauthorizedError) {
                println("Authentication failed: ${e.message}")
            } catch (e: NetworkError) {
                println("Network error: ${e.message}")
            }
        }
    }

    /**
     * Sample for getting all users (Admin only)
     */
    fun getUsersSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "admin", "password")
        val client = SnorlaxLOG(config)

        // Using coroutines
        sampleScope.launch {
            try {
                val users = client.getUsers()
                println("Found ${users.size} users:")
                users.forEach { user ->
                    println("- ${user.name} (${user.role})")
                }
            } catch (e: UnauthorizedError) {
                println("Admin permissions required: ${e.message}")
            } catch (e: NetworkError) {
                println("Network error: ${e.message}")
            }
        }
    }

    /**
     * Sample for creating a user (Admin only)
     */
    fun createUserSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "admin", "password")
        val client = SnorlaxLOG(config)

        // Using coroutines
        sampleScope.launch {
            try {
                val newUser = RGDBUser("newuser", "password123", RGDBRole.USER)
                client.createUser(newUser)
                println("User ${newUser.name} created successfully")
            } catch (e: UnauthorizedError) {
                println("Admin permissions required: ${e.message}")
            } catch (e: InvalidInputError) {
                println("Invalid user data: ${e.message}")
            } catch (e: NetworkError) {
                println("Network error: ${e.message}")
            }
        }
    }

    /**
     * Sample for getting all storage names (User only)
     */
    fun getStoragesSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "username", "password")
        val client = SnorlaxLOG(config)

        // Using coroutines
        sampleScope.launch {
            try {
                val storages = client.getStorages()
                println("Available storages:")
                storages.forEach { storage ->
                    println("- ${storage.name}")
                }
            } catch (e: UnauthorizedError) {
                println("Authentication failed: ${e.message}")
            } catch (e: NetworkError) {
                println("Network error: ${e.message}")
            }
        }
    }

    /**
     * Sample for getting a shared table (User only)
     */
    fun getSharedTableSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "username", "password")
        val client = SnorlaxLOG(config)

        // Using coroutines
        sampleScope.launch {
            try {
                val sharedTable = client.getSharedTable("myDatabase")
                println("Shared entries in myDatabase:")
                sharedTable.forEach { entry ->
                    println("${entry.key}: ${entry.value}")
                }
            } catch (e: UnauthorizedError) {
                println("Authentication failed: ${e.message}")
            } catch (e: NotFoundException) {
                println("Database not found: ${e.message}")
            } catch (e: NetworkError) {
                println("Network error: ${e.message}")
            }
        }
    }

    /**
     * Sample for getting a private table (User only)
     */
    fun getPrivateTableSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "username", "password")
        val client = SnorlaxLOG(config)

        // Using coroutines
        sampleScope.launch {
            try {
                val privateTable = client.getPrivateTable("myDatabase")
                println("Private entries in myDatabase:")
                privateTable.forEach { entry ->
                    println("${entry.key}: ${entry.value}")
                }
            } catch (e: UnauthorizedError) {
                println("Authentication failed: ${e.message}")
            } catch (e: NotFoundException) {
                println("Database not found: ${e.message}")
            } catch (e: NetworkError) {
                println("Network error: ${e.message}")
            }
        }
    }

    /**
     * Sample for using asyncFunctionToSync
     */
    fun asyncFunctionToSyncSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "username", "password")
        val client = SnorlaxLOG(config)

        // Instead of this:
        val syncGetUsers = client.asyncFunctionToSync { client.getUsers() }
        val users = syncGetUsers()

        // Use this:
        val users2 = client.syncGetUsers()

        // Or implement your own wrapper with proper context:
        fun <T> withMyContext(block: suspend () -> T): T {
            return runBlocking(sampleDispatcher) { block() }
        }
    }

    /**
     * Sample for using kotlinFunctionToJavaFuture
     */
    fun kotlinFunctionToJavaFutureSample() {
        val config = SnorlaxLOGConfig("https://rgdb.example.com", "username", "password")
        val client = SnorlaxLOG(config)

        // Instead of this:
        val future = client.kotlinFunctionToJavaFuture { client.syncGetUsers() }

        // Use this directly:
        val future2 = CompletableFuture.supplyAsync { client.syncGetUsers() }

        // Or use Kotlin's coroutine integration:
        val deferred = GlobalScope.async { client.getUsers() }
        // Note: In newer Kotlin versions, you would use future conversion utilities
    }
}
