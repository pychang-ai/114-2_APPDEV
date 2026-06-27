# W13 取得 Activity 的回傳資料（Ch09-5）

> **APP 開發課程** ｜ 第 13 週 ｜ 5/21
> **教科書**：Ch09 Activity 進階互動
> **單元**：9-5 取得活動的回傳資料
> **課後作業繳交期限**：W14 上課前（5/27）
> **進階補充**：📖 [課後進階練習（選項）— 相機、權限、多合約](advanced.md)

---

## 學習目標

1. 理解「雙向資料傳遞」的場景——何時需要從第二個畫面拿回資料
2. 認識已棄用的舊版 API：`startActivityForResult()` + `onActivityResult()`（了解即可）
3. 掌握現代做法：`ActivityResultLauncher` + `registerForActivityResult()`
4. 完整實作**港口入出港通報系統**：從表單 Activity 接收填寫結果
5. 正確處理使用者「確認」與「取消」兩種回傳情境

---

## 一、為什麼需要「回傳資料」？

### 1-1 單向 vs 雙向資料傳遞

W08 學到的 Intent + `putExtra` 是**單向**傳遞：

```
MainActivity ──putExtra──→ DetailActivity
（傳出去，不管結果）
```

但很多場景需要**雙向**：開啟第二個畫面讓使用者填寫後，把結果帶回來：

```
MainActivity ──啟動──→ ReportActivity（填寫表單）
MainActivity ←──回傳── ReportActivity（按確認後）
```

### 1-2 常見使用場景

| 場景 | 說明 |
|------|------|
| 表單填寫後確認 | 開啟填寫頁，確認後把填好的資料帶回主頁 |
| 相片選取 | 開啟相簿或相機，取得使用者選的圖片 |
| 位置選擇 | 開啟地圖讓使用者點選位置，回傳座標 |
| 登入/授權 | 開啟登入頁，成功後回傳 token |

---

## 二、舊版 API（已棄用，了解即可）

> **注意**：以下 API 在 API Level 29（Android 10）後已標記棄用（deprecated）。  
> 了解它有助於讀懂舊的教科書和網路範例，但**新專案請用第三節的現代 API**。

### 2-1 startActivityForResult（舊版啟動）

```java
// ❌ 舊寫法（已棄用，僅供了解）
// 第二個參數 requestCode：區分不同請求來源
Intent intent = new Intent(this, ReportActivity.class);
startActivityForResult(intent, 100);  // requestCode = 100
```

### 2-2 onActivityResult（舊版接收）

```java
// ❌ 舊寫法（已棄用，僅供了解）
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 100 && resultCode == RESULT_OK) {
        String shipName = data.getStringExtra("SHIP_NAME");
        // 處理回傳資料...
    }
}
```

**舊版問題**：
- `requestCode` 管理混亂，多個請求容易搞錯
- 業務邏輯散落在一個巨大的 `onActivityResult` 方法中
- 不支援 Lambda，程式碼冗長

---

## 三、現代 API：ActivityResultLauncher

### 3-1 核心概念

現代 Activity Result API 把「啟動」和「接收結果」**合併在同一個物件（Launcher）**中，程式碼更集中、更容易管理：

```java
// ✅ 現代寫法
ActivityResultLauncher<Intent> 啟動器 = registerForActivityResult(
    合約類型,          // 告訴系統要做什麼（啟動 Activity、拍照、請求權限...）
    result -> {        // 結果回來時執行這個
        // 處理結果
    }
);
```

### 3-2 三步驟使用流程

**Step 1：宣告 Launcher（欄位）**

```java
public class MainActivity extends AppCompatActivity {

    // 宣告為欄位（不是局部變數）
    private ActivityResultLauncher<Intent> reportLauncher;

    // ...
}
```

