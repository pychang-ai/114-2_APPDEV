# W11 進階介面元件 + 訊息與對話方塊（Ch07＋Ch08 雙章）

> **APP 開發課程** ｜ 第 11 週 ｜ 5/7
> **教科書**：Ch07 進階介面元件 ＋ Ch08 訊息與對話方塊
> **Ch07 單元**：7-1 Spinner / 7-2 ListView / 7-3 ArrayAdapter / 7-4 Options Menu
> **Ch08 單元**：8-1 Toast/Log / 8-2 Dialog 介紹 / 8-3 對話方塊 4 型 / 8-4 日期與時間對話方塊
> **本週不出作業**——所有題目皆為**課後練習（選項，不繳交、不計分）**
> **進階補充**：📖 [課後進階練習（選項）— 從 ListView 進化到 RecyclerView](advanced.md)

---

## 學習目標

### Ch07 進階介面元件
1. 用 **Spinner** 建立下拉式選單，並處理選取事件（`OnItemSelectedListener`）
2. 用 **ListView** 建立可捲動清單，並處理點擊事件（`OnItemClickListener`）
3. 用 **ArrayAdapter** 把資料來源（Java 陣列 / 字串資源）橋接到清單元件
4. 在 Action Bar 建立 **Options Menu**，並處理選項點擊（`onOptionsItemSelected`）

### Ch08 訊息與對話方塊
5. 用 **Toast** 顯示暫時性彈跳訊息，用 **Log** 列印偵錯資訊（搭配 `try/catch` 例外處理）
6. 認識 **Dialog 類別**結構與 `AlertDialog.Builder` 建構模式
7. 建立 4 種對話方塊：**訊息**、**確認**、**單選**、**複選**（搭配 `DialogInterface.OnClickListener`）
8. 用 **DatePickerDialog** 與 **TimePickerDialog** 建立日期/時間選擇器

---

## 一、7-1 下拉式選單（Spinner）

> **PPT：Android_Ch07.ppt（投影片 P3–P12）**

### 7-1-1 建立 Spinner 元件

Spinner 是類似 Windows 作業系統的下拉式清單方塊——**單選**的清單元件，使用者點擊後展開項目選一個。

#### Android Studio 介面設計工具新增方式

從元件面板「**Containers**」區段拖入 **Spinner**。

#### 用 entries 屬性綁字串陣列資源

Spinner 顯示什麼項目？最常用做法是綁**字串陣列資源**（放在 `res/values/strings.xml`）：

**Step 1：在 `strings.xml` 定義字串陣列**

```xml
<resources>
    <string-array name="steaks">
        <item>三分熟</item>
        <item>五分熟</item>
        <item>七分熟</item>
        <item>全熟</item>
    </string-array>
</resources>
```

**Step 2：在 layout XML 用 entries 綁定**

```xml
<Spinner
    android:id="@+id/spinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:entries="@array/steaks" />
```

> 📌 `@array/steaks` 中：`@array` 表示字串陣列資源，`steaks` 是 `<string-array>` 的 name。

#### getSelectedItemPosition() 方法 — 取得選取項目索引

```java
Spinner sp = (Spinner) findViewById(R.id.spinner);
int index = sp.getSelectedItemPosition();
```

回傳整數**索引值**（從 0 開始）。

#### 取出陣列值的另一種寫法

```java
String[] steaks = getResources().getStringArray(R.array.steaks);
String selected = steaks[index];
```

> 📌 `getResources().getStringArray(R.array.xxx)` 是 Java 程式碼**從資源檔讀字串陣列**的標準方式。

#### 🛠️ 實作專案 Ch7_1_1：牛排點餐單

**功能**：用 Spinner 選擇牛排幾分熟，按下按鈕後在下方 TextView 顯示選擇結果。

**元件配置**：

| 元件 | id | 內容 |
|---|---|---|
| Spinner | `spinner` | 綁 `@array/steaks`（三分熟 / 五分熟 / 七分熟 / 全熟）|
| Button | `btnShow` | 「顯示選擇」 |
| TextView | `lblOutput` | 顯示結果 |

