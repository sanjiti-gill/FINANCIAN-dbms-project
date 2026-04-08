package dao;

import model.Customer;
import util.DBConnection;
import util.FinancianException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerDAO - CRUD for Customer table.
 * FIX: All IDs are VARCHAR (String) to match actual schema.
 */
public class CustomerDAO implements DatabaseOperations<Customer> {

    // ==================== INSERT ====================
    @Override
    public boolean add(Customer customer) {
        // NOTE: Customer_ID is VARCHAR - you must supply it (e.g. "CUST06")
        String sql = "INSERT INTO Customer (Customer_ID, Name, Phone, Email, Address, PAN_Number) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getPanNumber());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("[SUCCESS] Customer added: " + customer.getName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add customer: " + e.getMessage());
        }
        return false;
    }

    // ==================== SELECT ALL ====================
    @Override
    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer ORDER BY Customer_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch customers: " + e.getMessage());
        }
        return list;
    }

    // ==================== SELECT BY ID ====================
    @Override
    public Customer getById(String customerId) {
        String sql = "SELECT * FROM Customer WHERE Customer_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return null;
    }

    // ==================== UPDATE ====================
    @Override
    public boolean update(Customer c) {
        String sql = "UPDATE Customer SET Name=?, Phone=?, Email=?, Address=?, PAN_Number=? WHERE Customer_ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getPhone());
            pstmt.setString(3, c.getEmail());
            pstmt.setString(4, c.getAddress());
            pstmt.setString(5, c.getPanNumber());
            pstmt.setString(6, c.getCustomerId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] Customer updated: " + c.getCustomerId()); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update: " + e.getMessage());
        }
        return false;
    }

    // ==================== DELETE ====================
    @Override
    public boolean delete(String customerId) {
        String sql = "DELETE FROM Customer WHERE Customer_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) { System.out.println("[SUCCESS] Customer deleted: " + customerId); return true; }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete: " + e.getMessage());
        }
        return false;
    }

    // ==================== SEARCH BY NAME ====================
    public List<Customer> searchByName(String name) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE Name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return list;
    }

    // ==================== JOIN: Customer + Account + Loan ====================
    public void viewCustomerLoanSummary() {
        String sql =
            "SELECT c.Customer_ID, c.Name, c.Phone, " +
            "       a.Account_ID, a.Account_Type, a.Balance, " +
            "       l.Loan_ID, l.Loan_Type, l.Principal_Amount, l.Interest_Rate, l.Loan_Tenure " +
            "FROM Customer c " +
            "JOIN Account a ON c.Customer_ID = a.Customer_ID " +
            "JOIN Loan    l ON a.Account_ID  = l.Account_ID " +
            "ORDER BY c.Customer_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n" + "=".repeat(115));
            System.out.printf("%-8s %-20s %-12s | %-8s %-10s %-12s | %-8s %-10s %-14s %-7s %-6s%n",
                "C.ID","Name","Phone","Acc.ID","Type","Balance","Loan ID","Loan Type","Principal","Rate%","Tenure");
            System.out.println("=".repeat(115));

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-8s %-20s %-12s | %-8s %-10s %-12.2f | %-8s %-10s %-14.2f %-7.2f %-6d%n",
                    rs.getString("Customer_ID"), rs.getString("Name"), rs.getString("Phone"),
                    rs.getString("Account_ID"), rs.getString("Account_Type"), rs.getBigDecimal("Balance"),
                    rs.getString("Loan_ID"), rs.getString("Loan_Type"),
                    rs.getBigDecimal("Principal_Amount"), rs.getBigDecimal("Interest_Rate"), rs.getInt("Loan_Tenure")
                );
            }
            if (!found) System.out.println("No records found. Add customers, accounts, and loans first.");
            System.out.println("=".repeat(115));

        } catch (SQLException e) {
            System.err.println("[ERROR] Join query failed: " + e.getMessage());
        }
    }

    // ==================== Custom Exception demo ====================
    public Customer getByIdOrThrow(String id) throws FinancianException {
        Customer c = getById(id);
        if (c == null) throw new FinancianException("Customer '" + id + "' not found.", 404);
        return c;
    }

    // ==================== HELPER ====================
    private Customer map(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getString("Customer_ID"),
            rs.getString("Name"),
            rs.getString("Phone"),
            rs.getString("Email"),
            rs.getString("Address"),
            rs.getString("PAN_Number")
        );
    }
}
