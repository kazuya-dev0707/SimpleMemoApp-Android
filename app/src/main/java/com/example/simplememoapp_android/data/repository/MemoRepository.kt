package com.example.simplememoapp_android.data.repository

import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.model.Memo
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class MemoRepository(private val memoDao: MemoDao) {

    val allMemos: Flow<List<Memo>> = memoDao.getAllMemos()

    /**
     * 新しいメモをDBに挿入します。
     * @param text メモの本文
     * @return 挿入処理の結果。成功時はResult.success(Unit)、失敗時はResult.failure(Exception)
     */
    suspend fun insert(text: String): Result<Unit> {
        return try {val newMemo = Memo(
            text = text,
            createdAt = LocalDateTime.now()
        )
            memoDao.insertMemo(newMemo)
            Result.success(Unit)
        } catch (e: Exception) {
            // 必要であればここでログ出力や特定のエラー型への変換を行う
            Result.failure(e)
        }
    }
}
    