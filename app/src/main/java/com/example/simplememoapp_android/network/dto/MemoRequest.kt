// network/dto/MemoRequest.kt (新規作成)
package com.example.simplememoapp_android.network.dto
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class MemoRequest(
    val title: String,
    val content: String,
    val userId: String
)