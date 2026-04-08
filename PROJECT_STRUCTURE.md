# FINANCIAN - EMI Financial Stress & Early Warning System
## Java JDBC Mini Project

## Project Structure
```
FINANCIAN/
├── src/
│   ├── model/
│   │   ├── Person.java              (abstract base class)
│   │   ├── Customer.java            (extends Person)
│   │   ├── Account.java
│   │   ├── Loan.java
│   │   ├── EMI.java
│   │   ├── Payment.java
│   │   ├── RiskCategory.java
│   │   └── EMIRiskProfile.java
│   ├── dao/
│   │   ├── DatabaseOperations.java  (interface)
│   │   ├── CustomerDAO.java
│   │   ├── AccountDAO.java
│   │   ├── LoanDAO.java
│   │   └── EMIDAO.java
│   ├── util/
│   │   ├── DBConnection.java
│   │   └── FinancianException.java  (custom exception)
│   └── main/
│       └── MainMenu.java
├── stored_procedure.sql
└── PROJECT_STRUCTURE.md
```

## How to Run
1. Install MySQL + Java JDK 8+
2. Create database: `CREATE DATABASE financian_db;`
3. Run your schema SQL files from the GitHub repo
4. Run stored_procedure.sql
5. Update DBConnection.java with your MySQL credentials
6. Compile: `javac -cp .;mysql-connector-java-8.x.jar src/**/*.java`
7. Run: `java -cp .;mysql-connector-java-8.x.jar main.MainMenu`
