class BaseBankAccount {
    private String owner;
    private String accountId;
    private double balance;

    public BaseBankAccount(String owner, String accountId) {
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
        System.out.println("帳戶：" + accountId + " 戶名：" + owner + " 餘額：" + balance);
    }
}

// TODO: SavingsAccount 繼承 BaseBankAccount
class RealSavingsAccount extends BaseBankAccount {
    private double interestRate;

    public RealSavingsAccount(String owner, String accountId, double interestRate) {
        super(owner, accountId);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        double interest = getBalance() * interestRate;
        System.out.println("利息 " + interest + " 已存入");
        deposit(interest); // 呼叫父類別的存款方法
    }
}

public class SavingsAccount {
    public static void main(String[] args) {
        // TODO: 建立物件
        RealSavingsAccount sa = new RealSavingsAccount("李小華", "S001", 0.02);
        
        sa.deposit(10000);
        sa.showInfo();
        
        sa.addInterest();
        sa.showInfo();
    }
}