---

### 7-1-2 Spinner 的選取項目事件

#### 動機

按按鈕才顯示太被動——**選的當下就立刻反應**，靠 `OnItemSelectedListener`。

#### 實作 OnItemSelectedListener 介面

```java
public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(this);
    }
    ...
}
```

#### 兩個必實作方法

| 方法 | 觸發時機 | 是否常用 |
|---|---|---|
| **`onItemSelected()`** | 使用者選了某項 | **常用** |
| `onNothingSelected()` | 沒選任何項（罕見）| 通常空實作 |

```java
@Override
public void onItemSelected(AdapterView<?> adapterView,
                            View view, int position, long id) {
    int r = 0;
    switch (position) {
        case 0:  r = v1 + v2; break;  // 加法
        case 1:  r = v1 - v2; break;  // 減法
        case 2:  r = v1 * v2; break;  // 乘法
        case 3:  r = v1 / v2; break;  // 除法
    }
    output.setText("運算結果 = " + r);
}

@Override
public void onNothingSelected(AdapterView<?> adapterView) {
    // 通常留空
}
```

> 📌 `position` 參數是「**目前選中的項目索引**」，可直接用 `switch` 對應動作。

#### 🛠️ 實作專案 Ch7_1_2：四則計算機

**功能**：用 Spinner 選擇 + / - / × / ÷ 四種運算，選擇後立即顯示兩個輸入數字的運算結果。

---

## 二、7-2 列舉清單方塊（ListView）

> **PPT：Android_Ch07.ppt（投影片 P13–P17）**

### ListView 元件

#### 與 Spinner 的差異

| 元件 | 顯示方式 | 互動 |
|---|---|---|
| Spinner | **下拉**展開後選一個 | 點 → 展開 → 選 |
| **ListView** | **直接顯示**多項，超出可捲動 | 直接點某項 |

#### Android Studio 新增方式

從「**Legacy**」區段拖入 **ListView**。

> ⚠️ **易混淆點**：ListView 在「**Legacy**」（舊版）區段，不在「Common」。新版 Android 推薦用 RecyclerView 取代——但本週**先用 ListView 學 Adapter 概念**，下週的 RecyclerView 才好理解。

#### OnItemClickListener — 處理項目點擊

```java
public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener {

    private String[] cities = {"高雄", "台北", "台中", "台南", "新竹"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView,
                            View view, int position, long id) {
        TextView output = (TextView) findViewById(R.id.lblOutput);
        output.setText("你是住在: " + cities[position]);
    }
}
```

> 📌 `position` 用法和 7-1-2 的 `onItemSelected` 一樣——「**目前點到的項目索引**」。

#### 🛠️ 實作專案 Ch7_2：城市選擇

**功能**：ListView 顯示 5 個城市名稱，點擊任一個立即在 TextView 顯示「你是住在：高雄」。

---

## 三、7-3 ArrayAdapter 接合器

> **PPT：Android_Ch07.ppt（投影片 P18–P23）**

ArrayAdapter 用在「**清單項目要在程式執行時才決定**」的情境（從資料庫、網路、運算結果取得資料）；若項目固定不變，直接用 XML 的 `entries="@array/xxx"` 就好。

#### 接合器（Adapter）概念

> 接合器 = **資料來源** 與 **清單元件** 之間的**橋樑**

```
資料來源（陣列/資料庫/網路）
       ↓
   ArrayAdapter（接合器）
       ↓
清單元件（Spinner / ListView）
```

### 建立 ArrayAdapter 物件

#### 方法 1：用 Java 字串陣列建立

```java
String[] courses = {"美式漢堡", "特選牛排", "牛肉飯", "義大利麵"};

ArrayAdapter<String> a1 = new ArrayAdapter<>(this,
    android.R.layout.simple_spinner_item, courses);
```

> 📌 `<String>` 是泛型參數——告訴 ArrayAdapter「我裝的是字串」。

#### 方法 2：用字串陣列資源建立

