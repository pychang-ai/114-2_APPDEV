# W07 Android Studio 入門與程式設計基礎

> **APP 開發課程** ｜ 第 7 週 ｜ 4/9
> **教科書**：Ch01 §1-6 ＋ Ch02 全章 ＋ Ch03 全章

---

## 本週目標

1. 認識 Android 行動作業系統
2. 安裝 Android Studio 並建立第一個專案
3. 學會 XML 佈局與基本元件（TextView、Button、EditText）
4. 理解 Activity 類別的 Java 程式架構
5. 完成使用者輸入與輸出的互動 App

---

## 一、Android 行動作業系統簡介（Ch01 §1-6）

> **PPT：Android_Ch01.pdf P64-67**

### Android 是什麼？

- 基於 Linux 的開放原始碼行動作業系統
- 由 Google 主導開發，2008 年首次發布
- 目前全球市占率超過 70%
- 開發語言：Java / Kotlin

### 為什麼學 Android？

| 優勢 | 說明 |
|------|------|
| 市占率高 | 全球最多人使用的行動作業系統 |
| 開發工具免費 | Android Studio 完全免費 |
| Java 基礎可用 | W1-W5 學的 OOP 直接派上用場 |
| 就業市場需求 | APP 開發是熱門技能 |

---

## 二、開發環境建置（Ch02 全章）

> **PPT：Android_Ch02.pdf 全部 31 頁**

### 2-1 行動裝置軟硬體規格（P2-4）

Android 裝置支援的硬體：相機、GPS、加速感測器、陀螺儀、藍牙、WiFi 等。
不是每種裝置都有完整硬體，開發時需考慮相容性。

### 2-2 開發環境及工具介紹（P5）

Android Studio = Google 官方的 Android 整合開發環境（IDE）

### 2-3 安裝開發環境（實作）

1. 前往 https://developer.android.com/studio 下載最新版
2. 安裝時選擇 **Standard** 設定
3. 等待 SDK 下載完成（約需 10-20 分鐘）

### 2-4 建立第 1 個 Android Studio 專案（實作）

1. 開啟 Android Studio → **New Project**
2. 選擇 **Empty Views Activity**
3. 設定：
   - **Name**：`HelloMarine`
   - **Package name**：`com.example.hellomarine`
   - **Language**：Java
   - **Minimum SDK**：API 24（Android 7.0）
4. 點選 **Finish**，等待 Gradle 同步完成

### 2-5 Android Studio 使用介面（P末段）

| 區域 | 功能 |
|------|------|
| Project 視窗（左側） | 檔案管理，切換 Android / Project 檢視 |
| 編輯器（中央） | 寫 Java 程式碼或編輯 XML |
| Design 工具（中央） | 拖拉 UI 元件設計畫面 |
| Logcat（下方） | 偵錯訊息輸出 |
| Run（上方工具列） | 編譯並執行 App |

---

## 三、Android 程式設計入門（Ch03）

> **PPT：Android_Ch03.pdf 全部 61 頁**

### 3-1 Android 應用程式介紹（P2-6）

Android App 由四大元件組成：

| 元件 | 功能 | 本課程重點 |
|------|------|-----------|
| **Activity（活動）** | 使用者看到的畫面 | ⭐ 主要學習目標 |
| Service（服務） | 背景執行（如播放音樂） | 後期選用 |
| Content Provider | 跨 App 資料共享 | 不涉及 |
| Broadcast Receiver | 接收系統廣播 | 不涉及 |

> 一個 Activity = 一個畫面。App 可以有多個 Activity。

### 3-2 應用程式設計流程（P7-18）

開發一個 Android 畫面的五步驟：

```
Step 1：新增 Android Studio 專案
Step 2：在佈局檔（XML）設計使用介面
Step 3：設定介面元件的屬性（id、text、onClick）
Step 4：在活動類別（Java）新增事件處理方法
Step 5：編譯與執行（Run > Run 'app' 或 Shift+F10）
```

**兩個核心檔案的關係：**

```
activity_main.xml（佈局檔）          MainActivity.java（程式檔）
┌──────────────────────┐           ┌──────────────────────┐
│  用 XML 標籤描述       │           │  用 Java 寫邏輯       │
│  畫面「長什麼樣」       │  ←連結→  │  畫面「做什麼事」       │
│                      │           │                      │
│  <TextView id="tv">  │           │  findViewById(R.id.tv)│
│  <Button onClick="">  │           │  button_Click() {...} │
└──────────────────────┘           └──────────────────────┘
```

