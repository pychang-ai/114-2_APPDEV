# W10 基本介面元件：RadioButton、CheckBox、ImageView

> **APP 開發課程** ｜ 第 10 週 ｜ 4/30
> **教科書**：Ch06 基本介面元件
> **單元**：6-1 選項按鈕實習　/　6-2 核取方塊實習　/　6-3 圖形顯示實習
> **課後作業繳交期限**：W11 上課前（5/6）

---

## 學習目標

1. 用 **RadioGroup + RadioButton** 建立**單選**使用介面（如性別、票種）
2. 用 **CheckBox** 建立**複選**使用介面（如多重口味選擇）
3. 處理**選項改變事件**（`OnCheckedChangeListener`）和**文字改變事件**（`TextWatcher`），使 UI 即時回應
4. 使用 **ImageView** 顯示圖形資源，認識 `drawable` / `mipmap` 目錄差異

---

## 一、6-1 選項按鈕（RadioButton）

> **PPT：Android_Ch06.ppt（投影片 P3–P21）**

### 6-1-1 RadioGroup 與 RadioButton 元件

#### 為什麼需要 RadioGroup？

**單獨存在的 RadioButton 沒有單選功能**——你勾了一個，再勾另一個，兩個會同時被選中。要做「**多選一**」（性別、票種、是否同意），必須把多個 RadioButton **群組**起來：

| 元件 | 角色 | 是否可見 |
|---|---|---|
| `RadioGroup` | 看不見的容器，負責「同一群組只能選一個」 | 否（只是邏輯群組）|
| `RadioButton` | 真正可見的單選按鈕 | 是 |

```
RadioGroup（容器，看不見）
├── RadioButton（男）
└── RadioButton（女）
        ↑ 同時只能選一個
```

#### Android Studio 介面設計工具新增方式

1. 從元件面板「Buttons」區段拖入 **RadioGroup**
2. 在 RadioGroup 內部新增多個 **RadioButton**

#### isChecked() 方法 — 檢查使用者是否選擇

每個 RadioButton 有兩個狀態：選擇 / 沒有選擇。Java 程式碼用 `isChecked()` 取得：

```java
RadioButton boy = (RadioButton) findViewById(R.id.rdbBoy);
if (boy.isChecked())
    str += "男\n";
```

回傳 `true` 表示有選擇；`false` 表示沒選。

#### getCheckedRadioButtonId() 方法 — 直接問群組現在選哪個

逐一對每個 RadioButton 呼叫 `isChecked()` 太囉唆，**直接問 RadioGroup 現在哪個被選中**更簡潔：

```java
RadioGroup type = (RadioGroup) findViewById(R.id.rgType);
if (type.getCheckedRadioButtonId() == R.id.rdbAdult)
    str += "全票\n";
else if (type.getCheckedRadioButtonId() == R.id.rdbChild)
    str += "兒童票\n";
else
    str += "學生票\n";
```

#### 🛠️ 實作專案 Ch6_1_1：門票選擇程式

**功能**：用 RadioButton 選擇性別與門票種類，按下按鈕後，將結果顯示在下方 TextView。

**元件配置**：

| 元件 | id | 內容 |
|---|---|---|
| RadioGroup（性別）| `rgGender` | 包含 `rdbBoy` / `rdbGirl` |
| RadioGroup（票種）| `rgType` | 包含 `rdbAdult` / `rdbChild` / `rdbStudent` |
| Button | `btnShow` | 「顯示選擇」 |
| TextView | `lblOutput` | 顯示結果 |

---

### 6-1-2 RadioGroup 的選項改變事件

#### 動機

每次按按鈕才顯示結果有點笨——能不能**選的當下就立刻顯示**？可以，靠**選項改變事件**。

#### 實作 OnCheckedChangeListener 介面

讓 Activity 自己當「傾聽者」（implements 介面）：

```java
public class MainActivity extends AppCompatActivity
    implements RadioGroup.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgGender);
        rg.setOnCheckedChangeListener(this);
    }
    ...
}
```

#### onCheckedChanged() 方法

介面只有一個必實作方法 `onCheckedChanged()`，當 RadioGroup 內任何選項改變時觸發：

```java
@Override
public void onCheckedChanged(RadioGroup radioGroup, int i) {
    TextView output = (TextView) findViewById(R.id.lblOutput);
    switch (i) {
        case R.id.rdbBoy:
            RadioButton boy = (RadioButton) findViewById(R.id.rdbBoy);
            output.setText(boy.getText());
            break;
        case R.id.rdbGirl:
            RadioButton girl = (RadioButton) findViewById(R.id.rdbGirl);
            output.setText(girl.getText());
            break;
    }
}
```

