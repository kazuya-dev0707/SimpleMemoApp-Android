package com.example.simplememoapp_android.network

import com.example.simplememoapp_android.network.dto.MemoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("user/{userId}/memo")
    suspend fun getMemosForUser(@Path("userId") userId: String): List<MemoResponse>
}