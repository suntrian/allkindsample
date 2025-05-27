plugins {
    java
    kotlin("jvm") version "2.1.20" apply false
}

group = "com.example"
version = "1.0.0-SNAPSHOT"


dependencies {

    implementation(platform("ch.qos.logback:logback-parent:1.5.18"))

}

subprojects {

    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        implementation("org.slf4j:slf4j-api:2.0.16")
        implementation("ch.qos.logback:logback-classic:1.5.18")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

        // JUnit 5 测试框架依赖
        testImplementation(platform("org.junit:junit-bom:5.10.2"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.junit.jupiter:junit-jupiter-engine")
        testImplementation("org.junit.jupiter:junit-jupiter-params")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}