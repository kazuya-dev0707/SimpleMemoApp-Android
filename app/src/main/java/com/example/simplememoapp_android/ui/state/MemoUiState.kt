package com.example.simplememoapp_android.ui.state

import com.example.simplememoapp_android.data.model.Memo


// UIの状態を型安全に表現するためのsealed class
sealed class MemoUiState {
    // ローディング中
    object Loading : MemoUiState()

    // 成功（メモリストを持つ）
    data class Success(val memos: List<Memo>) : MemoUiState()

    // データが空
    object Empty : MemoUiState()

    // エラー（エラーメッセージを持つ）
    data class Error(val message: String) : MemoUiState()
}