plugins {
    java
    kotlin("jvm") version "2.1.20" apply false
}

group = "com.example"
version = "1.0.0-SNAPSHOT"


dependencies {


}

subprojects {

    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }


}