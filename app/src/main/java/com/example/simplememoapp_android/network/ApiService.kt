package com.example.simplememoapp_android.network

import com.example.simplememoapp_android.network.dto.MemoResponse
import com.example.simplememoapp_android.network.dto.UserRequest
import com.example.simplememoapp_android.network.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("user/{userId}/memo")
    suspend fun getMemosForUser(@Path("userId") userId: String): List<MemoResponse>

    @GET("user")
    suspend fun getUserByEmail(@Query("email") email: String): List<UserResponse>

    @POST("user")
    suspend fun registerUser(@Body user: UserRequest): UserResponse

}