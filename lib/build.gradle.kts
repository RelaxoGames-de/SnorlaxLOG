plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // Apply the maven-publish plugin for publishing artifacts
    `maven-publish`

    // Apply the kotlinx-serialization plugin for serialization
    kotlin("plugin.serialization").version("2.0.0")
}

val ktor_version: String by project
val logback_version: String by project

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation("io.ktor:ktor-client-auth:$ktor_version")

    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.relaxogames.snorlax-log"
            artifactId = "snorlax-log"
            version = "1.0.0"

            from(components["java"])

            pom {
                name.set("SnorlaxLOG")
                description.set("The central library for interacting with the RGDB Backend")
                url.set("https://github.com/RelaxoGames-de/SnorlaxLOG")

                developers {
                    developer {
                        id.set("jotrorox")
                        name.set("Johannes (Jotrorox) Müller")
                        email.set("mail@jotrorox.com")
                    }
                }
            }
        }
    }
}
