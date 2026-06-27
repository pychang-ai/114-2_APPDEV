package week04;

class BankAccount {
    // 加上 final，因為戶名和帳號通常不會變動
    private final String owner;
    private final String accountId;
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
            System.out.println("成功存款：" + amount + " 元");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("成功提款：" + amount + " 元");
        } else {
            System.out.println("提款失敗：餘額不足或金額錯誤");
        }
    }

    public void showInfo() {
        System.out.println("--------------------------------");
        System.out.println("帳戶編號：" + accountId);
        System.out.println("持 有 人：" + owner);
        System.out.println("目前餘額：" + balance + " 元");
    }
}

public class SavingsAccount extends BankAccount {
    private final double interestRate;

    public SavingsAccount(String owner, String accountId, double interestRate) {
        super(owner, accountId);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        // 計算利息並存入
        double interest = getBalance() * interestRate;
        System.out.println(">>> 結算利息：年利率 " + (interestRate * 100) + "%");
        deposit(interest); // 透過 deposit 存入，保持邏輯一致性
    }

    public static void main(String[] args) {
        System.out.println("=== 銀行系統執行中 ===");
        
        // 建立儲蓄帳戶：利率 2%
        SavingsAccount savingsAcc = new SavingsAccount("李小華", "S001", 0.02);   
        
        savingsAcc.deposit(10000); // 存入一萬
        savingsAcc.showInfo();
        
        savingsAcc.addInterest();  // 結算利息
        savingsAcc.showInfo();
    }
}