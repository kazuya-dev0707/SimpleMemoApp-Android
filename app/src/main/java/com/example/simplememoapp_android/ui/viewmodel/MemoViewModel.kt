package com.example.simplememoapp_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.simplememoapp_android.data.model.Memo
import com.example.simplememoapp_android.ui.state.MemoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MemoViewModel : ViewModel() {

    // ViewModelの内部でのみ変更可能なUI状態
    private val _uiState = MutableStateFlow<MemoUiState>(MemoUiState.Empty)

    // UIへは、変更不可能な読み取り専用のStateFlowとして公開する
    val uiState: StateFlow<MemoUiState> = _uiState.asStateFlow()

    /**
     * 新しいメモを追加する
     * @param text 入力されたメモの本文
     */
    fun addMemo(text: String) {
        // 入力チェック：空白文字のみの場合は何もしない
        if (text.isBlank()) {
            return
        }

        val newMemo = Memo(
            text = text.trim() // 前後の空白は除去
        )

        // 現在の状態を「不変的」に更新する
        _uiState.update { currentState ->
            val currentMemos = if (currentState is MemoUiState.Success) {
                currentState.memos
            } else {
                emptyList()
            }
            val newMemos = listOf(newMemo) + currentMemos
            MemoUiState.Success(newMemos)
        }
    }
}