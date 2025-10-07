package com.example.simplememoapp_android.network.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class MemoResponse(
    val id: String,
    val createdAt: String,
    val title: String,
    val content: String,
    val updatedAt: String,
    val userId: String
)