**Step 2：在 onCreate 初始化（必須在 onCreate 裡呼叫）**

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // 初始化 Launcher — 必須在 onCreate 中，不能在點擊事件裡
    reportLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    String shipName = data.getStringExtra("SHIP_NAME");
                    tvStatus.setText("最新通報：" + shipName);
                }
            } else {
                // 使用者按取消或直接返回
                Toast.makeText(this, "未填寫通報", Toast.LENGTH_SHORT).show();
            }
        }
    );
}
```

> ⚠️ **常見錯誤**：如果把 `registerForActivityResult` 寫在按鈕的 `setOnClickListener` 裡，App 會閃退（`IllegalStateException`）。  
> **原因**：這個方法必須在 Activity 的 `STARTED` 狀態之前呼叫，onCreate 是唯一安全位置。

**Step 3：在需要時啟動**

```java
// 在任何地方（按鈕點擊、選單等）呼叫 launch
btnNewReport.setOnClickListener(v -> {
    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
    reportLauncher.launch(intent);
});
```

### 3-3 回傳端（ReportActivity）的設定

被啟動的 Activity 用 `setResult()` + `finish()` 回傳結果：

```java
// 確認回傳
private void submitReport() {
    Intent result = new Intent();
    result.putExtra("SHIP_NAME", etShipName.getText().toString());
    result.putExtra("PORT", etPort.getText().toString());
    setResult(Activity.RESULT_OK, result);  // ← 設定結果
    finish();                                // ← 關閉自己，觸發回傳
}

// 取消回傳
private void cancelReport() {
    setResult(Activity.RESULT_CANCELED);    // ← 不帶資料
    finish();
}
```

| setResult 第一個參數 | 意義 | 呼叫端 resultCode |
|---|---|---|
| `Activity.RESULT_OK` | 成功完成（有資料） | `== Activity.RESULT_OK` |
| `Activity.RESULT_CANCELED` | 取消或未完成 | `!= Activity.RESULT_OK` |

---

## 四、完整實作範例：港口入出港通報系統

> **功能**：主頁顯示最新入出港通報。按「新增通報」開啟填寫表單，送出後自動更新主頁。

### 4-1 專案架構

```
app/
├── java/.../
│   ├── MainActivity.java        ← 主頁（顯示通報、接收回傳）
│   └── ReportActivity.java      ← 通報表單（填寫後回傳資料）
├── res/layout/
│   ├── activity_main.xml
│   └── activity_report.xml
└── AndroidManifest.xml
```

### 4-2 介面設計

**activity_main.xml**（主頁）

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="港口入出港通報系統"
        android:textSize="22sp"
        android:textStyle="bold"
        android:layout_marginBottom="8dp" />

    <!-- 最新通報顯示區 -->
    <TextView
        android:id="@+id/tvLatestReport"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#E8F4FD"
        android:padding="16dp"
        android:text="（尚無通報）"
        android:textSize="16sp"
        android:layout_marginBottom="16dp" />

    <!-- 通報明細 -->
    <TextView
        android:id="@+id/tvShipName"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="船名："
        android:textSize="16sp"
        android:layout_marginBottom="6dp" />

    <TextView
        android:id="@+id/tvPort"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="港口："
        android:textSize="16sp"
        android:layout_marginBottom="6dp" />

    <TextView
        android:id="@+id/tvStatus"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="狀態："
        android:textSize="16sp"
        android:layout_marginBottom="6dp" />

    <TextView
        android:id="@+id/tvNote"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="備註："
        android:textSize="16sp"
        android:layout_marginBottom="24dp" />

    <Button
        android:id="@+id/btnNewReport"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="新增通報"
        android:textSize="18sp"
        android:backgroundTint="#0D6EFD" />

</LinearLayout>
```

**activity_report.xml**（通報表單）

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="新增通報"
            android:textSize="22sp"
            android:textStyle="bold"
            android:layout_marginBottom="20dp" />

        <!-- 船名 -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="船名"
            android:textSize="14sp"
            android:textColor="#666666" />
        <EditText
            android:id="@+id/etShipName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="例：萬海 601"
            android:inputType="text"
            android:layout_marginBottom="16dp" />

        <!-- 港口 -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="港口"
            android:textSize="14sp"
            android:textColor="#666666" />
        <EditText
            android:id="@+id/etPort"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="例：高雄港"
            android:inputType="text"
            android:layout_marginBottom="16dp" />

        <!-- 狀態：入港 / 出港 -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="狀態"
            android:textSize="14sp"
            android:textColor="#666666" />
        <RadioGroup
            android:id="@+id/rgStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:layout_marginBottom="16dp">
            <RadioButton
                android:id="@+id/rbEnter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="入港"
                android:checked="true"
                android:layout_marginEnd="24dp" />
            <RadioButton
                android:id="@+id/rbExit"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="出港" />
        </RadioGroup>

        <!-- 備註 -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="備註（選填）"
            android:textSize="14sp"
            android:textColor="#666666" />
        <EditText
            android:id="@+id/etNote"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="例：攜帶危險品、需吊桿服務..."
            android:inputType="textMultiLine"
            android:lines="3"
            android:gravity="top"
            android:layout_marginBottom="24dp" />

        <!-- 按鈕列 -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <Button
                android:id="@+id/btnCancel"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="取消"
                android:layout_marginEnd="8dp"
                android:backgroundTint="#6C757D" />

            <Button
                android:id="@+id/btnSubmit"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="送出通報"
                android:layout_marginStart="8dp"
                android:backgroundTint="#198754" />

        </LinearLayout>

    </LinearLayout>
