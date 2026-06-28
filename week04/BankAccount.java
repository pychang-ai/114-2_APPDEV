package week04;

public class BankAccount {
    // TODO 1: 宣告 private 屬性：owner(String), accountId(String), balance(double)
    private String owner, accountId;
    private double balance;

    // TODO 2: 建構子，接收 owner 和 accountId，餘額初始為 0
    public BankAccount(String owner, String accountId) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = 0;
    }

    // TODO 3: getter 方法（getOwner, getAccountId, getBalance）
    public String getOwner() {
        return owner;
    }

    public String getAccountId() {
        return accountId;
    }
    
    public double getBalance() {
        return balance;
    }

    // TODO 4: deposit 方法，存入金額（金額必須大於 0）
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("存款金額必須大於 0");
        }
    }

    // TODO 5: withdraw 方法，提取金額（餘額不足要印出提示）
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else{
            System.out.println("餘額不足，無法提取 " + amount);
        }
    }

    // TODO 6: showInfo 方法，印出帳戶資訊
    public void showInfo() {
        System.out.println("帳戶：" + owner + " 戶名：" + accountId + " 餘額：" + balance);
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
