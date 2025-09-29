package com.example.simplememoapp_android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "memos")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val createdAt: LocalDateTime // 初期値を削除 (DB側で設定するため)
)