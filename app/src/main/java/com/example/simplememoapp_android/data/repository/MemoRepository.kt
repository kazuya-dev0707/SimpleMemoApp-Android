package com.example.simplememoapp_android.data.repository

import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.model.Memo
import kotlinx.coroutines.flow.Flow

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
}