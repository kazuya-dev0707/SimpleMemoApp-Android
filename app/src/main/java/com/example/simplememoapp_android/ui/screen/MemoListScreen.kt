package com.example.simplememoapp_android.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDateTime
import com.example.simplememoapp_android.data.model.Memo
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.simplememoapp_android.ui.state.MemoUiState
import com.example.simplememoapp_android.ui.viewmodel.MemoListViewModel
import com.example.simplememoapp_android.ui.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoListScreen(navController: NavController) {
    // ★★★ ViewModelの取得が、この一行だけで完了！ ★★★
    val viewModel: MemoListViewModel = hiltViewModel()

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
                else -> {
                    // MemoListScreenでは他のイベントは処理しない
                }
            }
        }
    }

    val context = LocalContext.current

    // ★ファイル作成ランチャーを登録
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri: Uri? ->
            uri?.let {
                viewModel.exportMemos(it, context.contentResolver)
            }
        }
    )


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("爆速メモアプリ") },
                actions = {
                    var menuExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "メニュー")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("全件エクスポート") },
                            onClick = {
                                menuExpanded = false
                                createFileLauncher.launch("memos.json")
                            }
                        )
                    }
                },
            )
        },
        // ★ フローティングアクションボタンを追加
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // 新規作成画面へ遷移 (-1Lは新規作成を示すID)
                navController.navigate("memo_detail/-1L")
            }) {
                Icon(Icons.Default.Add, contentDescription = "新しいメモ")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
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
                        onDeleteClick = {
                            viewModel.deleteMemo(it)
                        },
                        // ★ メモアイテムをタップしたときの処理を追加
                        onMemoClick = { memo ->
                                navController.navigate("memo_detail/${memo.id}")
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

// 部品3：メモのリスト全体
@Composable
private fun MemoListSection(
    memos: List<Memo>,
    onDeleteClick: (Memo) -> Unit,
    onMemoClick: (Memo) -> Unit // ★ onMemoClickを追加
) {
    // ① 大量のアイテムを効率的に表示するためのリスト
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // ② memosリストの中身を一つずつ取り出して表示
        items(memos) { memo ->
            MemoItem(
                memo = memo,
                onDeleteClick = onDeleteClick,
                onMemoClick = onMemoClick // ★ onMemoClickを渡す
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
            Memo(
                id = 1,
                serverId = "server1",
                userId = "user1",
                title = "キックボクシング",
                content = "キックボクシングに行く",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            Memo(
                id = 2,
                serverId = "server2",
                userId = "user1",
                title = "タリーズ",
                content = "タリーズで実装する",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            Memo(
                id = 3,
                serverId = "server3",
                userId = "user1",
                title = "筋トレ",
                content = "夜はジムで筋トレ",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
        MemoListSection(
            memos = dummyMemos,
            onDeleteClick = { memo ->
                println("プレビュー (List): メモ削除ボタンクリック: ${memo.title}")
            },
            onMemoClick = { memo ->
                println("プレビュー (List): メモクリック: ${memo.title}")
            }
        )
    }
}

@Composable
private fun MemoItem(
    memo: Memo,
    onDeleteClick: (Memo) -> Unit,
    onMemoClick: (Memo) -> Unit // ★ onMemoClickを追加
) {
    Card(
        // ★ onClickを追加してカード全体をタップ可能にする
        onClick = { onMemoClick(memo) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row( // Cardの中身をRowにして、テキストとボタンを横並びにする
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = memo.title,
                modifier = Modifier
                    .weight(1f) // テキストが残りのスペースを全て使う
                    .padding(16.dp)
            )
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
            serverId = "server1",
            userId = "user1",
            title = "プレビュー用のメモ",
            content = "これはプレビュー用のメモです！",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ),
        onDeleteClick = { clickedMemo ->
            println("プレビュー: メモ削除ボタンクリック: ${clickedMemo.title}")
        },
        onMemoClick = { clickedMemo ->
            println("プレビュー: メモクリック: ${clickedMemo.title}")
        }
    )
}

// プレビュー用のコード
@Preview(showBackground = true)
@Composable
fun MemoListScreenPreview() {
    // Previewでは実際のナビゲーションは不要なので、ダミーのNavControllerを渡す
    MemoListScreen(navController = rememberNavController())
}