> 第二個參數 `i` 是「**目前被選中的那個 RadioButton 的 id**」，可直接用 `switch` 比對。

#### 🛠️ 實作專案 Ch6_1_2：性別即時顯示

修改 Ch6_1_1，**移除按鈕**，選擇性別當下即在 TextView 顯示，不必等按鈕。

---

### 6-1-3 EditText 的文字改變事件

#### 動機

EditText 也想做「使用者一打字就馬上反應」？用 **TextWatcher** 介面。

#### 實作 TextWatcher 介面

```java
public class MainActivity extends AppCompatActivity
    implements TextWatcher {

    private EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        txt = (EditText) findViewById(R.id.txtName);
        txt.addTextChangedListener(this);
    }
    ...
}
```

#### TextWatcher 三個方法

介面**強制實作 3 個**，但實務上只用中間那一個：

| 方法 | 觸發時機 | 是否常用 |
|---|---|---|
| `beforeTextChanged()` | 文字**改變前** | 少 |
| **`onTextChanged()`** | 文字**改變中**（每打一字） | **常用** |
| `afterTextChanged()` | 文字**改變後** | 少 |

```java
@Override
public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    show(rg.getCheckedRadioButtonId());
}
```

> 另外兩個方法**留空**即可，但**不能不寫**——介面強制要求。

#### 🛠️ 實作專案 Ch6_1_3：姓名 + 性別即時顯示

在 Ch6_1_2 基礎上新增 EditText 輸入姓名。**輸入或編輯姓名**、**或選擇性別**，都會立刻在 TextView 顯示「姓名 + 性別」。

---

## 二、6-2 核取方塊（CheckBox）

> **PPT：Android_Ch06.ppt（投影片 P22–P35）**

### 6-2-1 CheckBox 元件

#### CheckBox vs RadioButton

| 元件 | 用途 | 多選還是單選？ | 需要群組嗎？ |
|---|---|---|---|
| RadioButton | 多選一（互斥）| **單選** | 需要 RadioGroup |
| **CheckBox** | 開關式選擇（獨立）| **可複選** | **不需要群組** |

每個 CheckBox 都是**獨立的開關**，所以放在一起就自然複選——這也是為什麼不需要 group 的原因。

#### isChecked() 方法

跟 RadioButton 用法一樣：

```java
CheckBox original = (CheckBox) findViewById(R.id.chkOriginal);
if (original.isChecked())
    str += original.getText() + "\n";
```

#### 🛠️ 實作專案 Ch6_2_1：披薩店訂購

**功能**：用 3 個 CheckBox（原味、牛肉、海鮮）選擇要點哪些口味，按下按鈕顯示訂購清單。

```
☐ 原味披薩
☑ 牛肉披薩      ← 你訂購：
☑ 海鮮披薩         牛肉披薩
                   海鮮披薩
[ 顯示訂購 ]
```

---

### 6-2-2 CheckBox 的選項改變事件

#### 介面：CompoundButton.OnCheckedChangeListener

CheckBox 用的介面**和 RadioGroup 不同**——是 `CompoundButton.OnCheckedChangeListener`：

```java
public class MainActivity extends AppCompatActivity
    implements CompoundButton.OnCheckedChangeListener {
    ...
}
```

> ⚠️ **易錯點**：RadioGroup 用 `RadioGroup.OnCheckedChangeListener`，CheckBox 用 `CompoundButton.OnCheckedChangeListener`，兩個是**不同**的介面，不能混用。

#### 兩種註冊傾聽者方法

**方法 1：一一註冊**

```java
CheckBox chk1 = (CheckBox) findViewById(R.id.chkOriginal);
chk1.setOnCheckedChangeListener(this);

CheckBox chk2 = (CheckBox) findViewById(R.id.chkBeef);
chk2.setOnCheckedChangeListener(this);

CheckBox chk3 = (CheckBox) findViewById(R.id.chkSeafood);
chk3.setOnCheckedChangeListener(this);
```

**方法 2：for 迴圈 + ID 陣列**（**推薦**，元件多時更簡潔）

```java
private int[] chkIDs = {R.id.chkOriginal, R.id.chkBeef, R.id.chkSeafood};

for (int id : chkIDs) {
    CheckBox chk = (CheckBox) findViewById(id);
    chk.setOnCheckedChangeListener(this);
}
```

