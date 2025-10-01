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
        return try {
            val newMemo = Memo(
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
    
    /**
     * 指定されたメモをデータベースから削除します。
     * ViewModelからの要求をDAOに仲介します。
     * @param memo 削除対象のMemoオブジェクト。
     * @return 削除処理の結果。成功時はResult.success(Unit)、失敗時はResult.failure(Exception)
     */
    suspend fun delete(memo: Memo): Result<Unit> { // 戻り値を Result<Unit> に変更
        return try { // try-catch の結果を return する
            memoDao.deleteMemo(memo)
            Result.success(Unit) // 成功時は Result.success
        } catch (e: Exception) {
            // エラーハンドリングは呼び出し元に委ねるため、Result.failure で返す
            // println("メモの削除に失敗しました: ${e.message}") // ← この行は削除またはコメントアウト
            Result.failure(e) // 失敗時は Result.failure
        }
    }
}
