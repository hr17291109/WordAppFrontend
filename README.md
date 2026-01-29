# WordAppFrontend

# 📖 Word Sharing App

研究室のメンバーやクラスメイトと、論文や学習で学んだ英単語を共有するためのAndroidアプリケーションです。
個人の単語帳としてだけでなく、グループ全員の単語リストを閲覧できる共有機能を備えています。

## ✨ 特徴 (Features)

* **ユーザー認証**: 安全なサインアップとサインイン機能。
* **単語管理 (CRUD)**:
    * 英単語と日本語の意味を登録・保存。
    * 自分のリストから不要な単語を削除。
* **共有機能 (Everyone's Words)**:
    * 登録された全ユーザーの単語を一括で閲覧可能。
    * 研究室やチーム内での知識共有に最適。
* **セキュリティ**:
    * パスワードの変更機能。
    * 入力時のパスワード非表示/表示切り替え (Eye Icon)。

## 🛠 使用技術 (Tech Stack)

**Frontend (Android)**
* **Language**: Java
* **IDE**: Android Studio
* **Networking**: [Retrofit2](https://square.github.io/retrofit/)
* **JSON Parsing**: [Jackson](https://github.com/FasterXML/jackson)
* **UI Components**:
    * `ConstraintLayout`
    * `RecyclerView`
    * `TextInputLayout` (Material Components)

**Backend**
* **Language**: Java (Jakarta EE / Spring Boot)
* **Architecture**: REST API

## ⚙️ セットアップ (Setup)

### 前提条件
* Android Studio がインストールされていること。
* バックエンドサーバーがローカル (`localhost:8080`) で稼働していること。

### エミュレーター設定 (重要)
日本語を入力するために、エミュレーター側で以下の設定が必要です。
1.  **Settings** > **System** > **Languages & input**
2.  **On-screen keyboard** > **Gboard** > **Languages** で「Japanese」を追加。

### インストール手順
1.  リポジトリをクローンします。
    ```bash
    git clone [https://github.com/](https://github.com/)[your-username]/WordAppFrontend.git
    ```
2.  Android Studio でプロジェクトを開きます。
3.  `Run` ボタンをクリックしてエミュレーターで起動します。

## 📡 API エンドポイント (API Endpoints)

| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/accounts` | 新規アカウント登録 |
| `GET` | `/accounts/{id}` | ログイン認証 |
| `PUT` | `/accounts/{id}/password` | パスワード変更 |
| `POST` | `/accounts/{id}/words` | 単語追加 |
| `DELETE` | `/accounts/{id}/words` | 単語削除 |
| `GET` | `/accounts/words/all` | 全ユーザーの単語取得 |

## 👤 著者 (Author)

* 新田悠翔
* Affiliation: Kyoto Sangyo University

## 📝 ライセンス (License)

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
