package com.example.simplememoapp_android.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplememoapp_android.data.model.Memo

@Composable
private fun MemoItem(memo: Memo) {
    // Cardで囲むことで、メモが一つのかたまりとして見やすくなる
    Card(
        modifier = Modifier
            .fillMaxWidth() // 横幅いっぱいに広げる
            .padding(horizontal = 16.dp, vertical = 4.dp) // 外側の余白
    ) {
        // Cardの中にTextを配置する
        Text(
            text = memo.text,
            modifier = Modifier.padding(16.dp) // 内側の余白
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMemoItem() {
    MemoItem(memo = Memo(text = "これはプレビュー用のメモです！"))
}

// 部品2：メモ入力エリア
@Composable
private fun MemoInputSection(onAddClick: (String) -> Unit) {
    // ① 入力されたテキストを覚えておくための「記憶」の箱
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ② テキスト入力欄
        TextField(
            value = text,
            onValueChange = { text = it }, // 入力されるたびに記憶を更新
            modifier = Modifier.weight(1f),
            label = { Text("新しいメモ") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        // ③ 追加ボタン
        Button(onClick = {
            onAddClick(text) // 親に「追加ボタンが押されたよ！」と通知
            text = ""        // 通知したら入力欄を空にする
        }) {
            Text("追加")
        }
    }
}

// プレビュー用の魔法
@Preview(showBackground = true)
@Composable
fun PreviewMemoInputSection() {
    com.example.simplememoapp_android.ui.theme.SimpleMemoAppAndroidTheme {
        MemoInputSection(onAddClick = {})
    }
}

// 部品3：メモのリスト全体
@Composable
private fun MemoListSection(memos: List<Memo>) {
    // ① 大量のアイテムを効率的に表示するためのリスト
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // ② memosリストの中身を一つずつ取り出して表示
        items(memos) { memo ->
            MemoItem(memo = memo) // Step1で作った部品をここで使う！
        }
    }
}

// プレビュー用の魔法
@Preview(showBackground = true)
@Composable
fun PreviewMemoListSection() {
    com.example.simplememoapp_android.ui.theme.SimpleMemoAppAndroidTheme {
        // ダミーのデータを用意して、リストがどう見えるか確認する
        val dummyMemos = listOf(
            Memo(text = "キックボクシングに行く"),
            Memo(text = "タリーズで実装する"),
            Memo(text = "夜はジムで筋トレ")
        )
        MemoListSection(memos = dummyMemos)
    }
}