#### onCheckedChanged() 介面方法

```java
@Override
public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    switch (compoundButton.getId()) {
        case R.id.chkOriginal:
            output.setText("你點選的是原味...\n");
            break;
        case R.id.chkBeef:
            output.setText("你點選的是牛肉...\n");
            break;
        case R.id.chkSeafood:
            output.setText("你點選的是海鮮...\n");
            break;
    }
}
```

> ⚠️ 注意：第二個參數 `boolean b` 表示「現在是 ☑ 還是 ☐」——因為事件**勾選和取消都會觸發**。

#### 🛠️ 實作專案 Ch6_2_2：即時顯示訂購

修改 Ch6_2_1，每次勾/取消任一 CheckBox 時：
1. 立刻顯示「你點選的是 XX」
2. 列出**目前所有勾選**的口味（用 for 迴圈走訪 `chkIDs`）

---

## 三、6-3 圖形顯示（ImageView）

> **PPT：Android_Ch06.ppt（投影片 P36–P47）**

### 6-3-1 Android Studio 專案的圖形資源

#### 兩種圖形資源目錄

| 目錄 | 用途 | 格式 |
|---|---|---|
| `app/src/main/res/drawable` | 一般圖形（背景圖、按鈕圖、內容圖示）| PNG / JPG / GIF |
| `app/src/main/res/mipmap` | **應用程式圖示**（App icon）| WebP（新版）/ PNG |

> 🔍 **記憶法**：`drawable` 是「畫的內容」，`mipmap` 是「桌面 App 的圖示」。

#### 螢幕解析度子目錄

Android 為了讓**不同解析度螢幕**都能顯示清楚的圖，把目錄再分為：

| 子目錄 | 解析度 | 一般 px 大小（48dp 圖示為例）|
|---|---|---|
| `mipmap-mdpi` | 中密度 | 48 × 48 px |
| `mipmap-hdpi` | 高密度 | 72 × 72 px |
| `mipmap-xhdpi` | 超高密度 | 96 × 96 px |
| `mipmap-xxhdpi` | 超超高密度 | 144 × 144 px |
| `mipmap-xxxhdpi` | 超超超高密度 | 192 × 192 px |

> 📌 **新手做法**：放一份在 `mipmap-mdpi` 即可，Android 會自動縮放。等專題完成度高再補其他解析度。

#### 把圖片加進專案的步驟

| Step | 動作 |
|---|---|
| 1 | 用檔案總管選取圖檔 → 右鍵【**複製**】 |
| 2 | 在 Android Studio 專案視窗的 `res\mipmap` 上 → 右鍵【**Paste**】 |
| 3 | 在「Choose Destination Directory」對話方塊選 `mipmap-mdpi` → 按 **OK** |
| 4 | 「Copy」對話方塊 → 按 **OK** 完成 |

---

### 6-3-2 使用 ImageView 顯示圖形

#### 元件配置

從 Android Studio 介面設計工具的「**Common**」區段拖入 **ImageView**。

#### setImageResource() 方法 — 用 Java 切換圖片

```java
ImageView image = (ImageView) findViewById(R.id.imgPhoto);
image.setImageResource(R.mipmap.elephant);
```

> 參數 `R.mipmap.elephant` 對應 `app/src/main/res/mipmap/elephant.png` 圖檔。
> ⚠️ 檔名**不能有大寫字母**、**不能有空格**、**不能以數字開頭**——否則 R.mipmap 找不到。

#### 🛠️ 實作專案 Ch6_3：簡易秀圖程式

**功能**：4 個 RadioButton 對應 4 張圖（如 elephant / lion / tiger / panda），選哪個就在下方 ImageView 顯示對應圖片。

**核心程式片段**：

```java
@Override
public void onCheckedChanged(RadioGroup radioGroup, int i) {
    ImageView image = (ImageView) findViewById(R.id.imgPhoto);
    switch (i) {
        case R.id.rdbElephant:
            image.setImageResource(R.mipmap.elephant);
            break;
        case R.id.rdbLion:
            image.setImageResource(R.mipmap.lion);
            break;
        // ...
    }
}
```

> 🌊 **海洋主題建議**：本週實作可改用海洋生物圖（鯨魚、海豚、章魚、海龜），銜接期末專題的海事主題。

---

## 四、本週重點觀念複習卡

