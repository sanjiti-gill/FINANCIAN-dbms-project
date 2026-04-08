package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * EMI model class.
 * FIX: EMI_ID and Loan_ID are VARCHAR in your schema.
 * Maps to: EMI(EMI_ID, EMI_Amount, Due_Date, Paid_Status, Loan_ID)
 */
public class EMI {

    private String emiId;        // VARCHAR e.g. "EMI01"
    private BigDecimal emiAmount;
    private Date dueDate;
    private String paidStatus;   // "Paid", "Unpaid", "Overdue"
    private String loanId;       // VARCHAR FK

    public EMI() {}

    public EMI(String emiId, BigDecimal emiAmount, Date dueDate, String paidStatus, String loanId) {
        this.emiId      = emiId;
        this.emiAmount  = emiAmount;
        this.dueDate    = dueDate;
        this.paidStatus = paidStatus;
        this.loanId     = loanId;
    }

    public EMI(BigDecimal emiAmount, Date dueDate, String paidStatus, String loanId) {
        this.emiAmount  = emiAmount;
        this.dueDate    = dueDate;
        this.paidStatus = paidStatus;
        this.loanId     = loanId;
    }

    public String     getEmiId()                    { return emiId; }
    public void       setEmiId(String id)           { this.emiId = id; }
    public BigDecimal getEmiAmount()                { return emiAmount; }
    public void       setEmiAmount(BigDecimal a)    { this.emiAmount = a; }
    public Date       getDueDate()                  { return dueDate; }
    public void       setDueDate(Date d)            { this.dueDate = d; }
    public String     getPaidStatus()               { return paidStatus; }
    public void       setPaidStatus(String s)       { this.paidStatus = s; }
    public String     getLoanId()                   { return loanId; }
    public void       setLoanId(String id)          { this.loanId = id; }

    @Override
    public String toString() {
        return String.format(
            "EMI ID: %-8s | Amount: Rs.%-10.2f | Due: %-12s | Status: %-8s | Loan ID: %s",
            emiId, emiAmount, dueDate, paidStatus, loanId
        );
    }
}
