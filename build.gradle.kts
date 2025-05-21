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
        implementation("ch.qos.logback:logback-core:1.5.18")
    }


}