| 觀念 | 一句話記憶 |
|---|---|
| RadioButton 必須群組 | 單獨的 RadioButton 不互斥；要 RadioGroup 包起來才會單選 |
| 單選 vs 複選 | RadioButton **單選**（要 group）／ CheckBox **複選**（不用 group）|
| 兩種 OnCheckedChangeListener | RadioGroup 用 `RadioGroup.OnCheckedChangeListener`；CheckBox 用 `CompoundButton.OnCheckedChangeListener` |
| getCheckedRadioButtonId() | 直接問 RadioGroup「現在選哪個」，比逐一 isChecked() 簡潔 |
| TextWatcher 必須實作 3 方法 | 即使只用 `onTextChanged()`，另兩個方法仍要寫（即使是空的）|
| drawable vs mipmap | drawable = 一般圖；mipmap = App 圖示 |
| setImageResource() | Java 程式中切換 ImageView 顯示的圖片 |

---

## 五、本週作業

> 繳交方式：在你 fork 的 `114-2_APPDEV/week10/` 建立**一個 Android Studio 專案**完成下列功能，push 到你的 fork（沿用學期既有 PR）

### 作業：海洋生物選購 App（整合 Ch06 三節觀念）

設計一個「**海洋生物紀念品店**」訂購 App，整合本週 RadioButton / CheckBox / ImageView 三大元件。

#### 功能規格

| 區塊 | 元件 | 內容 |
|---|---|---|
| 1. 顧客資料 | `EditText` | 姓名輸入（用 TextWatcher 即時顯示在訂購單上） |
| 2. 紀念品種類（單選）| `RadioGroup` + 3 個 RadioButton | 馬克杯 / T 恤 / 海報 |
| 3. 海洋生物圖案（單選）| `RadioGroup` + 4 個 RadioButton | 鯨魚 / 海豚 / 章魚 / 海龜（連動下方 ImageView 顯示對應圖）|
| 4. 加購選項（複選）| 3 個 CheckBox | 禮盒包裝 / 環保袋 / 賀卡 |
| 5. 圖片預覽 | `ImageView` | 顯示目前選中的海洋生物圖 |
| 6. 訂購單 | `TextView` | 即時顯示「姓名 + 紀念品 + 圖案 + 加購項目」（任一改變立刻更新）|

#### 技術要求

| # | 要求 |
|---|---|
| 1 | RadioGroup 用 `OnCheckedChangeListener` 即時觸發顯示 |
| 2 | CheckBox 用 `CompoundButton.OnCheckedChangeListener` 處理勾選 |
| 3 | ImageView 用 `setImageResource()` 切換 4 張海洋生物圖（自己找圖或畫圖皆可，放在 `res/mipmap-mdpi`）|
| 4 | EditText 用 `TextWatcher` 即時更新姓名 |
| 5 | CheckBox 註冊**請用 for 迴圈 + ID 陣列**方法（不要一一註冊）|

#### 繳交清單

| # | 內容 |
|---|---|
| 1 | Android Studio 專案資料夾（含 `app/`、`build.gradle` 等）|
| 2 | 4 張海洋生物圖檔（放在專案 `res/mipmap-mdpi`）|
| 3 | 模擬器執行截圖 1 張：選好所有選項後的訂購單畫面 |
| 4 | `README.md`：簡述你選了哪 4 隻海洋生物、為什麼 |

---

## 六、給卡住的同學

| 卡點 | 解法 |
|---|---|
| RadioButton 點了**沒有單選效果**（兩個都被選中）| 沒包在同一個 RadioGroup 裡——檢查 XML 巢狀結構 |
| `setOnCheckedChangeListener` **找不到方法** | import 錯：RadioGroup 用 `RadioGroup.OnCheckedChangeListener`，CheckBox 用 `CompoundButton.OnCheckedChangeListener` |
| TextWatcher 編譯錯誤「missing methods」| 介面強制實作**三個方法**——`beforeTextChanged()` 和 `afterTextChanged()` 留空也要寫 |
| `R.mipmap.xxx` **紅字找不到** | 圖檔名有大寫、空格、或數字開頭——重新命名成全小寫英文 |
| 圖檔放進去**不顯示** | 重新 Build → Clean Project → Rebuild |

---

*下週起進入 Ch07 動態資料呈現（RecyclerView）+ Adapter 模式。請先熟悉本週三個事件處理介面（`OnCheckedChangeListener` × 2、`TextWatcher`）的差異。*
