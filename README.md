# 114-2 APP 開發

## name:徐詩雅
## sID:C112181116

## 目錄
- [課程資訊](#課程資訊)
- [課程說明](#課程說明)
- [作業繳交方式](#作業繳交方式)
- [課程進度表](#課程進度表)
- [小考說明](#小考說明)
- [期末專題](#期末專題)
- [課程工具與教材對應表](#課程工具與教材對應表)
- [評分比例](#評分比例)
- [Git 基本流程](#git-基本流程)

---

## 課程資訊
| 項目 | 說明 |
|------|------|
| 課程名稱 | APP 開發 |
| 學期 | 114 學年度第 2 學期 |
| 授課對象 | 大三以上 |
| 每週上課 | 3 小時 |
| 教科書/教材 | 1. [GeeksforGeeks: Java Tutorial](https://www.geeksforgeeks.org/java/java/) (W1-W6)<br>2. 依據課程教學大綱 (W7-W18) |
| 實作環境 | 第 1-6 週：VS Code + Java Extension (或線上編譯器)<br>第 7 週起：本機端 Android Studio、AWS EC2 (後端服務) |

## 課程說明
本課程分為兩大階段。由於設備考量與技術打底，**第一階段（第 1-6 週）將專注於 Java 物件導向程式設計 (OOP) 的核心觀念**，涵蓋類別與物件、封裝、繼承、多型與介面，這將為後續的 App 開發打下堅實的邏輯基礎。**第二階段（第 7 週起）將正式進入 Android App 開發**，涵蓋 UI 佈局、生命週期、資料儲存與網路 API 串接。

課程後期將結合 AWS EC2 建立專屬後端服務，並於第 10 週起導入 AI 輔助開發流程。我們將學習 Prompt Engineering 技巧，掌握如何對 ChatGPT、Copilot 等 AI 工具下達有效指令，輔助程式碼撰寫、架構優化與除錯，核心原則為「先理解再使用」。

## 作業繳交方式

本課程採用業界標準的 **Fork + Pull Request** 流程繳交作業：

1. Fork 本 Repo 到你的 GitHub 帳號（學期初做一次）。
2. 在你的 Fork 中建立每週資料夾（如 `week03/`）並完成作業。
3. 第一次作業完成後發 Pull Request 回本 Repo。
4. 之後每週完成作業只要 push 即可，PR 會自動包含新的 commit。

> 💡 期中前只需要發一次 PR。每週 push 後教師就能在同一個 PR 中看到你的所有作業。
> 期中後會重新發一次 PR，届時會另行通知。

## 課程進度表

### 第一階段：Java OOP 核心基礎 (對應 GeeksforGeeks 教材)
| 週次 | 教學內容 | 對應 GfG 網頁主題與連結 | 作業與專題進度 |
|------|---------|------------------------|---------------|
| **W1** | **課程導論與 Java 環境建置**<br>Git 操作。Java 基本語法：資料型態、變數、運算子。 | - [Java Basics](https://www.geeksforgeeks.org/java-basics/)<br>- [Java Operators](https://www.geeksforgeeks.org/operators-in-java/) | 建立 GitHub 帳號、完成首次 Fork+PR。 |
| **W2** | **流程控制與陣列**<br>If-else、Switch、迴圈 (for/while)、陣列、String 處理。 | - [Java Flow Control](https://www.geeksforgeeks.org/flow-control-in-java/)<br>- [Java Arrays](https://www.geeksforgeeks.org/arrays-in-java/)<br>- [Java String](https://www.geeksforgeeks.org/strings-in-java/) | 作業：實作基礎演算法（如九九乘法表、陣列處理）。 |
| **W3** | **類別與物件 (Classes & Objects)**<br>OOP 精神、定義 Class、建立 Object、屬性、方法、建構子。 | - [Java OOPs Concepts](https://www.geeksforgeeks.org/object-oriented-programming-oops-concept-in-java/)<br>- [Classes & Objects](https://www.geeksforgeeks.org/classes-objects-java/) | **專題分組、建立 Repo**。<br>作業：設計「海洋生物」類別並實例化。 |
| **W4** | **封裝與繼承 (Encapsulation & Inheritance)** + **小考 1**<br>Access Modifiers、`extends`、`super` 關鍵字。 | - [Java Encapsulation](https://www.geeksforgeeks.org/encapsulation-in-java/)<br>- [Java Inheritance](https://www.geeksforgeeks.org/inheritance-in-java/) | **個人專題題目探索**。<br>作業：實作海洋生物繼承架構。 |
| **W5** | **多型 (Polymorphism)**<br>方法多載 (Overloading)、方法覆寫 (Overriding)、`final`。 | - [Java Polymorphism](https://www.geeksforgeeks.org/polymorphism-in-java/) | **決定題目、繳交專題提案**。<br>作業：利用多型實作生物移動方式。 |
| **W6** | **抽象、介面與例外處理**<br>Abstract class、Interface (`implements`)、Try-catch。 | - [Java Abstraction](https://www.geeksforgeeks.org/abstraction-in-java/)<br>- [Java Interfaces](https://www.geeksforgeeks.org/interfaces-in-java/)<br>- [Exception Handling](https://www.geeksforgeeks.org/exceptions-in-java/) | **教師審查提案**。<br>作業：實作可游泳/潛水的 Interface。 |

### 第二階段：Android App 開發與 AI 導入
| 週次 | 教學內容 | 作業與專題進度 |
|------|---------|---------------|
| **W7** | **Android 專案結構與 UI 基礎**<br>安裝 Android Studio、XML 佈局、ConstraintLayout。 | 前期研究：專題資料來源確認。作業：實作專題靜態 UI。 |
| **W8** | **Activity 生命週期與 Intent** + **小考 2**<br>頁面跳轉 (Explicit/Implicit)、資料傳遞。 | 作業：實作多頁面切換與資料傳遞的 App。 |
| **W9** | **期中考**<br>上機實作：Java OOP 邏輯測驗 + Android 基礎 UI 與跳轉。 | |
| **W10** | **動態資料呈現 (RecyclerView) + AI 輔助開發**<br>Adapter 觀念。**導入 Prompt Engineering**。 | 專題模型開發啟動。作業：AI 輔助生成 Adapter 實作列表。 |
| **W11** | **Fragment 與導覽列 + AI 輔助**<br>單一 Activity 多 Fragment 架構、Bottom Navigation。 | 作業：將專案改版為具備底部導覽列的結構。 |
| **W12** | **本機資料儲存 (Room Database)** + **小考 3**<br>SQLite 封裝、Entity 與 DAO 設計。 | 作業：實作收藏夾（加入最愛）功能。 |
| **W13** | **網路連線與 API 串接 (Retrofit)**<br>HTTP 協定、JSON 格式解析。 | 作業：抓取公開 JSON (如氣象) 並顯示於列表。 |
| **W14** | **地圖與定位服務 + AI 輔助**<br>Google Maps API、取得設備經緯度、AI 輔助權限邏輯。 | 作業：實作顯示目前位置與港口標記的地圖。 |
| **W15** | **AWS EC2 後端部署基礎**<br>利用 AWS 建立簡易 Python/Node.js 後端 API 供 App 呼叫。 | 作業：部署 AWS 後端 API 並用 App 呼叫。 |
| **W16** | **上架準備與效能調校** + **小考 4**<br>App 簽名 (Keystore)、混淆 (ProGuard)。 | **專題整合測試**。作業：產生可發布的 APK 檔案。 |
| **W17** | **專題開發與除錯諮詢**<br>課堂提供各組 Debug 諮詢。 | **繳交期末報告與投影片**。 |
| **W18** | **期末專題發表**<br>口頭報告 + 實機 Demo，組間互評。 | **期末專題發表**。 |

## 小考說明
| 次數 | 週次 | 範圍 | 涵蓋重點 |
|------|------|------|---------|
| 小考 1 | 第 4 週 | W1-W3 | Java 基本語法、陣列與迴圈、類別與物件 (Class/Object) |
| 小考 2 | 第 8 週 | W4-W7 | Java 繼承、多型、介面，以及 Android 基礎 XML UI 佈局 |
| 小考 3 | 第 12 週 | W8-W11 | Android 頁面跳轉 (Intent)、RecyclerView 列表實作與 Fragment 切換 |
| 小考 4 | 第 16 週 | W12-W15 | Room 資料庫、Retrofit API 串接、地圖觀念與 AWS 基礎 |

## 評分比例
| 項目 | 比例 |
|------|------|
| 平時作業（每週 Fork + PR） | 20% |
| 小考（4 次） | 20% |
| 期中考 | 20% |
| 期末專題 | 20% |
| 課堂參與 | 20% |

## 期末專題
本課程期末專題將緊扣「**海事與海洋**」領域，請各組發揮創意，開發具備實際應用價值的 Android App。建議結合 App 特有硬體功能（相機、GPS）與網路資料串接。
詳細專題規範與公開資料集（如：政府資料開放平臺、中央氣象署、MarineTraffic 等）請見專題模板 [Repo](https://github.com/pychang-ai/114-2_APPDEV-project-template-/tree/main)。

## 課程工具與教材對應表
| 工具/平台 | 導入週次 | 使用範圍 |
|----------|---------|---------|
| GitHub + Git | 第 1 週 | 全學期作業繳交與版本控制 |
| VS Code / Java 編譯器 | 第 1 週 | 第 1-6 週 Java OOP 邏輯實作 |
| Android Studio | 第 7 週 | 第 7 週起 App 開發 IDE |
| Room Database | 第 12 週 | 本機端資料儲存 |
| Retrofit | 第 13 週 | 網路 API 串接 |
| ChatGPT / Copilot | 第 10 週 | AI 輔助程式開發與除錯 |
| AWS EC2 | 第 15 週 | 雲端後端 API 部署 |
