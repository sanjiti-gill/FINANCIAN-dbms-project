package dao;

import model.Loan;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LoanDAO - CRUD for Loan table.
 * FIX: All IDs are VARCHAR (String).
 */
public class LoanDAO implements DatabaseOperations<Loan> {

    @Override
    public boolean add(Loan loan) {
        String sql = "INSERT INTO Loan (Loan_ID, Loan_Type, Principal_Amount, Interest_Rate, Loan_Tenure, Start_Date, Account_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loan.getLoanId());
            pstmt.setString(2, loan.getLoanType());
            pstmt.setBigDecimal(3, loan.getPrincipalAmount());
            pstmt.setBigDecimal(4, loan.getInterestRate());
            pstmt.setInt(5, loan.getLoanTenure());
            pstmt.setDate(6, loan.getStartDate());
            pstmt.setString(7, loan.getAccountId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] Loan added: " + loan.getLoanId()); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add loan: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Loan> getAll() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM Loan ORDER BY Loan_ID";
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
    public Loan getById(String loanId) {
        String sql = "SELECT * FROM Loan WHERE Loan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loanId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return null;
    }

    public List<Loan> getByAccountId(String accountId) {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM Loan WHERE Account_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(Loan loan) {
        String sql = "UPDATE Loan SET Loan_Type=?, Principal_Amount=?, Interest_Rate=?, Loan_Tenure=?, Start_Date=? WHERE Loan_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loan.getLoanType());
            pstmt.setBigDecimal(2, loan.getPrincipalAmount());
            pstmt.setBigDecimal(3, loan.getInterestRate());
            pstmt.setInt(4, loan.getLoanTenure());
            pstmt.setDate(5, loan.getStartDate());
            pstmt.setString(6, loan.getLoanId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] Loan updated: " + loan.getLoanId()); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(String loanId) {
        String sql = "DELETE FROM Loan WHERE Loan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loanId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] Loan deleted: " + loanId); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return false;
    }

    // ==================== JOIN: Loan + EMI count summary ====================
    public void viewLoanEMISummary() {
        String sql =
            "SELECT l.Loan_ID, l.Loan_Type, l.Principal_Amount, l.Interest_Rate, l.Loan_Tenure, " +
            "       COUNT(e.EMI_ID) AS Total_EMIs, " +
            "       SUM(CASE WHEN e.Paid_Status = 'Paid'    THEN 1 ELSE 0 END) AS Paid_EMIs, " +
            "       SUM(CASE WHEN e.Paid_Status != 'Paid'   THEN 1 ELSE 0 END) AS Pending_EMIs " +
            "FROM Loan l " +
            "LEFT JOIN EMI e ON l.Loan_ID = e.Loan_ID " +
            "GROUP BY l.Loan_ID, l.Loan_Type, l.Principal_Amount, l.Interest_Rate, l.Loan_Tenure " +
            "ORDER BY l.Loan_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n" + "=".repeat(90));
            System.out.printf("%-8s %-12s %-14s %-7s %-10s %-10s %-6s %-8s%n",
                "Loan ID","Type","Principal(Rs.)","Rate%","Tenure(mo)","Total EMIs","Paid","Pending");
            System.out.println("=".repeat(90));

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-8s %-12s %-14.2f %-7.2f %-10d %-10d %-6d %-8d%n",
                    rs.getString("Loan_ID"), rs.getString("Loan_Type"),
                    rs.getBigDecimal("Principal_Amount"), rs.getBigDecimal("Interest_Rate"),
                    rs.getInt("Loan_Tenure"), rs.getInt("Total_EMIs"),
                    rs.getInt("Paid_EMIs"), rs.getInt("Pending_EMIs")
                );
            }
            if (!found) System.out.println("No loan records found.");
            System.out.println("=".repeat(90));

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    private Loan map(ResultSet rs) throws SQLException {
        return new Loan(
            rs.getString("Loan_ID"),
            rs.getString("Loan_Type"),
            rs.getBigDecimal("Principal_Amount"),
            rs.getBigDecimal("Interest_Rate"),
            rs.getInt("Loan_Tenure"),
            rs.getDate("Start_Date"),
            rs.getString("Account_ID")
        );
    }
}