### Android 專案結構

```
app/
├── manifests/
│   └── AndroidManifest.xml    ← App 設定檔（權限、Activity 註冊）
├── java/
│   └── com.example.hellomarine/
│       └── MainActivity.java  ← 主程式（Java 邏輯）
└── res/
    ├── layout/
    │   └── activity_main.xml  ← 畫面佈局（XML 描述 UI）
    ├── values/
    │   └── strings.xml        ← 字串資源
    └── drawable/               ← 圖片資源
```

---

### 3-3 建立活動的使用介面實習 — TextView 與 Button（P19-37）

#### 常用元件

| 元件 | 功能 | 常用屬性 |
|------|------|---------|
| `TextView` | 顯示文字 | `id`、`text`、`textSize`、`textStyle` |
| `Button` | 按鈕 | `id`、`text`、`onClick` |

#### 實習：按鈕計數器

**activity_main.xml**：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:gravity="center">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="歡迎來到海洋世界"
        android:textSize="24sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/tvOutput"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="按下按鈕開始探索"
        android:textSize="20sp" />

    <Button
        android:id="@+id/btnCount"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="探索海洋生物" />

    <Button
        android:id="@+id/btnReset"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="重新開始" />

</LinearLayout>
```

**MainActivity.java**：

```java
package com.example.hellomarine;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private int count = 0;  // 計數器變數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // 載入佈局檔

        // 找到元件（用 id 對應 XML）
        TextView tvOutput = findViewById(R.id.tvOutput);
        Button btnCount = findViewById(R.id.btnCount);
        Button btnReset = findViewById(R.id.btnReset);

        // 設定「探索」按鈕點擊事件
        btnCount.setOnClickListener(v -> {
            count++;
            tvOutput.setText("已發現 " + count + " 種海洋生物！");
        });

        // 設定「重新開始」按鈕點擊事件
        btnReset.setOnClickListener(v -> {
            count = 0;
            tvOutput.setText("按下按鈕開始探索");
        });
    }
}
```

#### 關鍵方法解析

| 方法 | 功能 | 範例 |
|------|------|------|
| `setContentView()` | 載入 XML 佈局檔 | `setContentView(R.layout.activity_main)` |
| `findViewById()` | 用 id 找到 XML 中的元件 | `findViewById(R.id.tvOutput)` |
| `setText()` | 設定 TextView 的文字 | `tvOutput.setText("Hello")` |
| `setOnClickListener()` | 設定按鈕點擊時要做的事 | `btn.setOnClickListener(v -> { ... })` |

---

### 3-4 活動類別的 Java 程式檔（P38-49）

#### MainActivity 結構解析

```java
// 1. 匯入必要的套件
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// 2. 繼承 AppCompatActivity（就是 W4 學的繼承！）
public class MainActivity extends AppCompatActivity {

