package com.example.simplememoapp_android.network

import com.example.simplememoapp_android.network.dto.MemoRequest
import com.example.simplememoapp_android.network.dto.MemoResponse
import com.example.simplememoapp_android.network.dto.UserRequest
import com.example.simplememoapp_android.network.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // --- User Endpoints ---
    @GET("user")
    suspend fun getUserByEmail(@Query("email") email: String): List<UserResponse>

    @POST("user")
    suspend fun registerUser(@Body user: UserRequest): UserResponse

    // --- Memo Endpoints ---
    @GET("users/{userId}/memos")
    suspend fun getMemosForUser(@Path("userId") userId: String): List<MemoResponse>

    @POST("users/{userId}/memos")
    suspend fun createMemo(@Path("userId") userId: String, @Body memo: MemoRequest): MemoResponse

    @PUT("users/{userId}/memos/{memoId}")
    suspend fun updateMemo(@Path("userId") userId: String, @Path("memoId") memoId: String, @Body memo: MemoRequest): MemoResponse

    @DELETE("users/{userId}/memos/{memoId}")
    suspend fun deleteMemo(@Path("userId") userId: String, @Path("memoId") memoId: String): MemoResponse
}