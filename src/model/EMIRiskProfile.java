package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * EMIRiskProfile model.
 * FIX: All IDs are VARCHAR in your schema.
 */
public class EMIRiskProfile {

    private String stressLevelId;
    private Date riskFlagDate;
    private int missedEmiCount;
    private BigDecimal overdueAmount;
    private String monitoringStatus;
    private Date lastReviewedDate;
    private String emiId;
    private String riskCategoryCode;

    public EMIRiskProfile() {}

    public EMIRiskProfile(String stressLevelId, Date riskFlagDate, int missedEmiCount,
                          BigDecimal overdueAmount, String monitoringStatus,
                          Date lastReviewedDate, String emiId, String riskCategoryCode) {
        this.stressLevelId    = stressLevelId;
        this.riskFlagDate     = riskFlagDate;
        this.missedEmiCount   = missedEmiCount;
        this.overdueAmount    = overdueAmount;
        this.monitoringStatus = monitoringStatus;
        this.lastReviewedDate = lastReviewedDate;
        this.emiId            = emiId;
        this.riskCategoryCode = riskCategoryCode;
    }

    public String     getStressLevelId()                    { return stressLevelId; }
    public void       setStressLevelId(String id)           { this.stressLevelId = id; }
    public Date       getRiskFlagDate()                     { return riskFlagDate; }
    public void       setRiskFlagDate(Date d)               { this.riskFlagDate = d; }
    public int        getMissedEmiCount()                   { return missedEmiCount; }
    public void       setMissedEmiCount(int c)              { this.missedEmiCount = c; }
    public BigDecimal getOverdueAmount()                    { return overdueAmount; }
    public void       setOverdueAmount(BigDecimal a)        { this.overdueAmount = a; }
    public String     getMonitoringStatus()                 { return monitoringStatus; }
    public void       setMonitoringStatus(String s)         { this.monitoringStatus = s; }
    public Date       getLastReviewedDate()                 { return lastReviewedDate; }
    public void       setLastReviewedDate(Date d)           { this.lastReviewedDate = d; }
    public String     getEmiId()                            { return emiId; }
    public void       setEmiId(String id)                   { this.emiId = id; }
    public String     getRiskCategoryCode()                 { return riskCategoryCode; }
    public void       setRiskCategoryCode(String c)         { this.riskCategoryCode = c; }

    @Override
    public String toString() {
        return String.format(
            "Stress ID: %-8s | EMI ID: %-8s | Missed: %-3d | Overdue: Rs.%-10.2f | Status: %-10s | Risk: %s",
            stressLevelId, emiId, missedEmiCount, overdueAmount, monitoringStatus, riskCategoryCode
        );
    }
}
