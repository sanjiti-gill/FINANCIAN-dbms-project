package dao;

import model.EMI;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EMIDAO - CRUD for EMI table + Risk report + Stored Procedure.
 * FIX: All IDs are VARCHAR (String).
 */
public class EMIDAO implements DatabaseOperations<EMI> {

    @Override
    public boolean add(EMI emi) {
        String sql = "INSERT INTO EMI (EMI_ID, EMI_Amount, Due_Date, Paid_Status, Loan_ID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, emi.getEmiId());
            pstmt.setBigDecimal(2, emi.getEmiAmount());
            pstmt.setDate(3, emi.getDueDate());
            pstmt.setString(4, emi.getPaidStatus());
            pstmt.setString(5, emi.getLoanId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] EMI added: " + emi.getEmiId()); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add EMI: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<EMI> getAll() {
        List<EMI> list = new ArrayList<>();
        String sql = "SELECT * FROM EMI ORDER BY EMI_ID";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return list;
    }

    @Override
    public EMI getById(String emiId) {
        String sql = "SELECT * FROM EMI WHERE EMI_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, emiId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return null;
    }

    public List<EMI> getByLoanId(String loanId) {
        List<EMI> list = new ArrayList<>();
        String sql = "SELECT * FROM EMI WHERE Loan_ID = ? ORDER BY Due_Date";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loanId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(EMI emi) {
        String sql = "UPDATE EMI SET EMI_Amount=?, Due_Date=?, Paid_Status=? WHERE EMI_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, emi.getEmiAmount());
            pstmt.setDate(2, emi.getDueDate());
            pstmt.setString(3, emi.getPaidStatus());
            pstmt.setString(4, emi.getEmiId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] EMI updated: " + emi.getEmiId()); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(String emiId) {
        String sql = "DELETE FROM EMI WHERE EMI_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, emiId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] EMI deleted: " + emiId); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return false;
    }

    // ==================== MARK AS PAID ====================
    public boolean markEMIAsPaid(String emiId) {
        String sql = "UPDATE EMI SET Paid_Status = 'Paid' WHERE EMI_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, emiId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] EMI " + emiId + " marked as PAID."); return true; }
            else System.out.println("[INFO] EMI not found: " + emiId);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return false;
    }

    // ==================== EMI RISK REPORT (5-table JOIN) ====================
    public void viewEMIRiskReport() {
        String sql =
            "SELECT c.Name AS Customer_Name, c.Phone, " +
            "       l.Loan_Type, l.Principal_Amount, " +
            "       e.EMI_ID, e.EMI_Amount, e.Due_Date, e.Paid_Status, " +
            "       rp.Missed_EMI_Count, rp.Overdue_Amount, rp.Monitoring_Status, " +
            "       rc.Risk_Level, rc.Description AS Risk_Description " +
            "FROM EMI e " +
            "JOIN Loan l             ON e.Loan_ID            = l.Loan_ID " +
            "JOIN Account a          ON l.Account_ID         = a.Account_ID " +
            "JOIN Customer c         ON a.Customer_ID        = c.Customer_ID " +
            "JOIN EMI_Risk_Profile rp ON e.EMI_ID            = rp.EMI_ID " +
            "JOIN Risk_Category rc   ON rp.Risk_Category_Code = rc.Risk_Category_Code " +
            "ORDER BY rc.Risk_Level DESC, rp.Missed_EMI_Count DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n" + "=".repeat(115));
            System.out.println("              *** EMI FINANCIAL STRESS & EARLY WARNING REPORT ***");
            System.out.println("=".repeat(115));
            System.out.printf("%-18s %-12s %-10s %-10s %-8s %-12s %-8s %-7s %-10s%n",
                "Customer","Phone","Loan Type","EMI Amt","EMI ID","Due Date","Status","Missed","Risk Level");
            System.out.println("-".repeat(115));

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-18s %-12s %-10s %-10.2f %-8s %-12s %-8s %-7d %-10s%n",
                    rs.getString("Customer_Name"), rs.getString("Phone"),
                    rs.getString("Loan_Type"), rs.getBigDecimal("EMI_Amount"),
                    rs.getString("EMI_ID"), rs.getDate("Due_Date"),
                    rs.getString("Paid_Status"), rs.getInt("Missed_EMI_Count"),
                    rs.getString("Risk_Level")
                );
                System.out.println("  -> Overdue: Rs." + rs.getBigDecimal("Overdue_Amount")
                    + " | Monitoring: " + rs.getString("Monitoring_Status")
                    + " | " + rs.getString("Risk_Description"));
                System.out.println("-".repeat(115));
            }
            if (!found) System.out.println("No at-risk EMI records found.");
            System.out.println("=".repeat(115));

        } catch (SQLException e) {
            System.err.println("[ERROR] Risk report failed: " + e.getMessage());
        }
    }

    // ==================== CALLABLE STATEMENT (Stored Procedure) ====================
    public void callGetOverdueEMIs(String loanId) {
        String call = "{CALL GetOverdueEMIsByLoan(?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(call)) {

            cstmt.setString(1, loanId);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.registerOutParameter(3, Types.DECIMAL);
            cstmt.execute();

            System.out.println("\n--- Stored Procedure Result ---");
            System.out.println("Loan ID             : " + loanId);
            System.out.println("Missed/Overdue EMIs : " + cstmt.getInt(2));
            System.out.printf("Total Overdue Amount: Rs.%.2f%n", cstmt.getDouble(3));

        } catch (SQLException e) {
            System.err.println("[ERROR] Stored procedure failed: " + e.getMessage());
            System.err.println("  -> Make sure stored_procedure.sql has been run in MySQL Workbench.");
        }
    }

    private EMI map(ResultSet rs) throws SQLException {
        return new EMI(
            rs.getString("EMI_ID"),
            rs.getBigDecimal("EMI_Amount"),
            rs.getDate("Due_Date"),
            rs.getString("Paid_Status"),
            rs.getString("Loan_ID")
        );
    }
}
