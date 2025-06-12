package com.example.aws.s3.service

import com.example.aws.s3.S3ExampleApplication
import io.minio.MinioClient
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import software.amazon.awssdk.services.s3.model.BucketVersioningStatus
import software.amazon.awssdk.services.s3.model.VersioningConfiguration
import java.io.ByteArrayInputStream
import java.time.Instant
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

@SpringBootTest(classes = [S3ExampleApplication::class])
@Testcontainers
class S3ServiceTest {

    companion object {
        private val logger = LoggerFactory.getLogger(S3ServiceTest::class.java)

        @Container
        private val minioContainer = GenericContainer("minio/minio:latest")
            .withExposedPorts(9000)
            .withEnv("MINIO_ROOT_USER", "minioadmin")
            .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
            .withCommand("server /data")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("aws.s3.endpoint") { "http://localhost:${minioContainer.getMappedPort(9000)}" }
            registry.add("aws.s3.region") { "us-east-1" }
            registry.add("aws.s3.access-key") { "minioadmin" }
            registry.add("aws.s3.secret-key") { "minioadmin" }
//            registry.add("aws.s3.endpoint") { "http://localhost:8998" }
//            registry.add("aws.s3.access-key") { "root" }
//            registry.add("aws.s3.secret-key") { "qwer1234" }
            registry.add("aws.s3.path-style-access") { true }
            logger.info("config properties: {}", registry)
        }
    }

    @Autowired
    private lateinit var s3Service: S3Service
    private val testBucket = "test-bucket"
    private val testKey = "test/object.txt"
    private val testContent = "Hello, S3!".toByteArray()

    @BeforeEach
    fun setup() {
        try {
            s3Service.createBucket(testBucket)
        } catch (e: Exception) {
            logger.error("create bucket {} error", testBucket, e)
        }
    }

    @AfterEach
    fun cleanup() {
        try {
            s3Service.deleteBucket(testBucket)
        } catch (e: Exception) {
            logger.error("delete bucket {} error", testBucket, e)
        }
    }

    @Test
    fun `test bucket operations`() {
        // Test bucket creation
        val newBucket = "new-bucket"
        s3Service.createBucket(newBucket)
        assertTrue(s3Service.listBuckets().contains(newBucket))

        // Test bucket deletion
        s3Service.deleteBucket(newBucket)
        assertFalse(s3Service.listBuckets().contains(newBucket))
    }

    @Test
    fun `test object operations`() {
        // Test upload
        val eTag = s3Service.uploadObject(
            testBucket,
            testKey,
            ByteArrayInputStream(testContent)
        )
        assertTrue(eTag.isNotEmpty())

        // Test download
        val downloadedContent = s3Service.downloadObject(testBucket, testKey)
        assertContentEquals(testContent, downloadedContent)

        // Test object existence
        assertTrue(s3Service.doesObjectExist(testBucket, testKey))

        // Test metadata
        val metadata = mapOf("content-type" to "text/plain")
        s3Service.uploadObject(
            testBucket,
            testKey,
            ByteArrayInputStream(testContent),
            metadata
        )
        assertEquals(metadata, s3Service.getObjectMetadata(testBucket, testKey))

        // Test last modified time
        val lastModified = s3Service.getObjectLastModified(testBucket, testKey)
        assertTrue(lastModified.isBefore(Instant.now()))

        // Test delete
        s3Service.deleteObject(testBucket, testKey)
        assertFalse(s3Service.doesObjectExist(testBucket, testKey))
    }

    @Test
    fun `test listing objects`() {
        // Upload multiple objects
        val objects = listOf(
            "folder1/file1.txt",
            "folder1/file2.txt",
            "folder2/file3.txt",
            "test.txt"
        )
        objects.forEach { key ->
            s3Service.uploadObject(
                testBucket,
                key,
                ByteArrayInputStream(testContent)
            )
        }

        // Test list all objects
        val allObjects = s3Service.listObjects(testBucket)
        assertEquals(objects.size, allObjects.size)
        assertTrue(allObjects.containsAll(objects))

        // Test list by prefix
        val folder1Objects = s3Service.listObjectsByPrefix(testBucket, "folder1/")
        assertEquals(2, folder1Objects.size)
        assertTrue(folder1Objects.containsAll(listOf("folder1/file1.txt", "folder1/file2.txt")))

        // Test list by pattern
        val txtFiles = s3Service.listObjectsByPattern(testBucket, "*.txt")
        assertEquals(4, txtFiles.size)
        assertTrue(txtFiles.containsAll(objects))

        objects.forEach {
            s3Service.deleteObject(testBucket, it)
        }
        val listObjects = s3Service.listObjects(testBucket)
        assertTrue { listObjects.isEmpty() }
    }

    @Test
    fun `test last modified time update`() {
        // Upload initial object
        s3Service.uploadObject(
            testBucket,
            testKey,
            ByteArrayInputStream(testContent)
        )
        val initialLastModified = s3Service.getObjectLastModified(testBucket, testKey)

        // Wait a moment
        Thread.sleep(1000)

        // Upload same object again
        s3Service.uploadObject(
            testBucket,
            testKey,
            ByteArrayInputStream(testContent)
        )
        val updatedLastModified = s3Service.getObjectLastModified(testBucket, testKey)

        // Verify last modified time was updated
        assertTrue(updatedLastModified.isAfter(initialLastModified))

        s3Service.deleteObject(testBucket, testKey)
        assertFalse { s3Service.doesObjectExist(testBucket, testKey) }
    }

    @Test
    fun `test object versioning operations`() {
        // 1. 启用存储桶版本控制
        s3Service.enableVersioning(testBucket)
        
        // 2. 上传初始版本
        val version1Content = "Version 1 content".toByteArray()
        s3Service.uploadObject(
            testBucket,
            testKey,
            ByteArrayInputStream(version1Content)
        )
        val version1Id = s3Service.getCurrentVersionId(testBucket, testKey)
        assertNotNull(version1Id, "Version ID should not be null")
        
        // 3. 更新文件内容（创建第二个版本）
        val version2Content = "Version 2 content".toByteArray()
        s3Service.uploadObject(
            testBucket,
            testKey,
            ByteArrayInputStream(version2Content)
        )
        val version2Id = s3Service.getCurrentVersionId(testBucket, testKey)
        assertNotNull(version2Id, "Version ID should not be null")
        assertTrue(version2Id != version1Id, "Version IDs should be different")
        
        // 4. 再次更新文件内容（创建第三个版本）
        val version3Content = "Version 3 content".toByteArray()
        s3Service.uploadObject(
            testBucket,
            testKey,
            ByteArrayInputStream(version3Content)
        )
        val version3Id = s3Service.getCurrentVersionId(testBucket, testKey)
        assertNotNull(version3Id, "Version ID should not be null")
        assertTrue(version3Id != version2Id, "Version IDs should be different")
        
        // 5. 获取所有版本
        val versions = s3Service.listObjectVersions(testBucket, testKey)
        assertEquals(3, versions.size, "Should have 3 versions")
        
        // 6. 验证版本顺序（最新的版本应该在列表最前面）
        val latestVersion = versions.first()
        assertTrue(latestVersion.isLatest, "First version should be the latest")
        assertEquals(version3Id, latestVersion.versionId, "Latest version ID should match")
        
        // 7. 下载并验证各个版本的内容
        val downloadedVersion1 = s3Service.downloadObjectVersion(testBucket, testKey, version1Id)
        val downloadedVersion2 = s3Service.downloadObjectVersion(testBucket, testKey, version2Id)
        val downloadedVersion3 = s3Service.downloadObjectVersion(testBucket, testKey, version3Id)
        
        assertContentEquals(version1Content, downloadedVersion1, "Version 1 content should match")
        assertContentEquals(version2Content, downloadedVersion2, "Version 2 content should match")
        assertContentEquals(version3Content, downloadedVersion3, "Version 3 content should match")
        
        // 8. 验证当前版本（不指定版本ID时）应该是最新版本
        val currentContent = s3Service.downloadObject(testBucket, testKey)
        assertContentEquals(version3Content, currentContent, "Current content should be version 3")
        
        // 9. 删除特定版本
        s3Service.deleteObjectVersion(testBucket, testKey, version2Id)
        
        // 10. 验证删除后的版本列表
        val remainingVersions = s3Service.listObjectVersions(testBucket, testKey)
        assertEquals(2, remainingVersions.size, "Should have 2 versions after deletion")
        assertFalse(
            remainingVersions.any { it.versionId == version2Id },
            "Version 2 should be deleted"
        )
        
        // 11. 验证删除的版本无法访问
        val exception = assertThrows(Exception::class.java) { s3Service.downloadObjectVersion(testBucket, testKey, version2Id) }
        s3Service.deleteObjectVersion(testBucket, testKey, version3Id)
        val downloadObject = s3Service.downloadObject(testBucket, testKey)
        assertContentEquals(version1Content, downloadObject)
        s3Service.deleteObjectVersion(testBucket, testKey, version1Id)

    }
} 