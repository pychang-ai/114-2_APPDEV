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

class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String owner, String accountId, double interestRate) {
        super(owner, accountId);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        double interest = getBalance() * interestRate;
        System.out.println("利息 " + interest + " 已存入");
        deposit(interest);
    }
}

public class SavingsAccount {

    public static void main(String[] args) {
        // TODO: 建立 SavingsAccount，年利率 0.02 (2%)
        // 存入 10000 元
        // 呼叫 addInterest()
        // 印出帳戶資訊

        SavingsAccount acc = new SavingsAccount("李小華", "S001", 0.02);

        acc.deposit(10000);
        acc.showInfo();

        acc.addInterest();
        acc.showInfo();

        // 預期輸出：
        // 帳戶：S001 戶名：李小華 餘額：10000.0
        // 利息 200.0 已存入
        // 帳戶：S001 戶名：李小華 餘額：10200.0
    }
}
