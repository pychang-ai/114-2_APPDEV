# 課後進階練習（選項）— 從 ListView 進化到 RecyclerView

> ⚙️ **本檔案為 W11 進階補充**——**不繳交、不計分、自由探索**。
> 想多挑戰自己、想做更接近業界的清單實作、想讓期末專題更有水準，這份適合你。
> 學完課本 Ch07 主課（Spinner / ListView / ArrayAdapter / Options Menu）再來看會比較順。

---

## 一、為什麼有了 ListView 還要學 RecyclerView？

### ListView 的痛點

當資料超過 50 筆時 ListView 會：

| 問題 | 後果 |
|---|---|
| 每滾一次就 inflate 一個新 view | 記憶體飆升、捲動頓挫 |
| 沒強制 ViewHolder 模式 | 工程師常忘記實作，效能差 |
| 不支援 Grid、橫向、瀑布流 | 換版面要換元件 |

### RecyclerView 的解法

> **「Recycler」就是「回收器」**：滾出畫面的 view 不是丟掉，而是丟回池子，給下一筆資料**重複使用**。

| 問題 | RecyclerView 解法 |
|---|---|
| 重用 view | **強制 ViewHolder 模式**（不寫不能編譯）|
| 排版彈性 | `LayoutManager` 切換（Linear / Grid / Staggered）|
| 資料綁定 | `Adapter`（與 ArrayAdapter 概念延伸）|
| 動畫 | `ItemAnimator` 獨立模組 |

### 三件套架構圖

```
┌─────────────────────────────────────────┐
│           RecyclerView（容器）           │
├─────────────────────────────────────────┤
│  LayoutManager：決定怎麼排                │
│      ├── LinearLayoutManager（垂直/水平）│
│      ├── GridLayoutManager               │
│      └── StaggeredGridLayoutManager      │
├─────────────────────────────────────────┤
│  Adapter：決定怎麼綁資料                  │
│      ├── onCreateViewHolder（造一個格子） │
│      ├── onBindViewHolder（把資料塞進去） │
│      └── getItemCount（共幾筆）          │
├─────────────────────────────────────────┤
│  ViewHolder：一個格子的快取               │
└─────────────────────────────────────────┘
```

---

## 二、第一個 RecyclerView 範例

### Step 1：在 build.gradle 加入相依套件

打開 `Module: app` 的 `build.gradle`：

```gradle
dependencies {
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
}
```

按 **Sync Now** 同步 gradle。

### Step 2：在 activity_main.xml 加入 RecyclerView

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### Step 3：設計每一筆資料的版面（item_ocean.xml）

在 `res/layout/` 新建 `item_ocean.xml`：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="12dp">

    <ImageView
        android:id="@+id/imgOcean"
        android:layout_width="80dp"
        android:layout_height="80dp" />

    <TextView
        android:id="@+id/txtName"
        android:layout_width="match_parent"
        android:layout_height="80dp"
        android:gravity="center_vertical"
        android:paddingStart="16dp"
        android:textSize="18sp" />
</LinearLayout>
```

### Step 4：寫 Adapter（核心）

新建 `OceanAdapter.java`：

```java
public class OceanAdapter extends RecyclerView.Adapter<OceanAdapter.ViewHolder> {

    private String[] names;
    private int[] images;

    public OceanAdapter(String[] names, int[] images) {
        this.names = names;
        this.images = images;
    }

    // 1. 建立 ViewHolder（造格子）
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ocean, parent, false);
        return new ViewHolder(view);
    }

    // 2. 綁資料（把資料塞進格子）
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtName.setText(names[position]);
        holder.imgOcean.setImageResource(images[position]);
    }

    // 3. 共幾筆
    @Override
    public int getItemCount() {
        return names.length;
    }

    // ViewHolder 內部類別（一個格子的快取）
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgOcean;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgOcean = itemView.findViewById(R.id.imgOcean);
        }
    }
}
```

### Step 5：在 MainActivity 設定 RecyclerView

```java
String[] names = {"鯨魚", "海豚", "章魚", "海龜", "海星", "水母", "海馬", "鯊魚"};
int[] images = {R.mipmap.whale, R.mipmap.dolphin, R.mipmap.octopus,
                R.mipmap.turtle, R.mipmap.starfish, R.mipmap.jellyfish,
                R.mipmap.seahorse, R.mipmap.shark};

RecyclerView rv = findViewById(R.id.recyclerView);
rv.setLayoutManager(new LinearLayoutManager(this));  // 垂直清單
rv.setAdapter(new OceanAdapter(names, images));
```

完成。RecyclerView 自動處理捲動、回收、效能。

---

## 三、切換不同的 LayoutManager

只改一行就能換版面：

```java
// 垂直清單（預設）
rv.setLayoutManager(new LinearLayoutManager(this));

// 水平清單
rv.setLayoutManager(new LinearLayoutManager(this,
    LinearLayoutManager.HORIZONTAL, false));

