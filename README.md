# FINANCIAN — EMI Financial Stress & Early Warning System
### Java JDBC Mini Project | DBMS | Second Year Computer Engineering

---

## Project Overview

FINANCIAN is a **console-based Java JDBC application** that interfaces with a MySQL database
to manage customers, bank accounts, loans, and EMI tracking — with an integrated
**Early Warning System** that identifies financially stressed customers.

---

## OOP Concepts Demonstrated

| Concept          | Where Used |
|-----------------|------------|
| **Encapsulation** | All model classes use `private` fields + getters/setters |
| **Inheritance**   | `Customer extends Person` (abstract base class) |
| **Polymorphism**  | `getDisplayInfo()` overridden in `Customer`; `toString()` overridden in all models |
| **Interface**     | `DatabaseOperations<T>` implemented by all DAO classes |
| **Abstract Class**| `Person` is abstract with abstract method `getDisplayInfo()` |
| **Custom Exception** | `FinancianException` used for validation errors |

---

## Project Structure

```
FINANCIAN/
├── src/
│   ├── model/
│   │   ├── Person.java            ← Abstract base class (Inheritance root)
│   │   ├── Customer.java          ← Extends Person
│   │   ├── Account.java
│   │   ├── Loan.java
│   │   ├── EMI.java
│   │   ├── Payment.java
│   │   ├── RiskCategory.java
│   │   └── EMIRiskProfile.java
│   ├── dao/
│   │   ├── DatabaseOperations.java ← Interface
│   │   ├── CustomerDAO.java        ← INSERT, SELECT, UPDATE, DELETE + JOIN
│   │   ├── AccountDAO.java
│   │   ├── LoanDAO.java            ← JOIN with EMI count
│   │   └── EMIDAO.java             ← CallableStatement + Risk JOIN
│   ├── util/
│   │   ├── DBConnection.java       ← JDBC connection (Singleton)
│   │   └── FinancianException.java ← Custom exception
│   └── main/
│       └── MainMenu.java           ← Entry point, menu-driven
├── stored_procedure.sql            ← MySQL stored procedure
├── sample_data.sql                 ← Test data for all tables
└── PROJECT_STRUCTURE.md
```

---

## Setup Instructions

### Prerequisites
- Java JDK 8 or higher
- MySQL 8.x
- `mysql-connector-java-8.x.x.jar` (download from https://dev.mysql.com/downloads/connector/j/)

### Step 1 — Create the Database
```sql
CREATE DATABASE financian_db;
USE financian_db;
```

### Step 2 — Run your Schema SQL
Run the SQL files from the GitHub repo (schema folder) to create all 11 tables.

### Step 3 — Load Sample Data
```bash
mysql -u root -p financian_db < sample_data.sql
```

### Step 4 — Run Stored Procedure
```bash
mysql -u root -p financian_db < stored_procedure.sql
```

### Step 5 — Configure DB Credentials
Open `src/util/DBConnection.java` and update:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/financian_db";
private static final String USER     = "root";
private static final String PASSWORD = "yourpassword";
```

### Step 6 — Compile
```bash
# From the FINANCIAN/ directory:
javac -cp ".;mysql-connector-java-8.x.x.jar" src/model/*.java src/util/*.java src/dao/*.java src/main/*.java
```
*(Use `:` instead of `;` on Linux/macOS)*

### Step 7 — Run
```bash
java -cp ".;mysql-connector-java-8.x.x.jar;src" main.MainMenu
```

---

## JDBC Features Used

| Feature              | Location |
|---------------------|----------|
| `PreparedStatement`  | All DAO classes (INSERT, UPDATE, DELETE, SELECT) |
| `ResultSet`          | All SELECT operations |
| `CallableStatement`  | `EMIDAO.callGetOverdueEMIs()` |
| `SQLException`       | All DAO methods with try-catch |
| Custom Exception     | `FinancianException` in CustomerDAO + MainMenu |
| JOIN Queries         | `viewCustomerLoanSummary()`, `viewLoanEMISummary()`, `viewEMIRiskReport()` |

---

## Sample Console Output

```
  ███████╗██╗███╗   ██╗ █████╗ ███╗   ██╗ ██████╗██╗ █████╗ ███╗   ██╗
  ...
         EMI Financial Stress & Early Warning System  | DBMS Mini Project

  ┌──────────────────────────────────────────────┐
  │              MAIN MENU                       │
  ├──────────────────────────────────────────────┤
  │   1. Add Customer                            │
  │   2. View All Customers                      │
  │  ...                                         │
  │  16. Customer + Account + Loan Report (JOIN) │
  │  17. EMI Risk Profile Report (JOIN)          │
  │  18. Overdue EMI Report (Stored Procedure)   │
  │   0. Exit                                    │
  └──────────────────────────────────────────────┘

--- All Customers ---
  Customer ID: 1     | Name: Arjun Sharma          | Phone: 9876543210   | PAN: ABCDE1234F  | Address: Flat 12, Pune
  Customer ID: 2     | Name: Priya Mehta           | Phone: 9123456780   | PAN: FGHIJ5678K  | Address: MG Road, Mumbai
  ...

--- Customer + Account + Loan Report ---
  C.ID  Customer Name         Phone        | Acc.ID   Acc.Type   Balance(₹)   | Loan ID  Loan Type  Principal(₹)   Rate%   Tenure(mo)
  1     Arjun Sharma          9876543210   | 1        Savings    150000.00    | 1        Home        2500000.00     8.50    240
  ...

--- EMI FINANCIAL STRESS & EARLY WARNING REPORT ---
  Customer           Phone        Loan Type  EMI Amt(₹) EMI ID    Due Date     Status   Missed EMIs  Risk Level
  Ravi Kulkarni      9988776655   Personal   6600.00    7         2024-02-10   Overdue  2            High
    Overdue: ₹13200.00 | Monitoring: Escalated | Borrower has missed 3+ EMIs...
  ...

--- Stored Procedure Result ---
  Loan ID             : 3
  Missed/Overdue EMIs : 2
  Total Overdue Amount: ₹13200.00
```

---

## DML Operations Summary

| Operation | Method |
|-----------|--------|
| INSERT    | `CustomerDAO.add()`, `AccountDAO.add()`, `LoanDAO.add()`, `EMIDAO.add()` |
| SELECT    | `getAll()`, `getById()`, `getByCustomerId()`, `searchByName()` |
| UPDATE    | `CustomerDAO.update()`, `EMIDAO.markEMIAsPaid()`, `EMIDAO.update()` |
| DELETE    | `CustomerDAO.delete()`, `AccountDAO.delete()`, `LoanDAO.delete()` |
| JOIN      | `viewCustomerLoanSummary()` (3-table), `viewLoanEMISummary()`, `viewEMIRiskReport()` (5-table) |
| PROCEDURE | `EMIDAO.callGetOverdueEMIs()` via `CallableStatement` |

---

*Project by: [Your Name] | Roll No: [Your Roll No] | Class: SE Computer Engineering*
