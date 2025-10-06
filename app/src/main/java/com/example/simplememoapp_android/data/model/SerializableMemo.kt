package com.example.simplememoapp_android.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.OptIn

@OptIn(InternalSerializationApi::class) // ★こちらが推奨される修正です
@Serializable
data class SerializableMemo(
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
)