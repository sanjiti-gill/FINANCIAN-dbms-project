package dao;

import model.Account;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountDAO - CRUD for Account table.
 * FIX: All IDs are VARCHAR (String).
 */
public class AccountDAO implements DatabaseOperations<Account> {

    @Override
    public boolean add(Account account) {
        String sql = "INSERT INTO Account (Account_ID, Account_Type, Balance, Open_Date, Customer_ID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getAccountId());
            pstmt.setString(2, account.getAccountType());
            pstmt.setBigDecimal(3, account.getBalance());
            pstmt.setDate(4, account.getOpenDate());
            pstmt.setString(5, account.getCustomerId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] Account added: " + account.getAccountId()); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add account: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Account> getAll() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account ORDER BY Account_ID";
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
    public Account getById(String accountId) {
        String sql = "SELECT * FROM Account WHERE Account_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return null;
    }

    public List<Account> getByCustomerId(String customerId) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE Customer_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(Account a) {
        String sql = "UPDATE Account SET Account_Type=?, Balance=?, Open_Date=?, Customer_ID=? WHERE Account_ID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, a.getAccountType());
            pstmt.setBigDecimal(2, a.getBalance());
            pstmt.setDate(3, a.getOpenDate());
            pstmt.setString(4, a.getCustomerId());
            pstmt.setString(5, a.getAccountId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] Account updated: " + a.getAccountId()); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(String accountId) {
        String sql = "DELETE FROM Account WHERE Account_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] Account deleted: " + accountId); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return false;
    }

    private Account map(ResultSet rs) throws SQLException {
        return new Account(
            rs.getString("Account_ID"),
            rs.getString("Account_Type"),
            rs.getBigDecimal("Balance"),
            rs.getDate("Open_Date"),
            rs.getString("Customer_ID")
        );
    }
}
