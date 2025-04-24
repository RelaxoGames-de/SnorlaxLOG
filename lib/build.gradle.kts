plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // Apply the maven-publish plugin for publishing artifacts.
    `maven-publish`

    // Apply the kotlinx-serialization plugin for serialization.
    alias(libs.plugins.kotlinx.serialization)

    // Dokka for automatic documentation.
    alias(libs.plugins.dokka)
}

buildscript {
    dependencies { classpath("org.jetbrains.dokka:dokka-base:${libs.versions.dokka.get()}") }
}

repositories { mavenCentral() }

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)

    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.serialization.kotlinx.xml)
    implementation(libs.ktor.serialization.kotlinx.cbor)
    implementation(libs.ktor.serialization.kotlinx.protobuf)

    implementation(libs.kotlinx.io.core)

    implementation(libs.ktor.client.auth)

    implementation(libs.ktor.client.logging)
    implementation(libs.logback.classic)
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.relaxogames"
            artifactId = "snorlax-log"
            version = "1.11"

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

dokka {
    moduleName.set("SnorlaxLOG Documentation")

    dokkaSourceSets.main {
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(
                    uri(
                            "https://github.com/RelaxoGames-de/SnorlaxLOG/tree/main/lib/src/main/kotlin"
                    )
            )
            remoteLineSuffix.set("#L")
        }
    }

    pluginsConfiguration.html {
        customStyleSheets.from("docs_src/logo-styles.css")
        customAssets.from("docs_src/relaxogames_icon.png")
        footerMessage.set("Copyright © 2025 RelaxoGames. All rights reserved.")
    }
}
