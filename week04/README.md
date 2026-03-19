
# W04 課堂練習：封裝與繼承

> **APP 開發課程** ｜ 第 4 週 ｜ 課堂實作練習  
> **工具**：VS Code + Extension Pack for Java  
> **時間**：約 40 分鐘  
> **繳交**：完成後 push 到自己的 Fork，發 PR 繳交

---

## 練習 1：封裝（15 分鐘）

**檔案名稱**：`week04/BankAccount.java`

請建立一個銀行帳戶類別，運用封裝保護帳戶資料：

1. 屬性全部設為 `private`
2. 透過 `getter` 讀取資料
3. 透過方法控制存款和提款，提款需檢查餘額

**骨架程式碼**：

```java
public class BankAccount {
    // TODO 1: 宣告 private 屬性：owner(String), accountId(String), balance(double)


    // TODO 2: 建構子，接收 owner 和 accountId，餘額初始為 0


    // TODO 3: getter 方法（getOwner, getAccountId, getBalance）


    // TODO 4: deposit 方法，存入金額（金額必須大於 0）
    public void deposit(double amount) {

    }

    // TODO 5: withdraw 方法，提取金額（餘額不足要印出提示）
    public void withdraw(double amount) {

    }

    // TODO 6: showInfo 方法，印出帳戶資訊
    public void showInfo() {

    }

    public static void main(String[] args) {
        BankAccount acc = new BankAccount("王小明", "A001");
        acc.deposit(1000);
        acc.showInfo();      // 餘額應為 1000
        acc.withdraw(300);
        acc.showInfo();      // 餘額應為 700
        acc.withdraw(800);   // 應印出餘額不足提示
        acc.deposit(-50);    // 應印出金額錯誤提示
    }
}
```

**預期輸出**：
```
帳戶：A001 戶名：王小明 餘額：1000.0
帳戶：A001 戶名：王小明 餘額：700.0
餘額不足，無法提取 800.0
存款金額必須大於 0
```

---

## 練習 2：繼承（15 分鐘）

**檔案名稱**：`week04/MarineLife.java`

請建立海洋生物的繼承體系：

1. 父類別 `MarineLife`：所有海洋生物的共同屬性和方法
2. 子類別 `Fish`：魚類，有特定行為
3. 子類別 `Whale`：鯨魚，有特定行為
4. 使用 `extends` 和 `super`

**骨架程式碼**：

```java
// 父類別
class MarineLife {
    protected String name;
    protected String habitat;

    public MarineLife(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    public String info() {
        return name + "，棲息地：" + habitat;
    }

    public String move() {
        return name + " 在水中移動";
    }
}

// TODO 1: Fish 繼承 MarineLife
// - 新增屬性 scaleColor（鱗片顏色）
// - 建構子接收 name, habitat, scaleColor，用 super 呼叫父類別建構子
// - 覆寫 move()，回傳「[name] 擺動魚鰭游泳」
// - 新增方法 fishInfo()，回傳「[info()] 鱗片：[scaleColor]」


// TODO 2: Whale 繼承 MarineLife
// - 新增屬性 length（體長，單位公尺）
// - 建構子接收 name, habitat, length，用 super 呼叫父類別建構子
// - 覆寫 move()，回傳「[name] 擺動尾鰭前進」
// - 新增方法 whaleInfo()，回傳「[info()] 體長：[length] 公尺」


public class MarineLife {
    // 注意：檔名是 MarineLife.java，所以這個 class 要加 public
    // 上面的 MarineLife class 不加 public

    public static void main(String[] args) {
        // TODO 3: 建立 Fish 和 Whale 各一個
        // 印出 fishInfo() / whaleInfo() 和 move()

        // 預期輸出：
        // 小丑魚，棲息地：珊瑚礁 鱗片：橘白相間
        // 小丑魚 擺動魚鰭游泳
        // 藍鯨，棲息地：太平洋 體長：30.0 公尺
        // 藍鯨 擺動尾鰭前進
    }
}
```

**注意**：一個檔案中有多個 class 時，只有和檔名相同的 class 可以加 `public`，其他 class 不加。

---

## 練習 3：封裝 + 繼承綜合（10 分鐘）

**檔案名稱**：`week04/SavingsAccount.java`

請結合練習 1 的 BankAccount，建立一個有利息的儲蓄帳戶：

1. `SavingsAccount` 繼承 `BankAccount`
2. 新增 `interestRate` 屬性（年利率）
3. 新增 `addInterest()` 方法，計算並存入利息

**骨架程式碼**：

```java
class BankAccount {
    private String owner;
    private String accountId;
    private double balance;

    public BankAccount(String owner, String accountId) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = 0;
    }

    public String getOwner() { return owner; }
    public String getAccountId() { return accountId; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        }
    }

    public void showInfo() {
        System.out.println("帳戶：" + accountId +
            " 戶名：" + owner +
            " 餘額：" + balance);
    }
}

// TODO: SavingsAccount 繼承 BankAccount
// - 新增 private double interestRate
// - 建構子接收 owner, accountId, interestRate
//   用 super(owner, accountId) 呼叫父類別建構子
// - addInterest() 方法：利息 = getBalance() * interestRate
//   印出「利息 xxx 已存入」，然後呼叫 deposit(利息)


public class SavingsAccount {

    public static void main(String[] args) {
        // TODO: 建立 SavingsAccount，年利率 0.02 (2%)
        // 存入 10000 元
        // 呼叫 addInterest()
        // 印出帳戶資訊

        // 預期輸出：
        // 帳戶：S001 戶名：李小華 餘額：10000.0
        // 利息 200.0 已存入
        // 帳戶：S001 戶名：李小華 餘額：10200.0
    }
}
```

---

## 繳交方式

完成後將三個檔案放在 `week04/` 資料夾內，push 到你的 Fork，發 PR 繳交：

```bash
cd 你的Fork資料夾
mkdir -p week04
# 把三個 .java 檔放進 week04/
git add week04/
git commit -m "W04 課堂練習：封裝與繼承"
git push origin main
# 到 GitHub 網頁發 PR
```

---

## 練習對應表

| 練習 | 主題 | 難度 | 時間 | 對應 GFG 教材 |
|:----:|------|:----:|:----:|-----------|
| 1 | 封裝 | 簡單 | 15 min | [Encapsulation](https://www.geeksforgeeks.org/java/encapsulation-in-java/) |
| 2 | 繼承 | 中等 | 15 min | [Inheritance](https://www.geeksforgeeks.org/java/inheritance-in-java/) |
| 3 | 封裝+繼承 | 中等 | 10 min | [Access Modifiers](https://www.geeksforgeeks.org/java/access-modifiers-java/) |
