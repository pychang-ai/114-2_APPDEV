# W05 課後作業：海洋生態系統模擬器

> **APP 開發課程** ｜ 第 5 週 ｜ 課後作業  
> **繳交期限**：W06 上課前  
> **繳交方式**：放在 `week05/` 資料夾，push 到 Fork 即可

---

## 作業說明

整合本週所學的多載、覆寫和 final，建立一個完整的海洋生態系統模擬器。

**檔案**：`week05/MarineEcosystem.java`

---

## 要求

### 1. 父類別 Creature

- 屬性：`name`、`habitat`
- 建構子接收 name 和 habitat
- 方法 `move()`：回傳一般移動描述
- 方法 `eat()`：回傳一般覓食描述
- 方法 `describe()`：回傳 name 和 habitat 的完整描述
- `final` 方法 `kingdom()`：回傳「動物界」，子類別不能覆寫

### 2. 至少 4 個子類別

從以下選擇 4 種或自訂：

| 生物 | 建議 move() | 建議 eat() |
|------|-----------|-----------|
| Shark 鯊魚 | 高速衝刺 | 撕咬獵物 |
| Turtle 海龜 | 緩慢划動四肢 | 啃食海草 |
| Dolphin 海豚 | 躍出水面 | 合作圍捕魚群 |
| Octopus 章魚 | 噴射水流推進 | 用觸手捕捉獵物 |
| Seahorse 海馬 | 擺動背鰭直立游動 | 吸食浮游生物 |
| Crab 螃蟹 | 橫向移動 | 用螯夾取食物 |

每個子類別必須：
- 繼承 Creature
- 用 `super(name, habitat)` 呼叫父類別建構子
- 覆寫 `move()` 和 `eat()`，加上 `@Override`

### 3. 方法多載 feed()

在 Creature 中實作 3 個版本的 feed()：

```java
// 版本 1：無參數
String feed()
// 回傳：name + " 正在覓食"

// 版本 2：指定食物
String feed(String food)
// 回傳：name + " 正在吃 " + food

// 版本 3：指定食物和數量
String feed(String food, int amount)
// 回傳：name + " 吃了 " + amount + " 份 " + food
```

### 4. final 使用

- 至少一個 `final` 變數：例如 `final int OCEAN_DEPTH = 11034;`
- 至少一個 `final` 方法：例如 `kingdom()` 回傳「動物界」

### 5. main 中展示多型

用 `Creature[]` 陣列放入 4 種生物，用 for-each 迴圈呼叫相同方法，展示不同行為：

```java
public static void main(String[] args) {
    final int OCEAN_DEPTH = 11034;
    System.out.println("海洋最深處：" + OCEAN_DEPTH + " 公尺\n");

    Creature[] ecosystem = {
        // 放入 4 個子類別的物件
    };

    for (Creature c : ecosystem) {
        System.out.println(c.describe());
        System.out.println("  分類：" + c.kingdom());
        System.out.println("  移動：" + c.move());
        System.out.println("  覓食：" + c.eat());
        System.out.println("  餵食：" + c.feed());
        System.out.println("  餵食：" + c.feed("小魚"));
        System.out.println("  餵食：" + c.feed("小魚", 3));
        System.out.println();
    }
}
```

---

## 預期輸出範例

```
海洋最深處：11034 公尺

大白鯊（深海）
  分類：動物界
  移動：大白鯊 高速衝刺獵食
  覓食：大白鯊 撕咬獵物
  餵食：大白鯊 正在覓食
  餵食：大白鯊 正在吃 小魚
  餵食：大白鯊 吃了 3 份 小魚

綠蠵龜（珊瑚礁）
  分類：動物界
  移動：綠蠵龜 緩慢划動四肢
  覓食：綠蠵龜 啃食海草
  餵食：綠蠵龜 正在覓食
  餵食：綠蠵龜 正在吃 小魚
  餵食：綠蠵龜 吃了 3 份 小魚

（其他兩種生物...）
```

---

