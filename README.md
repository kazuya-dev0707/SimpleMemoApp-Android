# Simple Memo App (Android)

## 📝 概要 (Overview)
Jetpack Composeの学習用に作成した、モダンなAndroid開発技術を実践するためのシンプルなメモアプリです。
**Roomデータベースを導入し、アプリを閉じてもメモが消えないデータ永続化に対応しています。**

## ✨ 機能 (Features)
- メモの追加機能
- リアルタイムでのリスト表示
- **データ永続化（アプリを再起動してもデータが残る）**

## 📸 スクリーンショット (Screenshots)
<img src="https://private-user-images.githubusercontent.com/232746255/494822233-f100a852-3f6f-4e82-9a7b-e0a20795df80.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTkwMzc2NTYsIm5iZiI6MTc1OTAzNzM1NiwicGF0aCI6Ii8yMzI3NDYyNTUvNDk0ODIyMjMzLWYxMDBhODUyLTNmNmYtNGU4Mi05YTdiLWUwYTIwNzk1ZGY4MC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwOTI4JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDkyOFQwNTI5MTZaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT02YWNlNzhlNWMyNDRkMzE1M2NmMzdiZmYwNDkzMTEzNjEzYWUyMDFlMzFmY2ViYWIwNGQxY2Q2MDVlNmU1NDJlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.nE1JMT0lJxnRsY10BzTnWCbqwc1JjTpkl8oxbkuLPUU" alt="Screenshot of Memo App" width="300"><img src="https://private-user-images.githubusercontent.com/232746255/494822715-4ecfddda-1a52-4204-b03f-b3eec1e4b034.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTkwMzc5NTksIm5iZiI6MTc1OTAzNzY1OSwicGF0aCI6Ii8yMzI3NDYyNTUvNDk0ODIyNzE1LTRlY2ZkZGRhLTFhNTItNDIwNC1iMDNmLWIzZWVjMWU0YjAzNC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwOTI4JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDkyOFQwNTM0MTlaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT03Zjc1MGFlMzlmNGJjZTk1NjdiYzMwZTZkNGExNjA5OTY1Y2Y2N2ExODY0MjRkM2EyM2JjNDQ4ZjdiMWRjNjE5JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.rFyNVhc02BlOS_ouyISKL2dcw-7I7Ls52aerIyx32bQ" alt="Screenshot of Memo App" width="300">

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
