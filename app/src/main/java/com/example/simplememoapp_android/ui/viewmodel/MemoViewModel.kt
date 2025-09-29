package com.example.simplememoapp_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplememoapp_android.data.model.Memo
import com.example.simplememoapp_android.data.repository.MemoRepository
import com.example.simplememoapp_android.ui.state.MemoUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MemoViewModel(private val repository: MemoRepository) : ViewModel() {

    // Repositoryから流れてくる`Flow<List<Memo>>`を、UIが表示すべき`StateFlow<MemoUiState>`に変換する
    val uiState: StateFlow<MemoUiState> = repository.allMemos
        .map { memos ->
            if (memos.isEmpty()) {
                MemoUiState.Empty
            } else {
                MemoUiState.Success(memos)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = MemoUiState.Loading
        )

    /**
     * 新しいメモを追加します。
     * Repositoryからの処理結果（成功・失敗）を受け取り、UIに反映します。
     * @param text 追加するメモの本文。
     */
    fun addMemo(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            repository.insert(text.trim())
                .onFailure { e ->
                    // 失敗時はログに出力する（エラーUIへの反映は今後の課題）
                    println("メモの挿入に失敗しました: ${e.message}")
                }
        }
    }
}