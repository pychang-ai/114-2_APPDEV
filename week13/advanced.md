# W13 進階補充：相機拍照、權限申請、多合約管理

> **課後進階練習（選項）** — 不繳交、不計分
> 適合已完成正課內容、想探索 ActivityResultLauncher 更多應用場景的同學

---

## 一、ActivityResultContracts 合約總覽

`registerForActivityResult` 的第一個參數是**合約（Contract）**，不同合約對應不同功能：

| 合約 | 用途 | 輸入型別 | 回傳型別 |
|------|------|----------|----------|
| `StartActivityForResult()` | 啟動任意 Activity（通用） | `Intent` | `ActivityResult` |
| `TakePicture()` | 拍照存到指定 Uri | `Uri` | `Boolean`（是否拍攝成功） |
| `PickVisualMedia()` | 從相簿選圖/影片（推薦） | `PickVisualMediaRequest` | `Uri?` |
| `RequestPermission()` | 申請單一執行時權限 | `String` | `Boolean`（是否授權） |
| `RequestMultiplePermissions()` | 申請多個執行時權限 | `Array<String>` | `Map<String,Boolean>` |
| `GetContent()` | 從裝置選任意檔案 | `String`（MIME type） | `Uri?` |

---

## 二、拍照並顯示（TakePicture 合約）

### 場景

船舶通報系統延伸：通報時可附上船舶照片。

### 實作步驟

**Step 1：build.gradle 確認 dependencies（通常已有）**

```groovy
dependencies {
    implementation "androidx.activity:activity:1.7.0"  // 或更新版
}
```

**Step 2：宣告並初始化 Launcher**

```java
private ActivityResultLauncher<Uri> cameraLauncher;
private Uri photoUri;
private ImageView ivShipPhoto;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_report);

    ivShipPhoto = findViewById(R.id.ivShipPhoto);

    // 建立暫存檔 Uri（拍照存到這裡）
    File photoFile = new File(getCacheDir(), "ship_photo.jpg");
    photoUri = FileProvider.getUriForFile(
        this,
        getPackageName() + ".fileprovider",  // 需在 Manifest 設定 FileProvider
        photoFile
    );

    // 初始化相機 Launcher
    cameraLauncher = registerForActivityResult(
        new ActivityResultContracts.TakePicture(),
        success -> {
            if (success) {
                // 拍照成功，顯示圖片
                ivShipPhoto.setImageURI(photoUri);
            } else {
                Toast.makeText(this, "拍照取消", Toast.LENGTH_SHORT).show();
            }
        }
    );

    // 按鈕觸發拍照
    Button btnPhoto = findViewById(R.id.btnPhoto);
    btnPhoto.setOnClickListener(v -> cameraLauncher.launch(photoUri));
}
```

**Step 3：AndroidManifest.xml 設定 FileProvider**

```xml
<application ...>
    <!-- FileProvider 設定（拍照存檔必要） -->
    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths" />
    </provider>
</application>
```

**Step 4：新增 `res/xml/file_paths.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <cache-path name="ship_photos" path="." />
</paths>
```

> **注意**：Android 6.0+ 還需要動態申請相機權限（見第三節）。

---

## 三、動態申請權限（RequestPermission 合約）

Android 6.0（API 23）起，危險權限（相機、位置、聯絡人等）**必須在執行時請求**，光在 Manifest 宣告不夠。

### 場景

通報時要使用相機，先確認是否有相機權限。

### 實作

```java
private ActivityResultLauncher<String> permissionLauncher;
private ActivityResultLauncher<Uri> cameraLauncher;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_report);

    // 相機 Launcher（沿用上一節）
    cameraLauncher = registerForActivityResult(
        new ActivityResultContracts.TakePicture(),
        success -> {
            if (success) ivShipPhoto.setImageURI(photoUri);
        }
    );

    // 權限申請 Launcher
    permissionLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestPermission(),
        granted -> {
            if (granted) {
                // 授權成功，啟動相機
                cameraLauncher.launch(photoUri);
            } else {
                Toast.makeText(this,
                    "需要相機權限才能拍照",
                    Toast.LENGTH_LONG).show();
            }
        }
    );

    // 按鈕點擊：先檢查權限，再決定是否需要申請
    Button btnPhoto = findViewById(R.id.btnPhoto);
    btnPhoto.setOnClickListener(v -> {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // 已有權限，直接拍照
            cameraLauncher.launch(photoUri);
        } else {
            // 沒有權限，先申請
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    });
}
```

**AndroidManifest.xml 加入宣告**（仍然需要，執行時再補請求）：

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

### 權限流程圖

```
使用者按拍照按鈕
        │
        ▼
  checkSelfPermission
        │
  ┌─────┴─────┐
GRANTED    NOT_GRANTED
  │              │
直接啟動    permissionLauncher.launch
相機               │
             ┌─────┴─────┐
           授權         拒絕
             │              │
         啟動相機       顯示說明
```

---

## 四、一個 Activity 管理多個 Launcher

當你需要「新增通報 Launcher」和「相機 Launcher」同時存在，只需宣告多個欄位：

```java
public class ReportActivity extends AppCompatActivity {

    // 兩個不同用途的 Launcher，互不干擾
    private ActivityResultLauncher<String>  permissionLauncher;
    private ActivityResultLauncher<Uri>     cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            granted -> {
                if (granted) cameraLauncher.launch(photoUri);
                else Toast.makeText(this, "需要相機權限", Toast.LENGTH_SHORT).show();
            }
        );

        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success) ivShipPhoto.setImageURI(photoUri);
            }
        );

        // 按鈕分別觸發對應 Launcher
        btnPhoto.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                cameraLauncher.launch(photoUri);
            else
                permissionLauncher.launch(Manifest.permission.CAMERA);
        });
    }
}
```

> **原則**：每個 Launcher 負責一件事，不要試圖用一個 Launcher 做多種事情。

---

## 五、完整「海港通報 + 拍照」App 架構

```
PortReportApp/
├── MainActivity.java           ← 主頁（通報列表 + 新增按鈕）
├── ReportActivity.java         ← 通報表單（船名、港口、狀態、拍照）
├── res/layout/
│   ├── activity_main.xml
│   ├── activity_report.xml     ← 增加 ImageView + 拍照按鈕
│   └── item_report.xml         ← ListView 每筆的格式
├── res/xml/
│   └── file_paths.xml          ← FileProvider 路徑設定
└── AndroidManifest.xml         ← CAMERA 權限 + FileProvider

資料流：
ReportActivity ──── RESULT_OK ────→ MainActivity
    ├─ SHIP_NAME
    ├─ PORT
    ├─ STATUS
    ├─ NOTE
    └─ PHOTO_URI（拍照的 Uri）
```

---

## 延伸思考

1. 如果要讓使用者可以「從相簿選照片」或「拍照」二選一，你會如何設計？（提示：`PickVisualMedia` 合約）

2. 通報資料目前只存在記憶體（`ArrayList`），App 關掉就消失。下週學完 Room Database 後，可以把通報記錄**永久存到本機資料庫**——想想看資料表要設計幾個欄位？

3. `ActivityResultContracts.GetContent()` 可以選任意檔案，如果你想讓使用者上傳「船舶入港許可文件（PDF）」，應該怎麼呼叫它？
