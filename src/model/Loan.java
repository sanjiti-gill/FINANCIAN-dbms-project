package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Loan model class.
 * FIX: Loan_ID and Account_ID are VARCHAR in your schema.
 * Maps to: Loan(Loan_ID, Loan_Type, Principal_Amount, Interest_Rate, Loan_Tenure, Start_Date, Account_ID)
 */
public class Loan {

    private String loanId;          // VARCHAR e.g. "LOAN01"
    private String loanType;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private int loanTenure;
    private Date startDate;
    private String accountId;       // VARCHAR FK

    public Loan() {}

    public Loan(String loanId, String loanType, BigDecimal principalAmount,
                BigDecimal interestRate, int loanTenure, Date startDate, String accountId) {
        this.loanId          = loanId;
        this.loanType        = loanType;
        this.principalAmount = principalAmount;
        this.interestRate    = interestRate;
        this.loanTenure      = loanTenure;
        this.startDate       = startDate;
        this.accountId       = accountId;
    }

    public Loan(String loanType, BigDecimal principalAmount, BigDecimal interestRate,
                int loanTenure, Date startDate, String accountId) {
        this.loanType        = loanType;
        this.principalAmount = principalAmount;
        this.interestRate    = interestRate;
        this.loanTenure      = loanTenure;
        this.startDate       = startDate;
        this.accountId       = accountId;
    }

    public String     getLoanId()                       { return loanId; }
    public void       setLoanId(String id)              { this.loanId = id; }
    public String     getLoanType()                     { return loanType; }
    public void       setLoanType(String t)             { this.loanType = t; }
    public BigDecimal getPrincipalAmount()              { return principalAmount; }
    public void       setPrincipalAmount(BigDecimal a)  { this.principalAmount = a; }
    public BigDecimal getInterestRate()                 { return interestRate; }
    public void       setInterestRate(BigDecimal r)     { this.interestRate = r; }
    public int        getLoanTenure()                   { return loanTenure; }
    public void       setLoanTenure(int t)              { this.loanTenure = t; }
    public Date       getStartDate()                    { return startDate; }
    public void       setStartDate(Date d)              { this.startDate = d; }
    public String     getAccountId()                    { return accountId; }
    public void       setAccountId(String id)           { this.accountId = id; }

    @Override
    public String toString() {
        return String.format(
            "Loan ID: %-8s | Type: %-10s | Principal: Rs.%-12.2f | Rate: %-6.2f%% | Tenure: %-4d months | Start: %s",
            loanId, loanType, principalAmount, interestRate, loanTenure, startDate
        );
    }
}
