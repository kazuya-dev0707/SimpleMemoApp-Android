package com.example.simplememoapp_android.data.repository

import android.content.ContentResolver
import android.net.Uri
import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.model.Memo
import com.example.simplememoapp_android.data.model.SerializableMemo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MemoRepository(private val memoDao: MemoDao) {

    // メモ全件をFlowとして取得する
    fun getMemos(): Flow<List<Memo>> = memoDao.getAllMemos()

    // IDを指定して単一のメモをFlowとして取得する
    fun getMemoById(id: Long): Flow<Memo> = memoDao.getMemoById(id)

    // メモを新規追加する
    suspend fun addMemo(memo: Memo) {
        memoDao.insertMemo(memo)
    }

    // メモを更新する
    suspend fun updateMemo(memo: Memo) {
        memoDao.updateMemo(memo)
    }

    // メモを削除する
    suspend fun deleteMemo(memo: Memo) {
        memoDao.deleteMemo(memo)
    }

    suspend fun exportMemosToFile(uri: Uri, contentResolver: ContentResolver) {
        withContext(Dispatchers.IO) {
            val memos = memoDao.getAllMemos().first()
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