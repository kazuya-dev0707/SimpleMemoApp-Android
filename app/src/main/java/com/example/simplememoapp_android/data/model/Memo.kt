package com.example.simplememoapp_android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "memos")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String, // タイトル（Not Null）
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime // 更新日時
)