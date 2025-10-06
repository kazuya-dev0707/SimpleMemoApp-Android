package com.example.simplememoapp_android.ui.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplememoapp_android.data.model.Memo
import com.example.simplememoapp_android.data.repository.MemoRepository
import com.example.simplememoapp_android.ui.state.MemoUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}

class MemoListViewModel(private val repository: MemoRepository) : ViewModel() {

    val uiState: StateFlow<MemoUiState> = repository.getMemos()
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

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var exportJob: Job? = null

    fun exportMemos(uri: Uri, contentResolver: ContentResolver) {
        if (exportJob?.isActive == true) return

        exportJob = viewModelScope.launch {
            try {
                // TODO: ローディング表示を開始する
                repository.exportMemosToFile(uri, contentResolver)
                _eventFlow.emit(UiEvent.ShowSnackbar("メモのエクスポートが完了しました。"))
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("エクスポートに失敗しました: ${e.message}"))
            } finally {
                // TODO: ローディング表示を解除する
            }
        }
    }

    fun deleteMemo(memo: Memo) {
        viewModelScope.launch {
            try {
                repository.deleteMemo(memo)
                _eventFlow.emit(UiEvent.ShowSnackbar("メモを削除しました"))
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("メモの削除に失敗しました"))
            }
        }
    }
}