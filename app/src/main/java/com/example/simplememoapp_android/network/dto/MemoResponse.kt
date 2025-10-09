package com.example.simplememoapp_android.network.dto

import com.example.simplememoapp_android.data.model.Memo
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(InternalSerializationApi::class)
@Serializable
data class MemoResponse(
    val id: String,
    val createdAt: Long,
    val title: String,
    val content: String,
    val updatedAt: Long, // フィールド名を 'updateAt' から 'updatedAt' に修正
    val userId: String
)

// DTOからEntityへの変換関数
fun MemoResponse.toEntity(): Memo {
    // Long型（Unixタイムスタンプ/epoch seconds）をLocalDateTimeに変換します
    val createdAtDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(this.createdAt), ZoneId.systemDefault())
    val updatedAtDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(this.updatedAt), ZoneId.systemDefault())

    return Memo(
        serverId = this.id,
        userId = this.userId,
        title = this.title,
        content = this.content,
        createdAt = createdAtDateTime,
        updatedAt = updatedAtDateTime
    )
}
