package model;

/**
 * RiskCategory model class.
 * Maps to: Risk_Category(Risk_Category_Code, Risk_Level, Description)
 */
public class RiskCategory {

    private String riskCategoryCode;  // e.g. "RC001"
    private String riskLevel;         // e.g. "Low", "Medium", "High", "Critical"
    private String description;

    // Default constructor
    public RiskCategory() {}

    // Full constructor
    public RiskCategory(String riskCategoryCode, String riskLevel, String description) {
        this.riskCategoryCode = riskCategoryCode;
        this.riskLevel = riskLevel;
        this.description = description;
    }

    // Getters and Setters
    public String getRiskCategoryCode() { return riskCategoryCode; }
    public void setRiskCategoryCode(String riskCategoryCode) { this.riskCategoryCode = riskCategoryCode; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format(
            "Code: %-8s | Risk Level: %-10s | Description: %s",
            riskCategoryCode, riskLevel, description
        );
    }
}
