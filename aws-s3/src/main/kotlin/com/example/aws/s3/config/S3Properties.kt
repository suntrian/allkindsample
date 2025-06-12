package com.example.aws.s3.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.convert.DataSizeUnit
import org.springframework.util.unit.DataSize
import org.springframework.util.unit.DataUnit
import java.time.Duration

@ConfigurationProperties(prefix = "aws.s3")
data class S3Properties(
    val region: String,
    val endpoint: String? = null,
    val accessKey: String,
    val secretKey: String,
    val pathStyleAccess: Boolean = false,
    val connectionTimeout: Duration = Duration.ofSeconds(10),
    val socketTimeout: Duration = Duration.ofSeconds(30),
    val maxConnections: Int = 50,
    val retryMode: String = "standard",
    val maxRetryAttempts: Int = 3,
    val defaultBucket: String? = null,
    val multipartUpload: MultipartUploadConfig = MultipartUploadConfig()
) {
    data class MultipartUploadConfig(
        @DataSizeUnit(DataUnit.BYTES)
        val minimumPartSize: DataSize = DataSize.ofMegabytes(5L), // 5MB
        @DataSizeUnit(DataUnit.BYTES)
        val maximumPartSize: DataSize = DataSize.ofGigabytes(5), // 5GB
        val maxConcurrentRequests: Int = 10
    ) {
        fun getMinimumPartSizeBytes(): Long = minimumPartSize.toBytes()
        fun getMaximumPartSizeBytes(): Long = maximumPartSize.toBytes()
    }
} 