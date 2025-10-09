package com.example.simplememoapp_android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
// data/model/Memo.kt (修正後)
@Entity(tableName = "memos")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serverId: String,
    val userId: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)