</ScrollView>
```

### 4-3 ReportActivity.java（通報表單）

```java
package com.example.portreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    private EditText etShipName, etPort, etNote;
    private RadioGroup rgStatus;
    private RadioButton rbEnter, rbExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // 初始化元件
        etShipName = findViewById(R.id.etShipName);
        etPort     = findViewById(R.id.etPort);
        etNote     = findViewById(R.id.etNote);
        rgStatus   = findViewById(R.id.rgStatus);
        rbEnter    = findViewById(R.id.rbEnter);
        rbExit     = findViewById(R.id.rbExit);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnCancel = findViewById(R.id.btnCancel);

        // 送出通報
        btnSubmit.setOnClickListener(v -> {
            String shipName = etShipName.getText().toString().trim();
            String port     = etPort.getText().toString().trim();

            // 驗證必填欄位
            if (shipName.isEmpty() || port.isEmpty()) {
                Toast.makeText(this, "請填寫船名和港口", Toast.LENGTH_SHORT).show();
                return;
            }

            // 取得選擇的狀態
            String status = rbEnter.isChecked() ? "入港" : "出港";
            String note   = etNote.getText().toString().trim();

            // 打包回傳資料
            Intent result = new Intent();
            result.putExtra("SHIP_NAME", shipName);
            result.putExtra("PORT",      port);
            result.putExtra("STATUS",    status);
            result.putExtra("NOTE",      note.isEmpty() ? "無" : note);

            setResult(Activity.RESULT_OK, result);  // 設定回傳成功
            finish();                                // 關閉表單，觸發回傳
        });

        // 取消
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }
}
```

### 4-4 MainActivity.java（主頁接收回傳）

```java
package com.example.portreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Step 1：宣告 Launcher 欄位
    private ActivityResultLauncher<Intent> reportLauncher;

    private TextView tvLatestReport, tvShipName, tvPort, tvStatus, tvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化顯示元件
        tvLatestReport = findViewById(R.id.tvLatestReport);
        tvShipName     = findViewById(R.id.tvShipName);
        tvPort         = findViewById(R.id.tvPort);
        tvStatus       = findViewById(R.id.tvStatus);
        tvNote         = findViewById(R.id.tvNote);

        // Step 2：在 onCreate 初始化 Launcher（絕對不能放在按鈕點擊裡）
        reportLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // 使用者送出了通報
                    Intent data = result.getData();
                    if (data != null) {
                        String shipName = data.getStringExtra("SHIP_NAME");
                        String port     = data.getStringExtra("PORT");
                        String status   = data.getStringExtra("STATUS");
                        String note     = data.getStringExtra("NOTE");

                        // 更新畫面
                        tvLatestReport.setText("最新通報已更新");
                        tvShipName.setText("船名：" + shipName);
                        tvPort.setText("港口：" + port);
                        tvStatus.setText("狀態：" + status);
                        tvNote.setText("備註：" + note);

                        Toast.makeText(this,
                            shipName + " " + status + " 通報成功",
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 使用者取消，不更新
                    Toast.makeText(this, "通報已取消", Toast.LENGTH_SHORT).show();
                }
            }
        );

        // Step 3：按鈕點擊時啟動表單
        Button btnNewReport = findViewById(R.id.btnNewReport);
        btnNewReport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            reportLauncher.launch(intent);  // 用 launcher.launch 取代 startActivity
        });
    }
}
```

### 4-5 AndroidManifest.xml（記得註冊 ReportActivity）

```xml
<application ...>
    <activity android:name=".MainActivity">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>

    <!-- 新增這行 -->
    <activity android:name=".ReportActivity" />
