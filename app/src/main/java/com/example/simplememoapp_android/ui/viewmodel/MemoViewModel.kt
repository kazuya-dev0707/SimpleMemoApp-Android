package com.example.simplememoapp_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplememoapp_android.data.model.Memo
import com.example.simplememoapp_android.data.repository.MemoRepository
import com.example.simplememoapp_android.ui.state.MemoUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * UIに一度だけ通知したいイベントを定義するインターフェース。
 */
sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    // 今後「画面遷移しろ」などのイベントもここに追加できる
}

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
     * UIイベントを通知するための専用パイプライン。
     */
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


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
                    // ▼▼▼ 失敗時にイベントを発行するよう変更 ▼▼▼
                    _eventFlow.emit(UiEvent.ShowSnackbar("メモの保存に失敗しました"))
                    println("メモの挿入に失敗しました: ${e.message}")
                }
        }
    }

    /**
     * 指定されたメモを削除するようRepositoryに依頼します。
     * DB操作はUIスレッドをブロックしないよう、viewModelScopeで実行します。
     * @param memo 削除対象のMemoオブジェクト。
     */
    fun deleteMemo(memo: Memo) { 
        viewModelScope.launch {
            repository.delete(memo)
                .onFailure { e -> // ← 失敗時の処理を追加
                    // ▼▼▼ 失敗時にイベントを発行するよう変更 ▼▼▼
                    _eventFlow.emit(UiEvent.ShowSnackbar("メモの削除に失敗しました"))
                    println("メモの削除に失敗しました: ${e.message}")
                }
        }
    }
    
}