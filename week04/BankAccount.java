package week04;

public class BankAccount {
    private String owner;
    private String accountId;
    private double balance;

    public BankAccount(String owner, String accountId) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = 0.0;
    }

    public String getOwner() {
        return owner;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("存款金額必須大於 0");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0) {
            if (balance >= amount) {
                balance -= amount;
            } else {
                System.out.println("餘額不足，無法提取 " + amount);
            }
        } else {
            System.out.println("提款金額必須大於 0");
        }
    }

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