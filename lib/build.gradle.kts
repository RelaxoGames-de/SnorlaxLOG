plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.md-5:bungeecord-api:1.20-R0.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}