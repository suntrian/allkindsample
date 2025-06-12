package com.example.aws.s3.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.retry.RetryMode
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
@EnableConfigurationProperties(S3Properties::class)
class S3Configuration {

    @Bean
    fun s3Client(properties: S3Properties): S3Client {
        val httpClient = ApacheHttpClient.builder()
            .connectionTimeout(properties.connectionTimeout)
            .socketTimeout(properties.socketTimeout)
            .maxConnections(properties.maxConnections)
            .build()

        val credentials = AwsBasicCredentials.create(properties.accessKey, properties.secretKey)
        val credentialsProvider = StaticCredentialsProvider.create(credentials)

        val builder = S3Client.builder()
            .region(Region.of(properties.region))
            .credentialsProvider(credentialsProvider)
            .httpClient(httpClient)
            .overrideConfiguration {
//                it.retryStrategy(RetryMode.valueOf(properties.retryMode))
                it.retryStrategy{builder -> builder.maxAttempts(properties.maxRetryAttempts) }
            }

        properties.endpoint?.let {
            builder.endpointOverride(java.net.URI.create(it))
        }

        if (properties.pathStyleAccess) {
            builder.forcePathStyle(true)
        }

        return builder.build()
    }
} 