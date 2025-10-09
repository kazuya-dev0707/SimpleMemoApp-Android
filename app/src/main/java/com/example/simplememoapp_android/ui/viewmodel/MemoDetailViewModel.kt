package com.example.simplememoapp_android.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplememoapp_android.data.model.Memo
import com.example.simplememoapp_android.data.repository.MemoRepository
import com.example.simplememoapp_android.ui.state.MemoDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoDetailViewModel @Inject constructor(
    private val repository: MemoRepository,
    savedStateHandle: SavedStateHandle // NavControllerから引数を受け取るための仕組み
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val memoId: Long
    private var currentMemo: Memo? = null // ★更新対象のメモを保持する変数

    init {
        // savedStateHandleから "memoId" をキーにして値を取り出す
        memoId = savedStateHandle.get<Long>("memoId") ?: -1L

        if (memoId != -1L) {
            // 編集モード：DBからメモを取得
            viewModelScope.launch {
                repository.memos.first().find { it.id == memoId }?.let { memo ->
                    currentMemo = memo // ★取得したメモを更新処理のために保持しておく
                    _uiState.update {
                        it.copy(
                            title = memo.title,
                            content = memo.content,
                            createdAt = memo.createdAt,
                            isLoading = false
                        )
                    }
                }
            }
        } else {
            // 新規作成モード
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle, isSavable = true) }
    }

    fun onContentChange(newContent: String) {
        _uiState.update { it.copy(content = newContent, isSavable = true) }
    }

    fun saveMemo() {
        // タイトルが空の場合は保存しない
        if (uiState.value.title.isBlank()) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackbar("タイトルを入力してください"))
            }
            return
        }

        viewModelScope.launch {
            val title = uiState.value.title.trim()
            val content = uiState.value.content.trim()

            try {
                _uiState.update { it.copy(isLoading = true) }
                if (memoId != -1L) {
                    // 更新処理
                    currentMemo?.let {
                        repository.updateMemo(it, title, content)
                    }
                } else {
                    // 新規作成処理
                    repository.addMemo(title, content)
                }
                // ★★★ UIが待っているisFinishedフラグをtrueに更新する ★★★
                _uiState.update { it.copy(isFinished = true) }
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("保存に失敗しました: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
