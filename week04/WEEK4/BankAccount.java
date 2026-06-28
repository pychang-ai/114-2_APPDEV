public class BankAccount {
    // TODO 1: 宣告 private 屬性
    private String owner;
    private String accountId;
    private double balance;

    // TODO 2: 建構子
    public BankAccount(String owner, String accountId) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = 0.0; // 初始餘額為 0
    }

    // TODO 3: getter 方法
    public String getOwner() {
        return owner;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    // TODO 4: deposit 方法，存入金額
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("成功存入: " + amount);
        } else {
            System.out.println("錯誤：存入金額必須大於 0");
        }
    }

    // TODO 5: withdraw 方法，提取金額
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("錯誤：提款金額必須大於 0");
        } else if (amount <= balance) {
            balance -= amount;
            System.out.println("成功提取: " + amount);
        } else {
            System.out.println("錯誤：餘額不足，無法提款");
        }
    }

    // TODO 6: showInfo 方法，印出帳戶資訊
    public void showInfo() {
        System.out.println("帳戶資訊 - 持有人: " + owner + ", 帳號: " + accountId + ", 目前餘額: " + balance);
    }

    public static void main(String[] args) {
        BankAccount acc = new BankAccount("王小明", "A001");
        acc.deposit(1000);
        acc.showInfo();     // 餘額應為 1000
        acc.withdraw(300);
        acc.showInfo();     // 餘額應為 700
        acc.withdraw(800);   // 應印出餘額不足提示
        acc.deposit(-50);    // 應印出金額錯誤提示
    }
}
