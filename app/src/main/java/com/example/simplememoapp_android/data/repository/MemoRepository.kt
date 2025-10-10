package com.example.simplememoapp_android.data.repository

import android.content.ContentResolver
import android.net.Uri
import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.model.Memo
import com.example.simplememoapp_android.data.model.SerializableMemo
import com.example.simplememoapp_android.network.ApiService
import com.example.simplememoapp_android.network.dto.MemoRequest
import com.example.simplememoapp_android.network.dto.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoRepository @Inject constructor(
    private val memoDao: MemoDao,
    private val apiService: ApiService,
    private val userRepository: UserRepository
) {
    val memos: Flow<List<Memo>> = userRepository.loggedInUserIdFlow.flatMapLatest { userId ->
        if (userId.isNullOrBlank()) {
            flowOf(emptyList())
        } else {
            memoDao.getMemosByUserId(userId)
        }
    }

    suspend fun refreshMemos() {
        val userId = userRepository.loggedInUserIdFlow.first()
        if (userId.isNullOrBlank()) return

        try {
            val remoteMemos = apiService.getMemosForUser(userId)
            memoDao.deleteAllByUserId(userId)
            memoDao.insertAll(remoteMemos.map { it.toEntity() })
        } catch (e: Exception) {
            // TODO: エラーをUIに通知する仕組み
        }
    }

    suspend fun addMemo(title: String, content: String) {
        val userId = userRepository.loggedInUserIdFlow.first() ?: return
        val request = MemoRequest(title, content, userId)
        val newMemoFromApi = apiService.createMemo(userId, request)
        memoDao.insertMemo(newMemoFromApi.toEntity())
    }

    suspend fun updateMemo(memo: Memo, title: String, content: String) {
        val serverId = memo.serverId ?: return // 更新対象のメモは必ずサーバーIDを持っている

        val request = MemoRequest(title, content, memo.userId)
        // 1. APIを呼び出してサーバーのデータを更新
        val updatedMemoFromApi = apiService.updateMemo(memo.userId, serverId, request)

        // 2. ★★★ここからが修正箇所★★★
        //    APIからのレスポンスをEntityに変換し、ローカルDBの主キー(id)を維持する
        val updatedEntity = updatedMemoFromApi.toEntity().copy(
            id = memo.id
        )

        // 3. 正しい主キーを持った最新のデータでローカルDBを更新
        memoDao.updateMemo(updatedEntity)
    }

    suspend fun deleteMemo(memo: Memo) {
        val serverId = memo.serverId ?: return // 削除対象のメモは必ずサーバーIDを持っている
        apiService.deleteMemo(memo.userId, serverId)
        memoDao.deleteMemo(memo)
    }

    suspend fun exportMemosToFile(uri: Uri, contentResolver: ContentResolver) {
        withContext(Dispatchers.IO) {
            // ★★★ ここから修正 ★★★
            // 1. 現在ログインしているユーザーのIDを取得
            val userId = userRepository.loggedInUserIdFlow.first()
            if (userId.isNullOrBlank()) {
                // ログインしていない場合はエクスポートしない
                return@withContext
            }

            // 2. そのユーザーのメモだけをDBから取得
            val memos = memoDao.getMemosByUserId(userId).first()
            // ★★★ ここまで修正 ★★★

            val serializableMemos = memos.map { memo ->
                SerializableMemo(
                    title = memo.title,
                    content = memo.content,
                    createdAt = memo.createdAt.toString(),
                    updatedAt = memo.updatedAt.toString()
                )
            }
            val jsonString = Json.encodeToString(serializableMemos)
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.writer().use { it.write(jsonString) }
            }
        }
    }

}
