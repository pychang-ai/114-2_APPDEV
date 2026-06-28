// 父類別維持不變
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
        if (amount > 0 && amount <= balance) {
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
class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String owner, String accountId, double interestRate) {
        super(owner, accountId); // 呼叫父類別建構子
        this.interestRate = interestRate;
    }

    // 新增 addInterest 方法
    public void addInterest() {
        double interest = getBalance() * interestRate; // 透過 getter 取得餘額
        System.out.println("利息 " + interest + " 已存入");
        deposit(interest); // 透過存款方法更新餘額
    }
}

public class SavingsAccount {
    public static void main(String[] args) {
        // TODO: 建立 SavingsAccount
        SavingsAccount acc = new SavingsAccount("李小華", "S001", 0.02);
        
        acc.deposit(10000);
        acc.showInfo();     // 帳戶：S001 戶名：李小華 餘額：10000.0
        
        acc.addInterest();  // 呼叫 addInterest()
        
        acc.showInfo();     // 帳戶：S001 戶名：李小華 餘額：10200.0
    }
}
