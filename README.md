# Simple Memo App (Android)

## 📝 概要 (Overview)
Jetpack Composeの学習用に作成した、モダンなAndroid開発技術を実践するためのシンプルなメモアプリです。
Roomデータベースによるデータ永続化に対応しており、メモの追加・削除が可能です。

## ✨ 機能 (Features)
- メモの追加機能
- メモの削除機能
- リアルタイムでのリスト表示
- データ永続化（アプリを再起動してもデータが残る）

## 📸 スクリーンショット (Screenshots)
<img src="https://private-user-images.githubusercontent.com/232746255/494822233-f100a852-3f6f-4e82-9a7b-e0a20795df80.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTkwMzc2NTYsIm5iZiI6MTc1OTAzNzM1NiwicGF0aCI6Ii8yMzI3NDYyNTUvNDk0ODIyMjMzLWYxMDBhODUyLTNmNmYtNGU4Mi05YTdiLWUwYTIwNzk1ZGY4MC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwOTI4JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDkyOFQwNTI5MTZaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT02YWNlNzhlNWMyNDRkMzE1M2NmMzdiZmYwNDkzMTEzNjEzYWUyMDFlMzFmY2ViYWIwNGQxY2Q2MDVlNmU1NDJlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.nE1JMT0lJxnRsY10BzTnWCbqwc1JjTpkl8oxbkuLPUU" alt="Screenshot of Memo App" width="200"><<img src="https://private-user-images.githubusercontent.com/232746255/495984786-497a9a45-ab4d-47e9-a8aa-6dae9a4b4ac6.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTkyOTE1MjcsIm5iZiI6MTc1OTI5MTIyNywicGF0aCI6Ii8yMzI3NDYyNTUvNDk1OTg0Nzg2LTQ5N2E5YTQ1LWFiNGQtNDdlOS1hOGFhLTZkYWU5YTRiNGFjNi5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUxMDAxJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MTAwMVQwNDAwMjdaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT03NjFiNTBlMzFlY2NjMTZhNWQ3MmNjYTA1OTVhOTQwYWVkZTg0MzkyMzdiNThkNzIwZWYxNGUwN2NlZTVjOTFjJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.iXnvK9ysbP6cud2NFDtPsA39glbd0RRBM0YGNQsCRjQ" alt="Screenshot of Memo App" width="200">





## 🏛️ アーキテクチャ (Architecture)
このアプリは、Googleが推奨するモダンなAndroidアプリの設計に基づいています。

- **MVVM (Model-View-ViewModel) パターン**
- **Repository パターン**によるデータソースの抽象化
- **UI State**を用いた、単一方向のデータフロー (UDF)

```text
       [ ユーザー操作 (Event) ]
              ↓
+-----------------------------+       +--------------------------------+
|         UI層 (View)         |       |       ViewModel層 (頭脳)       |
|-----------------------------|       |--------------------------------|
|        MemoScreen.kt        | ----> |        MemoViewModel.kt        |
| (UIの表示とイベント通知)    |       | (UI Stateの管理、ロジック実行) |
+-----------------------------+       +----------------|---------------+
              ↑                                        ↓ (データ要求)
       [ UIの状態 (UiState) ]                          ↓
                                      +----------------|---------------+
                                      |      Repository層 (データの司書)   |
                                      |--------------------------------|
                                      |       MemoRepository.kt        |
                                      | (データソースの抽象化)         |
                                      +----------------|---------------+
                                                       ↓ (DB操作)
                                                       ↓
+------------------------------------------------------|------------------------------------------------------+
|                                     データ層 (Model)                                                      |
|-------------------------------------------------------------------------------------------------------------|
| [ Room Database ]                                                                                           |
|   AppDatabase.kt (DB本体) --- owns ---> MemoDao.kt (操作マニュアル) --- operates on ---> Memo.kt (テーブル設計図) |
+-------------------------------------------------------------------------------------------------------------+
```

## 🛠️ 使用技術 (Tech Stack)
- **UI:** Jetpack Compose
- **言語:** Kotlin
- **アーキテクチャ:** MVVM, Repository Pattern
- **データベース:** Room
- **非同期処理:** Kotlin Coroutines & StateFlow
- **バージョン管理:** Git / GitHub

## 🏗️ 構築方法 (How to Build)
1. このリポジトリをクローンします。
   `git clone https://github.com/kazuya-dev0707/SimpleMemoApp-Android.git`
2. Android Studioでプロジェクトを開きます。
3. GradleのSyncが完了したら、アプリを実行します。