```java
String[] desserts = getResources().getStringArray(R.array.dessert);

ArrayAdapter<String> a2 = new ArrayAdapter<>(this,
    android.R.layout.simple_spinner_item, desserts);
```

### setAdapter() — 指定使用的接合器

```java
Spinner sp1 = (Spinner) findViewById(R.id.spinner);
sp1.setAdapter(a1);
```

> 📌 ListView 也是同樣寫法：`listView.setAdapter(a1);`

### 🛠️ 實作專案 Ch7_3：團購點餐單

**功能**：兩個 Spinner 並列——
1. 第 1 個 Spinner（主餐）：用 **Java 陣列**建立 ArrayAdapter
2. 第 2 個 Spinner（點心）：用**字串陣列資源** `R.array.dessert` 建立 ArrayAdapter

按按鈕後在 TextView 顯示「主餐：XXX，點心：YYY」。

---

## 四、7-4 選項選單與動作列（Options Menu / Action Bar）

> **PPT：Android_Ch07.ppt（投影片 P25–P32）**

### 7-4-1 認識動作列（Action Bar）

#### 演進歷史

| 時期 | UI |
|---|---|
| 舊版 Android | 標題列 + **MENU 實體鍵**叫出選單 |
| Android 3.0+ | 整合為 **Action Bar**（活動最上方固定區）|
| 現在 | 沒有實體 MENU 鍵，選項整合進 Action Bar 的「**溢出選單**」（Overflow，垂直 3 點圖示）|

#### 動作列功能

- 顯示活動標題、圖示
- 提供切換 / 巡覽功能
- 顯示選單（**溢出選單** = Options Menu）

### 7-4-2 建立 Options Menu

#### Step 1：新增選單資源 XML

在 `res/menu/menu_main.xml`（用 Android Studio 介面設計工具建立）：

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/toF"
        android:title="轉華氏" />
    <item
        android:id="@+id/toC"
        android:title="轉攝氏" />
</menu>
```

#### Step 2：覆寫 onCreateOptionsMenu()

執行 Android Studio 選單 **Code > Override Methods** → 選 `onCreateOptionsMenu`：

```java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
}
```

> 📌 `MenuInflater` 把 XML 選單**展開**成真正的選單。

#### Step 3：覆寫 onOptionsItemSelected() 處理點擊

```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.toF:
            // 轉華氏：F = C × 9/5 + 32
            ...
            break;
        case R.id.toC:
            // 轉攝氏：C = (F - 32) × 5/9
            ...
            break;
    }
    return super.onOptionsItemSelected(item);
}
```

> 📌 `MenuItem.getItemId()` 取得使用者點到的選項 id。

### 🛠️ 實作專案 Ch7_4_2：溫度轉換選單

**功能**：EditText 輸入溫度數值，從動作列右上角「垂直 3 點」開啟選單，選「轉華氏」或「轉攝氏」立即轉換顯示。

---

## 五、8-1 顯示訊息（Toast / Log）

> **PPT：Android_Ch08.ppt（投影片 P3–P10）**

### 8-1-1 Toast 訊息

Toast 是**暫時性彈跳訊息**——會自動在幾秒後消失，**不打斷使用者操作**，最常用來做「**輕量回饋**」。

#### 三段式呼叫

```java
Toast.makeText(this, "你是住在: " + cities[position],
               Toast.LENGTH_SHORT).show();
