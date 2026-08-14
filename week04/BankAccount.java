public class BankAccount {
    // TODO 1: 宣告 private 屬性
    private String owner;
    private String accountId;
    private double balance;

    // TODO 2: 建構子
    public BankAccount(String owner, String accountId) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = 0; // 初始餘額為 0
    }

    // TODO 3: getter 方法
    public String getOwner() { return owner; }
    public String getAccountId() { return accountId; }
    public double getBalance() { return balance; }

    // TODO 4: deposit 方法
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("存款金額必須大於 0");
        }
    }

    // TODO 5: withdraw 方法
    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("餘額不足，無法提取 " + amount);
        } else if (amount <= 0) {
            System.out.println("提款金額必須大於 0");
        } else {
            balance -= amount;
        }
    }

    // TODO 6: showInfo 方法
    public void showInfo() {
        System.out.println("帳戶：" + accountId + " 戶名：" + owner + " 餘額：" + balance);
    }

    public static void main(String[] args) {
        BankAccount acc = new BankAccount("王小明", "A001");
        acc.deposit(1000);
        acc.showInfo();      
        acc.withdraw(300);
        acc.showInfo();      
        acc.withdraw(800);   
        acc.deposit(-50);    
    }
}