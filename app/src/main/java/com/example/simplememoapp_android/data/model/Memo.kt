package com.example.simplememoapp_android.data.model

import java.time.LocalDateTime
import java.util.UUID

data class Memo(
    val id: UUID = UUID.randomUUID(),
    val text: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)