```

| 參數 | 說明 |
|---|---|
| 第 1 個（`this`） | Context（活動自己）|
| 第 2 個（字串）| 訊息內容 |
| 第 3 個（duration）| `Toast.LENGTH_SHORT`（短，~2 秒）/ `Toast.LENGTH_LONG`（長，~3.5 秒）|

> ⚠️ **易錯點**：寫完 `makeText()` 一定要 **`.show()`**，不然不會出現。

#### 🛠️ 實作專案 Ch8_1_1：Toast 改寫城市選擇

**功能**：把 7-2 城市選擇的 TextView 顯示，改成 Toast 彈跳訊息。

```java
@Override
public void onItemClick(AdapterView<?> adapterView,
                        View view, int position, long id) {
    Toast.makeText(this, "你是住在: " + cities[position],
                   Toast.LENGTH_SHORT).show();
}
```

---

### 8-1-2 Log 偵錯訊息

#### Log 類別常用方法

| 方法 | 等級 | 用途 |
|---|---|---|
| `Log.v()` | Verbose | 最囉唆的細節 |
| `Log.d()` | Debug | **常用**，開發中除錯 |
| `Log.i()` | Info | 一般資訊 |
| `Log.w()` | Warn | 警告 |
| `Log.e()` | Error | 錯誤 |

#### 兩個參數寫法

```java
Log.d("Ch8_1_2", "除以 0 的錯誤....");
```

| 參數 | 說明 |
|---|---|
| 第 1 個（tag）| 識別字串，用來在 Logcat 過濾 |
| 第 2 個（msg）| 訊息內容 |

> 📌 在 Android Studio 下方 **Logcat** 視窗，輸入 tag 名稱即可過濾出自己的訊息。

#### try / catch / finally 例外處理

```java
try {
    // 可能出錯的程式碼
    int result = v1 / v2;
} catch (ArithmeticException e) {
    // 出錯時的處理
    Log.d("Ch8_1_2", "除以 0 的錯誤....");
    Toast.makeText(this, "不能除以 0", Toast.LENGTH_SHORT).show();
} finally {
    // 不論有沒有出錯都會執行（可省略）
}
```

| 區塊 | 說明 |
|---|---|
| `try` | 可能丟出例外的程式碼 |
| `catch (ExceptionType e)` | 處理特定類型的例外，可有多個 |
| `finally` | 收尾工作（可省略），**不論例外發生與否都執行** |

#### 🛠️ 實作專案 Ch8_1_2：四則計算機加除 0 檢查

**功能**：把 7-1-2 四則計算機改寫——若除法時第二個運算元是 0，就用 `Log.d()` 印偵錯訊息＋ Toast 提示使用者。

---

## 六、8-2 對話方塊介紹

> **PPT：Android_Ch08.ppt（投影片 P11–P12）**

### Dialog 類別架構

```
Dialog（基底類別）
  └─ AlertDialog（警告對話方塊，本週主角）
      └─ DatePickerDialog（日期選擇）
      └─ TimePickerDialog（時間選擇）
      └─ ProgressDialog（已過時，不教）