// 2 欄 Grid
rv.setLayoutManager(new GridLayoutManager(this, 2));

// 瀑布流（不等高）
rv.setLayoutManager(new StaggeredGridLayoutManager(2,
    StaggeredGridLayoutManager.VERTICAL));
```

> 💡 對比 ListView：**換版面要換元件**。RecyclerView 只換 LayoutManager，**程式碼幾乎不動**——這就是「分離關注點」的威力。

---

## 四、處理點擊事件

在 `OceanAdapter` 的 `onBindViewHolder()` 中：

```java
@Override
public void onBindViewHolder(ViewHolder holder, int position) {
    holder.txtName.setText(names[position]);
    holder.imgOcean.setImageResource(images[position]);

    // 設定點擊
    holder.itemView.setOnClickListener(v -> {
        int pos = holder.getBindingAdapterPosition();
        Toast.makeText(v.getContext(),
            "你選的是：" + names[pos], Toast.LENGTH_SHORT).show();
    });
}
```

> ⚠️ **重要**：用 `getBindingAdapterPosition()`，**不要用** `getAdapterPosition()`（已 deprecated）。

---

## 五、ListView vs RecyclerView 對照

| 面向 | ListView（課本 Ch07）| RecyclerView（業界標準）|
|---|---|---|
| 資料綁定 | ArrayAdapter | RecyclerView.Adapter（要自己寫繼承類別）|
| ViewHolder | 工程師可自選實作 | **強制必須**（不寫不能編譯）|
| 排版彈性 | 固定垂直 | LayoutManager 隨意切換 |
| 程式碼量 | 較少 | 較多（但可重用）|
| 效能 | 中 | **高**（重用機制完整）|
| 學習曲線 | 平緩 | 較陡 |
| **何時用？** | 簡單清單、項目少（< 30）| 任何正式專案、大量資料、需要彈性版面 |

> 📌 **學習路徑建議**：先把 ListView + ArrayAdapter（W11 主課）寫熟，**再來看 RecyclerView 才會懂為什麼這樣設計**。

---

## 六、進階挑戰：海洋生物圖鑑（不繳交、不計分）

> 💡 **想做就做、不做沒關係**。寫完可以拿來放期末專題、或做個人作品集。

### 任務

設計一個「海洋生物圖鑑」App：
- ≥ **8 種**海洋生物，每筆顯示**圖片 + 名稱 + 簡介**
- 點擊後**跳新 Activity** 顯示生物詳細資訊（用 Intent 傳資料，呼應 W10 學的）
- **至少切換一次 LayoutManager**（提交兩版截圖：Linear + Grid）

### 加分項（自由發揮）

| # | 加分項 | 難度 |
|---|---|---|
| 1 | 用 `onCreateViewHolder` 的 `viewType` 做**異質清單**（標題項 + 內容項不同版面）| ⭐⭐ |
| 2 | 加入**滑動刪除**（`ItemTouchHelper`）| ⭐⭐⭐ |
| 3 | 從**網路 API** 抓海洋生物資料（呼應 W14 Retrofit）| ⭐⭐⭐⭐ |
| 4 | 加上**搜尋功能**（SearchView 連動 Adapter）| ⭐⭐⭐ |
| 5 | 用 **DiffUtil** 做高效資料更新 | ⭐⭐⭐⭐ |

### 給卡住的同學

| 卡點 | 解法 |
|---|---|
| Sync gradle 失敗 | 檢查 `dependencies` 區塊的版本號是否寫錯 |
| RecyclerView **找不到類別** | `import androidx.recyclerview.widget.RecyclerView;`（**不要用**舊的 `android.support.v7`）|
| 清單**只顯示一筆**還重疊 | item layout 的 `layout_height` 寫成 `match_parent` 了 → 改 `wrap_content` |
| 點擊**無反應** | 檢查 `OnClickListener` 綁在 `holder.itemView` 還是子 view |
| Adapter 改了資料**畫面沒變** | 改完資料要呼叫 `adapter.notifyDataSetChanged()`（或更高效的 `notifyItemChanged`）|

---

## 七、再延伸（給想跳更前面的同學）

| 主題 | 何時會用 | 自學資源 |
|---|---|---|
| Jetpack Compose（聲明式 UI）| 取代 XML，是 Android 未來主流 | [官方教學](https://developer.android.com/jetpack/compose/tutorial) |
| Kotlin Coroutines（非同步）| 處理網路、資料庫不卡 UI | [官方文件](https://kotlinlang.org/docs/coroutines-overview.html) |
| ViewModel + LiveData | 處理畫面旋轉/暫停資料保留 | Android Architecture Components |
| Room Database | W12-W13 課本會教，可搶先看 | 課本 Ch08 |

---

*本補充教材由 PI 提供，給想多探索的同學。**不必所有人都做**——把課本主課（Ch07 Spinner/ListView/ArrayAdapter/Options Menu）寫熟才是基礎。* 
