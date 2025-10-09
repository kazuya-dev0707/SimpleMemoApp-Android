package com.example.simplememoapp_android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplememoapp_android.data.model.Memo
import kotlinx.coroutines.flow.Flow
// data/local/dao/MemoDao.kt (修正後)
@Dao
interface MemoDao {
    @Query("SELECT * FROM memos WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getMemosByUserId(userId: String): Flow<List<Memo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(memos: List<Memo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo: Memo)

    @Query("DELETE FROM memos WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: String)

    @Update
    suspend fun updateMemo(memo: Memo)

    @Delete
    suspend fun deleteMemo(memo: Memo)
}