```

### 4 種 AlertDialog 形式

| 形式 | 用途 | 設定方法 |
|---|---|---|
| **訊息對話方塊** | 顯示一段訊息（例：關於本書）| `setMessage()` |
| **確認對話方塊** | 確認某動作（例：確認結束程式）| `setPositiveButton()` + `setNegativeButton()` |
| **單選對話方塊** | 從多選項選一個（例：選顏色）| `setItems()` |
| **複選對話方塊** | 從多選項選多個（例：勾選手機作業系統）| `setMultiChoiceItems()` |

### 共通建構模式：AlertDialog.Builder

```java
AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setTitle("標題");
builder.setMessage("訊息內容");
builder.setPositiveButton("確定", null);
builder.show();
```

> 💡 **串連呼叫（method chaining）**：因為每個 setter 回傳 Builder 自己，可以連著寫：
> ```java
> new AlertDialog.Builder(this)
>     .setTitle("標題")
>     .setMessage("內容")
>     .setPositiveButton("確定", null)
>     .show();
> ```

---

## 七、8-3 對話方塊實習（4 型）

> **PPT：Android_Ch08.ppt（投影片 P13–P33）**

### 8-3-1 訊息對話方塊

#### 完整範例

```java
AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setTitle("關於本書");
builder.setMessage("Android 程式設計\n作者: 陳會安");
builder.setCancelable(true);
builder.setPositiveButton("確定", null);
builder.show();
```

| 方法 | 作用 |
|---|---|
| `setTitle()` | 對話方塊標題 |
| `setMessage()` | 訊息正文 |
| `setCancelable(true)` | 點對話方塊外可關閉 |
| `setPositiveButton("確定", null)` | 確定按鈕，事件傳 `null` 表示**只關閉**不執行其他動作 |
| `.show()` | 顯示對話方塊（最後一定要呼叫）|

#### 🛠️ 實作專案 Ch8_3_1：「關於本書」對話方塊

按按鈕後彈出「關於本書」對話方塊，僅顯示資訊，按確定關閉。

---

### 8-3-2 確認對話方塊

#### 兩個按鈕的事件處理（DialogInterface.OnClickListener）

```java
public class MainActivity extends AppCompatActivity
    implements DialogInterface.OnClickListener {

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                finish();  // 關閉應用程式
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(this, "按下取消鈕!",
                    Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
```

| 常數 | 對應按鈕 |
|---|---|
| `BUTTON_POSITIVE` | 確定鈕（`setPositiveButton`）|
| `BUTTON_NEGATIVE` | 取消鈕（`setNegativeButton`）|
| `BUTTON_NEUTRAL` | 中性鈕（`setNeutralButton`，較少用）|

#### 用串連呼叫建立

```java
new AlertDialog.Builder(this)
    .setTitle("確認")
    .setMessage("確認結束本程式?")
    .setPositiveButton("確定", this)
    .setNegativeButton("取消", this)
    .show();
```

> 📌 第 2 個參數 `this` = 活動自己，因為已 implements `DialogInterface.OnClickListener`。

#### 🛠️ 實作專案 Ch8_3_2：結束程式確認

按「結束程式」鈕 → 跳出確認對話方塊 → 確定就關閉，取消就 Toast 提示。

---

### 8-3-3 單選對話方塊

#### setItems() 建立單選清單

```java
String[] options = {"紅色", "黃色", "綠色"};
builder.setItems(options, this);
builder.setNegativeButton("取消", null);
```

> 📌 **每個選項都視為一個按鈕**——點選某項 = 按下對應按鈕。

#### onClick() 判斷使用者選擇

```java
@Override
public void onClick(DialogInterface dialog, int which) {
    Button btn = (Button) findViewById(R.id.button);
    switch (which) {
        case 0: btn.setBackgroundColor(Color.RED); break;
        case 1: btn.setBackgroundColor(Color.YELLOW); break;
        case 2: btn.setBackgroundColor(Color.GREEN); break;
    }
}
```

> 📌 `which` 參數 = 使用者選的選項**索引**（從 0 開始）。

#### 🛠️ 實作專案 Ch8_3_3：色彩選擇對話方塊

按按鈕 → 開單選對話方塊 → 選紅 / 黃 / 綠 → Button 背景色立即改變。

---

### 8-3-4 複選對話方塊

#### setMultiChoiceItems() 建立複選清單

```java
String[] items = {"Samsung", "OPPO", "Apple", "ASUS"};
boolean[] itemsChecked = new boolean[4];

new AlertDialog.Builder(this)
    .setTitle("請勾選選項?")
    .setMultiChoiceItems(items, itemsChecked, this)
    .setPositiveButton("確定", this)
    .setNegativeButton("取消", null)
    .show();
```

| 參數 | 說明 |
|---|---|
| `items` | 選項陣列 |
| `itemsChecked` | 對應的 boolean 陣列，記住每項是否被勾選 |
| `this` | 傾聽者物件（實作 `OnMultiChoiceClickListener`）|

#### 兩個必實作的介面

| 介面 | 方法 | 用途 |
|---|---|---|
| `DialogInterface.OnMultiChoiceClickListener` | `onClick(dialog, which, isChecked)` | **每勾選/取消一次**就觸發 |
| `DialogInterface.OnClickListener` | `onClick(dialog, which)` | **按確定鈕**才觸發 |

#### 勾選變動處理

```java
@Override
public void onClick(DialogInterface dialog,
                    int which, boolean isChecked) {
    Toast.makeText(this,
        items[which] + (isChecked ? " 勾選" : " 取消勾選"),
        Toast.LENGTH_SHORT).show();
}
```

#### 確定鈕後彙總

```java
@Override
public void onClick(DialogInterface dialog, int which) {
    String msg = "";
    for (int i = 0; i < items.length; i++) {
        if (itemsChecked[i]) msg += items[i] + "\n";
    }
    TextView output = (TextView) findViewById(R.id.lblOutput);
    output.setText(msg);
}
```

#### 🛠️ 實作專案 Ch8_3_4：手機作業系統複選

按「複選手機」→ 開複選對話方塊 → 勾選曾用過的手機系統 → 按確定 → TextView 顯示所有勾選項。

---

## 八、8-4 日期與時間對話方塊

> **PPT：Android_Ch08.ppt（投影片 P34–P41）**

### 8-4-1 DatePickerDialog（日期選擇）

#### 建立並顯示

```java
Calendar dt = Calendar.getInstance();
int year  = dt.get(Calendar.YEAR);
int month = dt.get(Calendar.MONTH);
int day   = dt.get(Calendar.DAY_OF_MONTH);

DatePickerDialog dlg = new DatePickerDialog(this, this, year, month, day);
dlg.show();
```

| 建構子參數 | 說明 |
|---|---|
| 1. `this` | Context（活動自己）|
| 2. `this` | 傾聽者物件（活動自己 implements `OnDateSetListener`）|
| 3-5. `year, month, day` | 初始顯示的年月日 |

#### 處理使用者選擇 — onDateSet()

```java
@Override
public void onDateSet(DatePicker datePicker, int y, int m, int d) {
    output.setText("日期: " + y + "/" + (m + 1) + "/" + d);
}
```

> ⚠️ **月份要 +1**：Java 的月份從 0 開始（0 = 一月）。寫成 `(m + 1)` 才是常見的 1–12 月。

---

### 8-4-2 TimePickerDialog（時間選擇）

#### 建立並顯示

```java
Calendar dt = Calendar.getInstance();
int hour   = dt.get(Calendar.HOUR);
int minute = dt.get(Calendar.MINUTE);

TimePickerDialog dlg = new TimePickerDialog(this, this, hour, minute, true);
dlg.show();
```

| 建構子參數 | 說明 |
|---|---|
| 1. `this` | Context |
| 2. `this` | 傾聽者物件（活動自己 implements `OnTimeSetListener`）|
| 3-4. `hour, minute` | 初始時與分 |
| 5. `is24HourView` | `true` = 24 小時制；`false` = 12 小時制 |

#### 處理使用者選擇 — onTimeSet()

```java
@Override
public void onTimeSet(TimePicker timePicker, int h, int m) {
    output.setText("時間: " + h + ":" + m);
}
```

#### 🛠️ 實作專案 Ch8_4：日期或時間設定對話方塊

按「設定日期」鈕 → 開 DatePickerDialog；按「設定時間」鈕 → 開 TimePickerDialog。選完顯示在 TextView。

---

## 九、本週重點觀念複習卡

### Ch07 進階介面元件

| 觀念 | 一句話記憶 |
|---|---|
| Spinner vs ListView | Spinner **下拉展開**選一個；ListView **直接顯示**多項可捲動 |
| `entries` 屬性 | 用 XML 寫死清單時的最快寫法 |
| `getSelectedItemPosition()` | Spinner 取目前選中的索引 |
| `OnItemSelectedListener` | Spinner 的「選的當下」事件 |
| `OnItemClickListener` | ListView 的「點的當下」事件 |
| `position` 參數 | 兩種事件方法**第三個參數**就是「點/選到的索引」|
| **ArrayAdapter** | **資料 → 清單**的橋樑（執行時才決定資料時必用）|
| `setAdapter()` | 把 ArrayAdapter 接到 Spinner / ListView |
| `R.array.xxx` | 從 strings.xml 字串陣列資源讀資料的索引 |
| Action Bar 溢出選單 | 「右上角垂直 3 點」= Options Menu |
| `onCreateOptionsMenu()` | XML 選單 → 真正選單 |
| `onOptionsItemSelected()` | 處理選單點擊（用 `getItemId()` 比對）|

### Ch08 訊息與對話方塊

| 觀念 | 一句話記憶 |
|---|---|
| **Toast** | 暫時性彈跳訊息，三段式 `makeText().show()` |
| `LENGTH_SHORT` / `LENGTH_LONG` | Toast 顯示時長：~2 秒 / ~3.5 秒 |
| **Log** | 偵錯訊息，用 `Log.d(tag, msg)` 印到 Logcat |
| `try / catch / finally` | Java 例外處理三段式 |
| **AlertDialog.Builder** | 對話方塊**建構模式**——串連 setter 後 `.show()` |
| 4 種對話方塊 | 訊息 / 確認 / 單選 / 複選 |
| `setItems()` | **單選**對話方塊（每項視為按鈕）|
| `setMultiChoiceItems()` | **複選**對話方塊（搭 `boolean[]` 記住勾選狀態）|
| `BUTTON_POSITIVE` / `BUTTON_NEGATIVE` | 對話方塊按鈕的 `which` 常數 |
| **DatePickerDialog** | 日期選擇器，月份從 0 開始（要 +1 顯示）|
| **TimePickerDialog** | 時間選擇器，第 5 參數 `is24HourView` |
| `Calendar.getInstance()` | 取目前年月日時分作為對話方塊初值 |

---

## 十、課後練習（選項）

> ⚙️ **本週不出作業**——以下題目為**課後練習，不繳交、不計分、自由探索**。
> 想練手感、想做完整 mini app、或想為期末專題暖身的同學適合動手。

### 練習：海洋生物分類查詢 App（整合 Ch07＋Ch08 雙章八節觀念）

設計一個「**海洋生物分類查詢**」App，整合本週 Spinner、ListView、ArrayAdapter、Options Menu、Toast、AlertDialog、DatePickerDialog 七大元件。

#### 功能規格（全部選做）

| 區塊 | 元件 | 內容 |
|---|---|---|
| 1. 分類選擇 | `Spinner`（Ch07）| 4 個分類：魚類 / 哺乳類 / 無脊椎 / 海底植物（綁字串陣列資源） |
| 2. 生物清單 | `ListView`（Ch07）| 顯示**該分類**下的海洋生物（每類至少 5 種，用 ArrayAdapter 動態切換）|
| 3. 切換回饋 | `Toast`（Ch08）| 切換 Spinner 時 Toast 提示「已切換到：哺乳類」 |
| 4. 詳細彈窗 | `AlertDialog` 訊息對話方塊（Ch08）| 點 ListView 某項 → **彈出**對話方塊顯示生物詳細介紹 |
| 5. 動作選單 | `Options Menu`（Ch07）| 4 項：「重置選擇」「顯示總生物數」「關於本程式」「**設定觀察日期**」|
| 6. 重置確認 | `AlertDialog` 確認對話方塊（Ch08）| 點「重置選擇」→ **確認對話方塊**，按確定才執行重置 |
| 7. 關於本程式 | `AlertDialog` 訊息對話方塊（Ch08）| 點「關於本程式」→ 顯示作者、版本資訊 |
| 8. 設定觀察日期 | `DatePickerDialog`（Ch08）| 點「設定觀察日期」→ 開日期選擇器 → Toast 顯示「觀察日期已設為 2026/5/7」 |

#### 技術重點（自我檢核）

| # | 重點 | 來自 |
|---|---|---|
| 1 | Spinner 用 `entries` 屬性綁字串陣列資源 | 7-1 |
| 2 | Spinner 用 `OnItemSelectedListener` 偵測切換 | 7-1-2 |
| 3 | 切換分類時 **ArrayAdapter 重新綁定 ListView** | 7-3 |
| 4 | ListView 用 `OnItemClickListener` | 7-2 |
| 5 | Options Menu 用 `onCreateOptionsMenu` + `onOptionsItemSelected` | 7-4 |
| 6 | Toast 用 `Toast.makeText(...).show()` | 8-1-1 |
| 7 | AlertDialog 用 `AlertDialog.Builder` 串連呼叫 | 8-2 / 8-3-1 |
| 8 | 確認對話方塊處理 `BUTTON_POSITIVE` 與 `BUTTON_NEGATIVE` | 8-3-2 |
| 9 | DatePickerDialog 配合 `Calendar.getInstance()` 取初值 | 8-4 |
| 10 | `try/catch` 在按鈕事件保護程式（防 NullPointer / 陣列越界）| 8-1-2 |

#### 進階加碼（雙章融合）

| # | 加碼項 |
|---|---|
| A | 把生物詳細介紹的 `AlertDialog` 改成**單選對話方塊**：「請選擇要顯示的資訊：基本介紹 / 棲地 / 食性」（8-3-3）|
| B | 加「批次標記」按鈕，用**複選對話方塊**勾選多個生物，回到主畫面 Toast 列出（8-3-4）|
| C | 點生物時用 `Log.d(...)` 印偵錯訊息到 Logcat，方便事後追蹤學生使用習慣 |

> 📌 寫完想分享或卡關，課堂提問或在 Discord/LINE 群討論皆可，**不必發 PR**。

---

## 十一、給卡住的同學

### Ch07 常見錯誤

| 卡點 | 解法 |
|---|---|
| Spinner **不顯示任何項目** | `entries` 沒指定，或字串陣列資源名稱拼錯 |
| `OnItemSelectedListener` **找不到** | import 寫錯：`import android.widget.AdapterView.OnItemSelectedListener;` |
| 切換 Spinner 後 ListView **沒變化** | 沒呼叫 `listView.setAdapter(newAdapter)` 重新綁定 |
| ListView 拖入後**找不到 OnItemClickListener** | 用的不是 `View.OnClickListener`！ListView 用 `AdapterView.OnItemClickListener` |
| Options Menu **沒出現** | `onCreateOptionsMenu` 沒寫 `return super...` 或 `return true` |
| 點選單後**沒反應** | `onOptionsItemSelected` 的 `switch(item.getItemId())` id 對錯 |
| `R.menu.menu_main` 紅字 | `res/menu/menu_main.xml` 不存在或檔名錯，重新建立 |

### Ch08 常見錯誤

| 卡點 | 解法 |
|---|---|
| Toast **不出現** | `makeText()` 後忘記 **`.show()`**——這是最常見錯誤 |
| Toast 文字**亂碼或空白** | 第 1 個參數應該是 Context（活動本身用 `this` / 監聽器內用 `getApplicationContext()`）|
| AlertDialog **不出現** | `Builder` 串連最後忘記 `.show()` |
| 對話方塊按鈕**沒作用** | 第 2 參數傳了 `null`——要傳 `this`（已 implements `OnClickListener`）|
| `BUTTON_POSITIVE` / `BUTTON_NEGATIVE` **找不到** | 完整路徑：`DialogInterface.BUTTON_POSITIVE` |
| 複選對話方塊**勾選沒記住** | `boolean[] itemsChecked` 陣列大小要與選項數一致 |
| DatePickerDialog 顯示**月份錯一個月** | 月份從 0 開始，顯示時要 `m + 1` |
| `Calendar.YEAR` / `MONTH` **紅字** | 沒 import：`import java.util.Calendar;` |
| `try/catch` **編譯錯** | `catch` 區塊括號內要寫例外類型，如 `catch (ArithmeticException e)` |

---

## 十二、下週預告（W12）

W12 進入 **Fragment 與導覽列**（Bottom Navigation），把多畫面 App 拆成可重用的 Fragment 模組。

> 💡 **想提早接觸現代 Android UI 寫法？** 課本下一章還在 Fragment，但業界**早已從 ListView 改用 RecyclerView**。
> 對清單元件想看更現代的做法，請看 [📖 課後進階練習（選項）— RecyclerView](advanced.md)，**自由探索、不繳交、不計分**。
