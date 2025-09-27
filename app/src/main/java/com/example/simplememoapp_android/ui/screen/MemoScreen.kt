package com.example.simplememoapp_android.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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