plugins {
    java
    kotlin("jvm")
    id("com.google.protobuf") version "0.9.4"
}

group = "org.example.grpc"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

val grpcVersion = "1.59.0"
val grpcKotlinVersion = "1.3.0"
val protobufVersion = "3.23.4"
val coroutinesVersion = "1.7.3"

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    
    // gRPC
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    
    // Protobuf
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")

    // For grpc-kotlin
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
    
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }
    
    // 从resources目录复制proto文件到标准位置
    generateProtoTasks {
        all().configureEach {
            // 从resources目录复制proto文件
            dependsOn("copyProtoFiles")

            plugins {
                create("grpc")
                create("grpckt")
            }
            builtins {
                create("kotlin")
            }
        }
    }
}

// 任务：从resources目录复制proto文件到标准protobuf目录
tasks.register<Copy>("copyProtoFiles") {
    doFirst {
        println("custom start copy Proto Files")
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    dependsOn(":grpc:processResources")
    from("src/main/resources")
    include("**/*.proto")
    into("build/extracted-protos/main")
    doLast {
        println("custom finish copy Proto Files")
    }
}

tasks.withType<Copy> {
    filesMatching("**/*.proto") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

// 设置protobuf源目录
sourceSets {
    main {
        proto {
            // 使用复制后的proto文件位置
            srcDir("build/extracted-protos/main")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}