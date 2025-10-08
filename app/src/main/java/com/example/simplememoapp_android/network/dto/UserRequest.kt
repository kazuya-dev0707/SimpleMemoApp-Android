package com.example.simplememoapp_android.network.dto
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable data class UserRequest(
    val email: String,
    val password: String
)
