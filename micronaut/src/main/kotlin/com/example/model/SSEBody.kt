package com.example.model

data class SSEBody<T>(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val data: T? = null,
    val retry: Int? = null
) {

    constructor(data: T?) : this(null, null, null, data, null)

}
