package com.example.simplememoapp_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.simplememoapp_android.data.local.dao.MemoDao
import com.example.simplememoapp_android.data.model.Memo
import java.time.LocalDateTime

@Database(entities = [Memo::class], version = 3, exportSchema = false) // ★ versionを3に更新
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao

//TODO ここってこのままでいいの？理解していない
    companion object {
        // ★★★ ここから修正 ★★★
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. 新しいスキーマで一時テーブルを作成
                database.execSQL("""
                    CREATE TABLE memos_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        content TEXT NOT NULL,
                        createdAt TEXT NOT NULL,
                        updatedAt TEXT NOT NULL
                    )
                """.trimIndent())

                // 2. 古いテーブル(memos)から新しいテーブル(memos_new)へデータをコピー
                //    - 古い'text'カラムを新しい'content'カラムにマッピング
                //    - 新しい'title'には空文字を、'updatedAt'には'createdAt'と同じ値を入れておく
                database.execSQL("""
                    INSERT INTO memos_new (id, title, content, createdAt, updatedAt)
                    SELECT id, '', text, createdAt, createdAt FROM memos
                """.trimIndent())

                // 3. 古いテーブルを削除
                database.execSQL("DROP TABLE memos")

                // 4. 新しいテーブルの名前を元の名前に変更
                database.execSQL("ALTER TABLE memos_new RENAME TO memos")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE memos ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE memos ADD COLUMN serverId TEXT NOT NULL DEFAULT ''") // ★ NOT NULLに変更
            }
        }
    }
}