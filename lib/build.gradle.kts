import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // Apply the maven-publish plugin for publishing artifacts
    `maven-publish`

    // Apply the kotlinx-serialization plugin for serialization
    kotlin("plugin.serialization").version("2.0.0")

    // Dokka for automatic documentation
    id("org.jetbrains.dokka").version("1.9.20")
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.20")
    }
}

val ktorVersion: String by project
val logbackVersion: String by project

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-cbor:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-protobuf:$ktorVersion")



    implementation("io.ktor:ktor-client-auth:$ktorVersion")

    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
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
            groupId = "de.relaxogames"
            artifactId = "snorlax-log"
            version = "1.7"

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

tasks.dokkaHtml {
    moduleName.set("SnorlaxLOG Documentation")

    dokkaSourceSets {
        named("main") {
            sourceRoots.from(file("src/main/kotlin"))
        }
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customStyleSheets = listOf(file("docs_src/logo-styles.css"))
        customAssets = listOf(file("docs_src/relaxogames_icon.png"))
        footerMessage = "Copyright © 2024 RelaxoGames. All rights reserved."
    }
}