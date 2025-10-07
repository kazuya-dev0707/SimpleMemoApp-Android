package com.example.simplememoapp_android.network

import com.example.simplememoapp_android.network.dto.MemoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users/{userId}/memos")
    suspend fun getMemosForUser(@Path("userId") userId: String): List<MemoResponse>
}