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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
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

    private var memoId: Long = -1L

    init {
        // savedStateHandleから "memoId" をキーにして値を取り出す
        memoId = savedStateHandle.get<Long>("memoId") ?: -1L

        if (memoId != -1L) {
            // 編集モード：DBからメモを取得
            viewModelScope.launch {
                repository.getMemoById(memoId).collect { memo ->
                    _uiState.value = _uiState.value.copy(
                        title = memo.title,
                        content = memo.content,
                        createdAt = memo.createdAt,
                        isLoading = false
                    )
                }
            }
        } else {
            // 新規作成モード
            _uiState.value = MemoDetailUiState(isLoading = false)
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle, isSavable = true)
    }

    fun onContentChange(newContent: String) {
        _uiState.value = _uiState.value.copy(content = newContent, isSavable = true)
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
            val now = LocalDateTime.now()
            val createdAt = uiState.value.createdAt ?: now

            val memoToSave = Memo(
                id = if (memoId != -1L) memoId else 0,
                title = uiState.value.title.trim(),
                content = uiState.value.content.trim(),
                createdAt = createdAt,
                updatedAt = now
            )

            try {
                if (memoId != -1L) {
                    repository.updateMemo(memoToSave)
                } else {
                    repository.addMemo(memoToSave)
                }
                _uiState.value = _uiState.value.copy(isFinished = true) // 保存完了フラグを立てる
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("保存に失敗しました"))
            }
        }
    }
}