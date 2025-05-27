plugins {
    kotlin("jvm") version "2.1.20"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.opentelemetry:opentelemetry-sdk:1.50.0")
    implementation("io.opentelemetry:opentelemetry-api:1.50.0")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure:1.50.0")
    implementation("io.opentelemetry:opentelemetry-exporter-logging:1.50.0")
    implementation("io.opentelemetry:opentelemetry-extension-kotlin:1.50.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}