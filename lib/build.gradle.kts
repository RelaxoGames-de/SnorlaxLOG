group = "de.relaxogames"
version = "1.0.0"

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    id("maven-publish")

    kotlin("plugin.serialization") version "2.0.21"

    id("org.jetbrains.dokka") version "1.9.20"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.ktor:ktor-client-core:3.0.1")
    implementation("io.ktor:ktor-client-cio:3.0.1")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "snorlaxlog"
            from(components["java"])
            
            // Add sources JAR
            artifact(tasks.register("sourcesJar", Jar::class) {
                archiveClassifier.set("sources")
                from(sourceSets.main.get().allSource)
            })

            // Add documentation JAR
            artifact(tasks.register("javadocJar", Jar::class) {
                archiveClassifier.set("javadoc")
                from(tasks.dokkaHtml)
                dependsOn(tasks.dokkaHtml)
            })
            
            pom {
                name.set("SnorlaxLOG")
                description.set("SnorlaxLOG is a Kotlin library for interacting with the Relaxogames DB API.")
                url.set("https://github.com/relaxogames/snorlaxlog")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
            }
        }
    }
    repositories {
        mavenLocal()
    }
}