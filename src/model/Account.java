package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Account model class.
 * FIX: Account_ID and Customer_ID are VARCHAR in your schema.
 * Maps to: Account(Account_ID, Account_Type, Balance, Open_Date, Customer_ID)
 */
public class Account {

    private String accountId;     // VARCHAR e.g. "ACC01"
    private String accountType;
    private BigDecimal balance;
    private Date openDate;
    private String customerId;    // VARCHAR FK

    public Account() {}

    public Account(String accountId, String accountType, BigDecimal balance,
                   Date openDate, String customerId) {
        this.accountId   = accountId;
        this.accountType = accountType;
        this.balance     = balance;
        this.openDate    = openDate;
        this.customerId  = customerId;
    }

    public Account(String accountType, BigDecimal balance, Date openDate, String customerId) {
        this.accountType = accountType;
        this.balance     = balance;
        this.openDate    = openDate;
        this.customerId  = customerId;
    }

    public String     getAccountId()              { return accountId; }
    public void       setAccountId(String id)     { this.accountId = id; }
    public String     getAccountType()            { return accountType; }
    public void       setAccountType(String t)    { this.accountType = t; }
    public BigDecimal getBalance()                { return balance; }
    public void       setBalance(BigDecimal b)    { this.balance = b; }
    public Date       getOpenDate()               { return openDate; }
    public void       setOpenDate(Date d)         { this.openDate = d; }
    public String     getCustomerId()             { return customerId; }
    public void       setCustomerId(String id)    { this.customerId = id; }

    @Override
    public String toString() {
        return String.format(
            "Account ID: %-8s | Type: %-10s | Balance: Rs.%-12.2f | Opened: %-12s | Customer ID: %s",
            accountId, accountType, balance, openDate, customerId
        );
    }
}