    // 3. 覆寫 onCreate 方法（就是 W5 學的 @Override！）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);          // 呼叫父類別（W4 的 super）
        setContentView(R.layout.activity_main);      // 載入畫面
    }
}
```

#### 觀念連結：Java OOP → Android

| Java OOP（W1-W5） | Android 對應 |
|-------------------|-------------|
| `class` 定義類別 | `public class MainActivity` |
| `extends` 繼承 | `extends AppCompatActivity` |
| `super()` 呼叫父類別 | `super.onCreate(savedInstanceState)` |
| `@Override` 覆寫方法 | `protected void onCreate(...)` |
| `new Object()` 建立物件 | `findViewById()` 取得元件 |
| **Interface（介面）** | `View.OnClickListener` ← 按鈕事件就是 Interface！ |

> W1-W5 學的 Java OOP 在 Android 全部派上用場。

#### R.java 資源對照

```
XML 中的 id               Java 中的對應
android:id="@+id/tvOutput"  →  R.id.tvOutput
res/layout/activity_main.xml →  R.layout.activity_main
res/drawable/shark.png       →  R.drawable.shark
```

> `R` 是 Android 自動產生的資源對照表，不需要手動修改。

---

### 3-5 EditText 資料輸入元件實習（P50-61）

#### EditText 元件

| 元件 | 功能 | 與 TextView 的差別 |
|------|------|-------------------|
| `TextView` | 顯示文字（唯讀） | 程式輸出 |
| `EditText` | 輸入文字（可編輯） | 程式輸入 |

#### 常用 inputType 屬性

| 屬性值 | 說明 | 用途 |
|--------|------|------|
| `text` | 一般文字 | 姓名、描述 |
| `number` | 整數 | 年齡、數量 |
| `numberDecimal` | 浮點數 | 溫度、距離 |
| `phone` | 電話號碼 | 聯絡電話 |

#### 實習：海洋溫度轉換器

**activity_main.xml**：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:gravity="center">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="海洋溫度轉換器"
        android:textSize="24sp"
        android:textStyle="bold" />

    <EditText
        android:id="@+id/etCelsius"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:hint="請輸入攝氏溫度"
        android:inputType="numberDecimal" />

    <Button
        android:id="@+id/btnConvert"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="轉換為華氏" />

    <TextView
        android:id="@+id/tvResult"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:textSize="20sp" />

    <TextView
        android:id="@+id/tvComment"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:textSize="16sp"
        android:textColor="#666666" />

</LinearLayout>
```

**MainActivity.java**：

```java
package com.example.hellomarine;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etCelsius = findViewById(R.id.etCelsius);
        Button btnConvert = findViewById(R.id.btnConvert);
        TextView tvResult = findViewById(R.id.tvResult);
        TextView tvComment = findViewById(R.id.tvComment);

        btnConvert.setOnClickListener(v -> {
            // 取得輸入值並轉換型別
            String input = etCelsius.getText().toString();
            if (input.isEmpty()) {
                tvResult.setText("請輸入溫度！");
                return;
            }

            double celsius = Double.parseDouble(input);
            double fahrenheit = celsius * 9.0 / 5.0 + 32;

            tvResult.setText(String.format("%.1f°C = %.1f°F", celsius, fahrenheit));

            // 根據溫度給出海洋生態評語
            if (celsius < 5) {
                tvComment.setText("深海溫度，適合大王烏賊棲息");
            } else if (celsius < 15) {
                tvComment.setText("冷水域，適合海帶和鮭魚");
            } else if (celsius < 25) {
                tvComment.setText("溫帶海域，生物多樣性豐富");
            } else {
                tvComment.setText("熱帶海域，珊瑚礁的家園");
            }
        });
    }
}
```

#### 關鍵語法

```java
// 取得 EditText 的文字（回傳 Editable，需轉 String）
String input = etCelsius.getText().toString();

// 字串轉數字
int num = Integer.parseInt(input);       // 轉整數
double num = Double.parseDouble(input);  // 轉浮點數

// 格式化輸出
String.format("%.1f°C = %.1f°F", celsius, fahrenheit);
```

---

## 四、課堂練習

### 練習 1：個人資訊 App（基礎）

修改 `HelloMarine` 專案：

1. 標題改為你的名字
2. 新增一個 `TextView` 顯示學號
3. 按鈕點擊後顯示「我是海資三甲的學生！」

### 練習 2：海洋生物問答 App（進階）

建立新專案 `MarineQuiz`：

1. 畫面顯示一個海洋生物的提示（如「我會高速衝刺獵食」）
2. 一個 `EditText` 讓使用者輸入答案（如「鯊魚」）
3. 一個 `Button`「送出答案」
4. 點擊後判斷答對或答錯，顯示結果在 `TextView`

> 這個練習結合了 TextView + EditText + Button + 條件判斷，是下週 Mini APP 的基礎。

---

## 五、本週 PPT 清單

| PPT 檔案 | 使用範圍 | 頁數 |
|---------|---------|------|
| Android_Ch01.pdf | §1-6（P64-67） | 4 頁 |
| Android_Ch02.pdf | 全章（P1-31） | 31 頁 |
| Android_Ch03.pdf | 全章（P1-61） | 61 頁 |

---

## 六、下週預告

W08 將教授**進階佈局設計**（Ch04 ConstraintLayout + LinearLayout）和**事件處理機制**（Ch05），
並運用所有技能完成 **海洋生物小百科 Mini APP**，這是期中考前的最後一份作業。
