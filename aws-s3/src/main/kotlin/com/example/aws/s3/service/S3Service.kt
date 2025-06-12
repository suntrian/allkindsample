package com.example.aws.s3.service

import software.amazon.awssdk.services.s3.model.ObjectAttributes
import java.io.InputStream
import java.nio.file.Path

interface S3Service {
    /**
     * 创建存储桶
     */
    fun createBucket(bucketName: String)

    /**
     * 删除存储桶
     */
    fun deleteBucket(bucketName: String)

    /**
     * 列出所有存储桶
     */
    fun listBuckets(): List<String>

    /**
     * 上传文件
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @param inputStream 输入流
     * @param metadata 元数据
     * @return 对象的ETag
     */
    fun uploadObject(
        bucketName: String,
        key: String,
        inputStream: InputStream,
        metadata: Map<String, String> = emptyMap()
    ): String

    /**
     * 下载文件
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @return 文件内容
     */
    fun downloadObject(bucketName: String, key: String): ByteArray

    /**
     * 删除文件
     * @param bucketName 存储桶名称
     * @param key 对象键
     */
    fun deleteObject(bucketName: String, key: String)

    /**
     * 列出存储桶中的所有对象
     * @param bucketName 存储桶名称
     * @return 对象键列表
     */
    fun listObjects(bucketName: String): List<String>

    /**
     * 列出指定前缀下的所有对象
     * @param bucketName 存储桶名称
     * @param prefix 前缀
     * @return 对象键列表
     */
    fun listObjectsByPrefix(bucketName: String, prefix: String): List<String>

    /**
     * 使用通配符列出对象
     * @param bucketName 存储桶名称
     * @param pattern 通配符模式
     * @return 对象键列表
     */
    fun listObjectsByPattern(bucketName: String, pattern: String): List<String>

    /**
     * 获取对象元数据
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @return 元数据映射
     */
    fun getObjectMetadata(bucketName: String, key: String): Map<String, String>

    /**
     * 获取对象的最后修改时间
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @return 最后修改时间
     */
    fun getObjectLastModified(bucketName: String, key: String): java.time.Instant

    /**
     * 检查对象是否存在
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @return 是否存在
     */
    fun doesObjectExist(bucketName: String, key: String): Boolean

    /** 启用存储桶版本控制 */
    fun enableVersioning(bucketName: String)

    /** 获取对象的所有版本 */
    fun listObjectVersions(bucketName: String, key: String): List<ObjectVersionInfo>

    /** 下载特定版本的对象 */
    fun downloadObjectVersion(bucketName: String, key: String, versionId: String): ByteArray

    /** 删除特定版本的对象 */
    fun deleteObjectVersion(bucketName: String, key: String, versionId: String)

    /** 获取对象的当前版本ID */
    fun getCurrentVersionId(bucketName: String, key: String): String?
} 