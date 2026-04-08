package main;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.EMIDAO;
import dao.LoanDAO;
import model.Account;
import model.Customer;
import model.EMI;
import model.Loan;
import util.DBConnection;
import util.FinancianException;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

/**
 * MainMenu - Entry point for the FINANCIAN application.
 * Console-based, menu-driven program.
 * NOTE: All IDs are VARCHAR strings to match your schema (e.g. "CUST01", "ACC01").
 */
public class MainMenu {

    static CustomerDAO customerDAO = new CustomerDAO();
    static AccountDAO  accountDAO  = new AccountDAO();
    static LoanDAO     loanDAO     = new LoanDAO();
    static EMIDAO      emiDAO      = new EMIDAO();
    static Scanner     sc          = new Scanner(System.in);

    public static void main(String[] args) {
        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");

            if      (choice == 1)  addCustomer();
            else if (choice == 2)  viewAllCustomers();
            else if (choice == 3)  searchCustomer();
            else if (choice == 4)  updateCustomer();
            else if (choice == 5)  deleteCustomer();
            else if (choice == 6)  addAccount();
            else if (choice == 7)  viewAllAccounts();
            else if (choice == 8)  viewAccountsByCustomer();
            else if (choice == 9)  addLoan();
            else if (choice == 10) viewAllLoans();
            else if (choice == 11) viewLoanEMISummary();
            else if (choice == 12) addEMI();
            else if (choice == 13) viewEMIsByLoan();
            else if (choice == 14) markEMIAsPaid();
            else if (choice == 15) updateEMIStatus();
            else if (choice == 16) viewCustomerLoanReport();
            else if (choice == 17) viewEMIRiskReport();
            else if (choice == 18) callOverdueEMIProcedure();
            else if (choice == 0) {
                System.out.println("\n  Thank you for using FINANCIAN. Goodbye!\n");
                DBConnection.closeConnection();
                running = false;
            } else {
                System.out.println("  [!] Invalid choice. Please try again.");
            }
        }
        sc.close();
    }

    // ================================================================
    //  BANNER
    // ================================================================
    static void printBanner() {
        System.out.println("\n");
        System.out.println("  ========================================================");
        System.out.println("    FINANCIAN - EMI Financial Stress & Early Warning System");
        System.out.println("    DBMS Mini Project | Second Year Computer Engineering");
        System.out.println("  ========================================================");
    }

    // ================================================================
    //  MENU
    // ================================================================
    static void printMainMenu() {
        System.out.println("\n  +--------------------------------------------------+");
        System.out.println("  |                  MAIN MENU                      |");
        System.out.println("  +--------------------------------------------------+");
        System.out.println("  |  CUSTOMER                                        |");
        System.out.println("  |   1. Add Customer                                |");
        System.out.println("  |   2. View All Customers                          |");
        System.out.println("  |   3. Search Customer by Name                     |");
        System.out.println("  |   4. Update Customer                             |");
        System.out.println("  |   5. Delete Customer                             |");
        System.out.println("  +--------------------------------------------------+");
        System.out.println("  |  ACCOUNT                                         |");
        System.out.println("  |   6. Add Account                                 |");
        System.out.println("  |   7. View All Accounts                           |");
        System.out.println("  |   8. View Accounts by Customer ID                |");
        System.out.println("  +--------------------------------------------------+");
        System.out.println("  |  LOAN                                            |");
        System.out.println("  |   9. Add Loan                                    |");
        System.out.println("  |  10. View All Loans                              |");
        System.out.println("  |  11. View Loan + EMI Summary (JOIN)              |");
        System.out.println("  +--------------------------------------------------+");
        System.out.println("  |  EMI                                             |");
        System.out.println("  |  12. Add EMI                                     |");
        System.out.println("  |  13. View EMIs by Loan ID                        |");
        System.out.println("  |  14. Mark EMI as Paid                            |");
        System.out.println("  |  15. Update EMI Status                           |");
        System.out.println("  +--------------------------------------------------+");
        System.out.println("  |  REPORTS                                         |");
        System.out.println("  |  16. Customer + Account + Loan Report (JOIN)     |");
        System.out.println("  |  17. EMI Risk Profile Report (JOIN)              |");
        System.out.println("  |  18. Overdue EMI Report (Stored Procedure)       |");
        System.out.println("  +--------------------------------------------------+");
        System.out.println("  |   0. Exit                                        |");
        System.out.println("  +--------------------------------------------------+");
    }

    // ================================================================
    //  CUSTOMER
    // ================================================================
    static void addCustomer() {
        System.out.println("\n  --- Add New Customer ---");
        System.out.print("  Customer ID (e.g. CUST06) : "); String id   = sc.nextLine().trim().toUpperCase();
        System.out.print("  Name                      : "); String name = sc.nextLine().trim();
        System.out.print("  Phone                     : "); String phone = sc.nextLine().trim();
        System.out.print("  Email                     : "); String email = sc.nextLine().trim();
        System.out.print("  Address                   : "); String addr  = sc.nextLine().trim();
        System.out.print("  PAN Number (e.g. ABCDE1234F): "); String pan = sc.nextLine().trim().toUpperCase();

        try {
            if (id.isEmpty() || name.isEmpty() || pan.isEmpty())
                throw new FinancianException("ID, Name, and PAN cannot be empty.", 400);
            if (pan.length() != 10)
                throw new FinancianException("PAN must be exactly 10 characters.", 400);

            Customer c = new Customer(id, name, phone, email, addr, pan);
            customerDAO.add(c);
        } catch (FinancianException e) {
            System.out.println("  [VALIDATION] " + e.getMessage());
        }
    }

    static void viewAllCustomers() {
        System.out.println("\n  --- All Customers ---");
        List<Customer> list = customerDAO.getAll();
        if (list.isEmpty()) { System.out.println("  No customers found."); return; }
        System.out.println("  " + "-".repeat(100));
        for (Customer c : list) System.out.println("  " + c.getDisplayInfo()); // POLYMORPHISM
        System.out.println("  " + "-".repeat(100));
        System.out.println("  Total: " + list.size() + " customer(s).");
    }

    static void searchCustomer() {
        System.out.println("\n  --- Search Customer by Name ---");
        System.out.print("  Enter name: ");
        String name = sc.nextLine().trim();
        List<Customer> results = customerDAO.searchByName(name);
        if (results.isEmpty()) System.out.println("  No customers found matching: " + name);
        else results.forEach(c -> System.out.println("  " + c));
    }

    static void updateCustomer() {
        System.out.println("\n  --- Update Customer ---");
        System.out.print("  Enter Customer ID to update (e.g. CUST01): ");
        String id = sc.nextLine().trim().toUpperCase();

        try {
            Customer c = customerDAO.getByIdOrThrow(id);
            System.out.println("  Current: " + c);
            System.out.println("  Press Enter to keep current value.");

            System.out.print("  Name    [" + c.getName()    + "]: "); String name  = sc.nextLine().trim();
            System.out.print("  Phone   [" + c.getPhone()   + "]: "); String phone = sc.nextLine().trim();
            System.out.print("  Email   [" + c.getEmail()   + "]: "); String email = sc.nextLine().trim();
            System.out.print("  Address [" + c.getAddress() + "]: "); String addr  = sc.nextLine().trim();

            if (!name.isEmpty())  c.setName(name);
            if (!phone.isEmpty()) c.setPhone(phone);
            if (!email.isEmpty()) c.setEmail(email);
            if (!addr.isEmpty())  c.setAddress(addr);

            customerDAO.update(c);
        } catch (FinancianException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }

    static void deleteCustomer() {
        System.out.println("\n  --- Delete Customer ---");
        System.out.print("  Enter Customer ID (e.g. CUST01): ");
        String id = sc.nextLine().trim().toUpperCase();
        System.out.print("  Are you sure? (yes/no): ");
        if (sc.nextLine().trim().equalsIgnoreCase("yes")) customerDAO.delete(id);
        else System.out.println("  Delete cancelled.");
    }

    // ================================================================
    //  ACCOUNT
    // ================================================================
    static void addAccount() {
        System.out.println("\n  --- Add New Account ---");
        System.out.print("  Account ID (e.g. ACC06)      : "); String accId  = sc.nextLine().trim().toUpperCase();
        System.out.print("  Customer ID (e.g. CUST01)    : "); String custId = sc.nextLine().trim().toUpperCase();
        System.out.println("  Type options: Savings / Current / Salary");
        System.out.print("  Account Type                 : "); String type   = sc.nextLine().trim();
        System.out.print("  Opening Balance (Rs.)        : "); BigDecimal bal = readDecimal();
        System.out.print("  Open Date (YYYY-MM-DD)       : "); Date date = readDate();
        if (date == null) { System.out.println("  [ERROR] Invalid date."); return; }

        Account acc = new Account(accId, type, bal, date, custId);
        accountDAO.add(acc);
    }

    static void viewAllAccounts() {
        System.out.println("\n  --- All Accounts ---");
        List<Account> list = accountDAO.getAll();
        if (list.isEmpty()) { System.out.println("  No accounts found."); return; }
        list.forEach(a -> System.out.println("  " + a));
    }

    static void viewAccountsByCustomer() {
        System.out.println("\n  --- Accounts by Customer ---");
        System.out.print("  Enter Customer ID (e.g. CUST01): ");
        String id = sc.nextLine().trim().toUpperCase();
        List<Account> list = accountDAO.getByCustomerId(id);
        if (list.isEmpty()) System.out.println("  No accounts found for: " + id);
        else list.forEach(a -> System.out.println("  " + a));
    }

    // ================================================================
    //  LOAN
    // ================================================================
    static void addLoan() {
        System.out.println("\n  --- Add New Loan ---");
        System.out.print("  Loan ID (e.g. LOAN06)        : "); String loanId = sc.nextLine().trim().toUpperCase();
        System.out.print("  Account ID (e.g. ACC01)      : "); String accId  = sc.nextLine().trim().toUpperCase();
        System.out.println("  Type options: Home / Car / Personal / Education / Business");
        System.out.print("  Loan Type                    : "); String type   = sc.nextLine().trim();
        System.out.print("  Principal Amount (Rs.)       : "); BigDecimal principal = readDecimal();
        System.out.print("  Interest Rate (%)            : "); BigDecimal rate      = readDecimal();
        int tenure = readInt("  Tenure (months)              : ");
        System.out.print("  Start Date (YYYY-MM-DD)      : "); Date date = readDate();
        if (date == null) { System.out.println("  [ERROR] Invalid date."); return; }

        Loan loan = new Loan(loanId, type, principal, rate, tenure, date, accId);
        loanDAO.add(loan);
    }

    static void viewAllLoans() {
        System.out.println("\n  --- All Loans ---");
        List<Loan> list = loanDAO.getAll();
        if (list.isEmpty()) { System.out.println("  No loans found."); return; }
        list.forEach(l -> System.out.println("  " + l));
    }

    static void viewLoanEMISummary() {
        System.out.println("\n  --- Loan + EMI Summary ---");
        loanDAO.viewLoanEMISummary();
    }

    // ================================================================
    //  EMI
    // ================================================================
    static void addEMI() {
        System.out.println("\n  --- Add New EMI ---");
        System.out.print("  EMI ID (e.g. EMI011)         : "); String emiId  = sc.nextLine().trim().toUpperCase();
        System.out.print("  Loan ID (e.g. LOAN01)        : "); String loanId = sc.nextLine().trim().toUpperCase();
        System.out.print("  EMI Amount (Rs.)             : "); BigDecimal amt = readDecimal();
        System.out.print("  Due Date (YYYY-MM-DD)        : "); Date date = readDate();
        if (date == null) { System.out.println("  [ERROR] Invalid date."); return; }
        System.out.println("  Status options: Paid / Unpaid / Overdue");
        System.out.print("  Paid Status                  : "); String status = sc.nextLine().trim();

        EMI emi = new EMI(emiId, amt, date, status, loanId);
        emiDAO.add(emi);
    }

    static void viewEMIsByLoan() {
        System.out.println("\n  --- EMIs by Loan ---");
        System.out.print("  Enter Loan ID (e.g. LOAN01): ");
        String id = sc.nextLine().trim().toUpperCase();
        List<EMI> list = emiDAO.getByLoanId(id);
        if (list.isEmpty()) System.out.println("  No EMIs found for Loan: " + id);
        else list.forEach(e -> System.out.println("  " + e));
    }

    static void markEMIAsPaid() {
        System.out.println("\n  --- Mark EMI as Paid ---");
        System.out.print("  Enter EMI ID (e.g. EMI001): ");
        String id = sc.nextLine().trim().toUpperCase();
        emiDAO.markEMIAsPaid(id);
    }

    static void updateEMIStatus() {
        System.out.println("\n  --- Update EMI Status ---");
        System.out.print("  Enter EMI ID (e.g. EMI001): ");
        String id = sc.nextLine().trim().toUpperCase();
        EMI emi = emiDAO.getById(id);
        if (emi == null) { System.out.println("  [ERROR] EMI not found: " + id); return; }
        System.out.println("  Current Status: " + emi.getPaidStatus());
        System.out.println("  Options: Paid / Unpaid / Overdue  (ENUM - use exactly as shown)");
        System.out.print("  New Status: ");
        String status = sc.nextLine().trim();
        emi.setPaidStatus(status);
        emiDAO.update(emi);
    }

    // ================================================================
    //  REPORTS
    // ================================================================
    static void viewCustomerLoanReport() {
        System.out.println("\n  --- Customer + Account + Loan Report (3-Table JOIN) ---");
        customerDAO.viewCustomerLoanSummary();
    }

    static void viewEMIRiskReport() {
        System.out.println("\n  --- EMI Risk Profile Report (5-Table JOIN) ---");
        emiDAO.viewEMIRiskReport();
    }

    static void callOverdueEMIProcedure() {
        System.out.println("\n  --- Overdue EMI Report (Stored Procedure) ---");
        System.out.print("  Enter Loan ID (e.g. LOAN01): ");
        String loanId = sc.nextLine().trim().toUpperCase();
        emiDAO.callGetOverdueEMIs(loanId);
    }

    // ================================================================
    //  HELPERS
    // ================================================================
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  [!] Enter a valid number."); }
        }
    }

    static BigDecimal readDecimal() {
        while (true) {
            try { return new BigDecimal(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("  [!] Invalid amount. Try again: Rs."); }
        }
    }

    static Date readDate() {
        try { return Date.valueOf(sc.nextLine().trim()); }
        catch (IllegalArgumentException e) { return null; }
    }
}
