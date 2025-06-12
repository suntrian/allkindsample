package com.example.aws.s3.service

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.regex.Pattern

@Service
class DefaultS3Service(
    private val s3Client: S3Client
) : S3Service {

    override fun createBucket(bucketName: String) {
        s3Client.createBucket { it.bucket(bucketName) }
    }

    override fun deleteBucket(bucketName: String) {
        s3Client.deleteBucket { it.bucket(bucketName) }
    }

    override fun listBuckets(): List<String> {
        return s3Client.listBuckets().buckets().map { it.name() }
    }

    override fun uploadObject(
        bucketName: String,
        key: String,
        inputStream: InputStream,
        metadata: Map<String, String>
    ): String {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .apply {
                if (metadata.isNotEmpty()) {
                    metadata(metadata)
                }
            }
            .build()
        val response = s3Client.putObject(putObjectRequest,
            RequestBody.fromInputStream(inputStream, inputStream.available().toLong()))
        return response.eTag()
    }

    override fun downloadObject(bucketName: String, key: String): ByteArray {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return s3Client.getObject(getObjectRequest).readAllBytes()
    }

    override fun deleteObject(bucketName: String, key: String) {
        s3Client.deleteObject { it.bucket(bucketName).key(key) }
    }

    override fun listObjects(bucketName: String): List<String> {
        return listObjectsByPrefix(bucketName, "")
    }

    override fun listObjectsByPrefix(bucketName: String, prefix: String): List<String> {
        val listObjectsRequest = ListObjectsV2Request.builder()
            .bucket(bucketName)
            .prefix(prefix)
            .build()

        return s3Client.listObjectsV2(listObjectsRequest).contents().map { it.key() }
    }

    override fun listObjectsByPattern(bucketName: String, pattern: String): List<String> {
        val regex = Pattern.compile(pattern.replace("*", ".*"))
        return listObjects(bucketName).filter { regex.matcher(it).matches() }
    }

    override fun getObjectMetadata(bucketName: String, key: String): Map<String, String> {
        val headObjectRequest = HeadObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return s3Client.headObject(headObjectRequest).metadata()
    }

    override fun getObjectLastModified(bucketName: String, key: String): java.time.Instant {
        val headObjectRequest = HeadObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return s3Client.headObject(headObjectRequest).lastModified()
    }

    override fun doesObjectExist(bucketName: String, key: String): Boolean {
        return try {
            val headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
            s3Client.headObject(headObjectRequest)
            true
        } catch (e: NoSuchKeyException) {
            false
        }
    }

    override fun enableVersioning(bucketName: String) {
        val versioningConfig = VersioningConfiguration.builder()
            .status(BucketVersioningStatus.ENABLED)
            .build()
        s3Client.putBucketVersioning {
            it.bucket(bucketName)
                .versioningConfiguration(versioningConfig)
        }
    }

    override fun listObjectVersions(bucketName: String, key: String): List<ObjectVersionInfo> {
        val request = ListObjectVersionsRequest.builder()
            .bucket(bucketName)
            .prefix(key)
            .build()
        val response = s3Client.listObjectVersions(request)
        return response.versions()
            .filter { it.key() == key }
            .map { version ->
                ObjectVersionInfo(
                    versionId = version.versionId(),
                    lastModified = version.lastModified(),
                    size = version.size(),
                    isLatest = version.isLatest,
                    isDeleteMarker = false
                )
            }
    }

    override fun downloadObjectVersion(bucketName: String, key: String, versionId: String): ByteArray {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .versionId(versionId)
            .build()
        s3Client.getObject(getObjectRequest).use { input ->
            return input.readAllBytes()
        }
    }

    override fun deleteObjectVersion(bucketName: String, key: String, versionId: String) {
        val deleteRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .versionId(versionId)
            .build()
        s3Client.deleteObject(deleteRequest)
    }

    override fun getCurrentVersionId(bucketName: String, key: String): String? {
        return try {
            val headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
            s3Client.headObject(headObjectRequest).versionId()
        } catch (e: NoSuchKeyException) {
            null
        }
    }
} 