</application>
```

---

## 五、重點整理

| 概念 | 說明 |
|------|------|
| `ActivityResultLauncher` | 現代「啟動並接收結果」的核心物件 |
| `registerForActivityResult` | 初始化 Launcher，**必須在 onCreate 中呼叫** |
| `ActivityResultContracts.StartActivityForResult()` | 合約類型：啟動 Activity 並取得結果 |
| `launcher.launch(intent)` | 取代舊版 `startActivityForResult` |
| `result.getResultCode()` | 取得回傳狀態（RESULT_OK / RESULT_CANCELED） |
| `result.getData()` | 取得回傳的 Intent（內含 putExtra 的資料） |
| `setResult(RESULT_OK, intent)` | 在被啟動的 Activity 設定回傳內容 |
| `finish()` | 關閉當前 Activity，觸發回傳給呼叫端 |

**資料流向圖**：

```
MainActivity                      ReportActivity
     │                                  │
     │── launch(intent) ──────────────→ │（使用者填寫表單）
     │                                  │
     │                   setResult(OK, data)
     │                        finish()  │
     │← result callback ←──────────────│
     │
     result.getResultCode() == RESULT_OK
     result.getData().getStringExtra("SHIP_NAME")
```

---

## 六、隨堂練習（45 分鐘）

> **目標**：完整實作「港口入出港通報系統」並執行在模擬器或實機上
> **繳交**：截圖放在 `week13/` 資料夾，push 到 Fork

### 練習步驟

**Step 1（5 分）：建立新專案**

建立新 Android 專案，名稱 `PortReport`，包名 `com.example.portreport`，最低 SDK 選 API 24。

**Step 2（10 分）：建立兩個 XML 介面**

- 複製 4-2 節的 `activity_main.xml` 和 `activity_report.xml`
- 新增 `ReportActivity`（右鍵 → New → Activity → Empty Views Activity）
- 確認 `AndroidManifest.xml` 已有兩個 Activity 的宣告

**Step 3（15 分）：實作 ReportActivity.java**

按照 4-3 節完成，驗證重點：
- [ ] 船名和港口為空時顯示 Toast 並阻止送出
- [ ] `setResult(RESULT_OK, result)` 包含四個 Extra
- [ ] 取消按鈕呼叫 `setResult(RESULT_CANCELED)` + `finish()`

**Step 4（15 分）：實作 MainActivity.java**

按照 4-4 節完成，驗證重點：
- [ ] `reportLauncher` 在 `onCreate` 中初始化（不是在按鈕裡）
- [ ] `RESULT_OK` 時更新四個 TextView
- [ ] `RESULT_CANCELED` 時顯示「通報已取消」Toast

**驗收**：執行 App，完成以下操作：

| 步驟 | 操作 | 預期結果 |
|:----:|------|---------|
| 1 | 啟動 App | 主頁顯示「（尚無通報）」 |
| 2 | 按「新增通報」 | 開啟通報表單頁 |
| 3 | 不填資料直接按「送出通報」 | 顯示 Toast 提示，不關閉頁面 |
| 4 | 填入船名「萬海 601」、港口「高雄港」、選「入港」，按送出 | 主頁更新為「萬海 601 入港 通報成功」 |
| 5 | 再按「新增通報」，在表單直接按「取消」 | 主頁資料不變，顯示「通報已取消」 |

**繳交**：截圖兩張
- `main_with_report.png`：主頁顯示通報資料後的畫面
- `report_form.png`：通報表單填寫中的畫面

---

## 七、課後作業：港口通報 App 進階版

> **繳交期限**：W14 上課前（5/27）
> **繳交方式**：Android 專案放在 `week13/` 資料夾，push 到 Fork

### 作業需求

在隨堂練習的基礎上，完成以下三項功能：

**必做（70 分）**

1. **通報記錄清單（30 分）**：將每次回傳的通報資料以字串形式加入 `ArrayList`，用 `ArrayAdapter` + `ListView` 顯示所有歷史通報（每筆格式：`[入港] 萬海601 @ 高雄港`）

2. **傳遞預設值到表單（20 分）**：點「新增通報」時，用 `putExtra` 把目前預設港口（例如「高雄港」）傳給 ReportActivity，讓 `etPort` 預先填入該值

3. **刪除最新通報（20 分）**：主頁加一個「清除最新」Button，按下後清空四個 TextView 並顯示「已清除」Toast

**選做（30 分）**

4. **修改現有通報（30 分）**：點擊 ListView 中某筆通報時，重新開啟 ReportActivity 並預填該筆資料（用 `putExtra` 傳入原本的船名/港口），使用者修改後送回覆蓋原紀錄

### 繳交清單

- [ ] `week13/` 內有完整 Android Studio 專案（或截圖 + 程式碼片段）
- [ ] `main_final.png`：主頁顯示歷史通報列表
- [ ] `report_with_prefill.png`：表單預填港口名稱的畫面
