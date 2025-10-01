package com.example.simplememoapp_android.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import com.example.simplememoapp_android.data.model.Memo
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplememoapp_android.MemoApplication
import com.example.simplememoapp_android.ui.state.MemoUiState
import com.example.simplememoapp_android.ui.viewmodel.MemoViewModel
import com.example.simplememoapp_android.ui.viewmodel.MemoViewModelFactory
import com.example.simplememoapp_android.ui.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class) // ← Scaffoldなどで必要なら追加
@Composable
fun MemoScreen() {

    // ContextからApplicationインスタンスを取得し、Repositoryにアクセス
    val application = LocalContext.current.applicationContext as MemoApplication
    val repository = application.repository

    // カスタムファクトリを使ってViewModelを生成
    val viewModel: MemoViewModel = viewModel(
        factory = MemoViewModelFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsState()

    /**
     * Snackbarの状態を管理し、表示を制御するための司令塔。
     */
    val snackbarHostState = remember { SnackbarHostState() }

    /**
     * ViewModelからの単発イベントを監視し、UIに反映させる。
     * この画面が初めて表示された時に一度だけ実行され、イベントを待ち受ける。
     */
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("爆速メモアプリ") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            MemoInputSection(onAddClick = { text ->
                viewModel.addMemo(text)
            })

            when (val state = uiState) {
                // 「ローディング中」の命令が来たら
                is MemoUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator() // クルクル回るインジケータを表示
                    }
                }
                // 「データが空」の命令が来たら
                is MemoUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("メモはありません。最初のメモを追加しよう！")
                    }
                }
                // 「成功（データあり）」の命令が来たら
                is MemoUiState.Success -> {
                    // MemoListSectionを舞台に登場させ、メモのリストを渡す
                    MemoListSection(
                        memos = state.memos,
                        // ここで onDeleteClick の実処理 (ViewModelのメソッド呼び出し) を渡す
                        onDeleteClick = { memo ->
                            viewModel.deleteMemo(memo)
                        }
                    )
                }
                // 「エラー」の命令が来たら
                is MemoUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("エラーが発生しました: ${state.message}")
                    }
                }
            }
        }
    }
}

@Composable
private fun MemoItem(
    memo: Memo,
    onDeleteClick: (Memo) -> Unit // ← これを追加！
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row( // Cardの中身をRowにして、テキストとボタンを横並びにする
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = memo.text,
                modifier = Modifier
                    .weight(1f) // テキストが残りのスペースを全て使う
                    .padding(16.dp)
            )
            // ゴミ箱アイコンのボタンを追加
            IconButton(onClick = { onDeleteClick(memo) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "メモを削除"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMemoItem() {
    // createdAt をダミーデータに追加
    MemoItem(
        memo = Memo(
            id = 1, // プレビュー用に適当なID
            text = "これはプレビュー用のメモです！",
            createdAt = LocalDateTime.now()
        ),
        onDeleteClick = { clickedMemo ->
            println("プレビュー: メモ削除ボタンクリック: ${clickedMemo.text}")
        }
    )
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
private fun MemoListSection(
    memos: List<Memo>,
    onDeleteClick: (Memo) -> Unit // onDeleteClick を引数に追加
) {
    // ① 大量のアイテムを効率的に表示するためのリスト
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // ② memosリストの中身を一つずつ取り出して表示
        items(memos) { memo ->
            MemoItem(
                memo = memo,
                onDeleteClick = onDeleteClick // MemoItemにコールバックを渡す
            ) 
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
            Memo(id = 1, text = "キックボクシングに行く", createdAt = LocalDateTime.now()),
            Memo(id = 2, text = "タリーズで実装する", createdAt = LocalDateTime.now()),
            Memo(id = 3, text = "夜はジムで筋トレ", createdAt = LocalDateTime.now())
        )
        MemoListSection(
            memos = dummyMemos,
            onDeleteClick = { memo ->
                println("プレビュー (List): メモ削除ボタンクリック: ${memo.text}")
            }
        )
    }
}
