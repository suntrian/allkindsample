package com.example.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/hello")
    suspend fun getHello(): Message

    @GET("/events")
    fun getEvents(): Call<ResponseBody>
} 