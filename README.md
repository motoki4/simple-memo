# SimpleMemo

SimpleMemo は Kotlin と Jetpack Compose、Room を用いたシンプルなオフラインメモ帳アプリです。端末内にのみデータを保持し、素早くメモを残せます。

## 特徴
- Jetpack Compose + Material 3 によるライト/ダーク対応 UI
- Room データベースを利用したメモの永続化
- メモの作成、編集、スワイプ削除
- MVVM アーキテクチャ（Repository + ViewModel）
- DAO と Repository のユニットテストを同梱

## セットアップ
1. [Android Studio](https://developer.android.com/studio) (Giraffe 以降) をインストールします。
2. このリポジトリをクローンします。
   ```bash
   git clone https://github.com/your-account/simple-memo.git
   ```
3. Android Studio でプロジェクトを開き、必要な SDK をインストールします（`minSdk=24`, `target/compileSdk=34`）。

## ビルド
- デバッグビルド: `./gradlew assembleDebug`
- リリース AAB 生成: `./gradlew bundleRelease`
  - `app/build.gradle.kts` の `release` ビルドタイプでは `minifyEnabled=true` を設定し、`proguard-android-optimize.txt` と `proguard-rules.pro` を使用しています。

## テスト
- DAO テスト: `app/src/test/java/com/k/simplememo/data/NoteDaoTest.kt`
- Repository テスト: `app/src/test/java/com/k/simplememo/data/NoteRepositoryTest.kt`
- 実行コマンド: `./gradlew test`

## アセット
- アプリ用アダプティブアイコン: `app/src/main/res/mipmap-*`
- Google Play 配信用アセット: `play-assets/`

## プライバシーポリシー
`docs/privacy.html` を参照してください。

## ライセンス
このプロジェクトは MIT ライセンスを想定しています。必要に応じて変更してください。
