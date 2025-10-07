package com.example.simplememoapp_android.network.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class MemoResponse(
    val id: String,
    val createdAt: Long,
    val title: String,
    val content: String,
    val updateAt: Long,
    val userId: String
)