package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Payment model class.
 * Maps to: Payment(Payment_ID, Payment_Date, Amount_Paid, Payment_Mode, Late_Fee, EMI_ID)
 */
public class Payment {

    private int paymentId;
    private Date paymentDate;
    private BigDecimal amountPaid;
    private String paymentMode;   // "Online", "Cash", "Cheque", "UPI"
    private BigDecimal lateFee;
    private int emiId;

    // Default constructor
    public Payment() {}

    // Full constructor
    public Payment(int paymentId, Date paymentDate, BigDecimal amountPaid,
                   String paymentMode, BigDecimal lateFee, int emiId) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentMode = paymentMode;
        this.lateFee = lateFee;
        this.emiId = emiId;
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public BigDecimal getLateFee() { return lateFee; }
    public void setLateFee(BigDecimal lateFee) { this.lateFee = lateFee; }

    public int getEmiId() { return emiId; }
    public void setEmiId(int emiId) { this.emiId = emiId; }

    @Override
    public String toString() {
        return String.format(
            "Payment ID: %-5d | Date: %-12s | Paid: ₹%-10.2f | Mode: %-10s | Late Fee: ₹%.2f | EMI ID: %d",
            paymentId, paymentDate, amountPaid, paymentMode, lateFee, emiId
        );
    }
}
