package com.example.aws.s3.service

/** 用于封装对象版本信息的数据类 */
data class ObjectVersionInfo(
    val versionId: String,
    val lastModified: java.time.Instant,
    val size: Long,
    val isLatest: Boolean,
    val isDeleteMarker: Boolean = false
)