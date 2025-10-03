package com.example.simplememoapp_android.ui.state

import java.time.LocalDateTime

data class MemoDetailUiState(
    val title: String = "",
    val content: String = "",
    val createdAt: LocalDateTime? = null,
    val isSavable: Boolean = false, // 保存ボタンが押せるか
    val isLoading: Boolean = true, // データ読み込み中か
    val isFinished: Boolean = false // 処理が完了し画面を閉じるか
)