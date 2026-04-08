-- ============================================================
-- FINANCIAN - Sample Test Data
-- VERIFIED against schema v2 (all VARCHAR PKs, correct ENUMs)
-- Run AFTER your schema SQL has created all tables.
-- ============================================================

USE financian;

-- ============================================================
-- RISK_CATEGORY (must be first - FK referenced by EMI_RISK_PROFILE)
-- Schema: RC01/RC02/RC03/RC04 | ENUM: Low/Medium/High/Critical
-- Schema already seeds this table. Skip if already inserted.
-- ============================================================
-- (RISK_CATEGORY is seeded in your schema.sql via INSERT INTO RISK_CATEGORY)
-- If you get a duplicate key error here, comment out this block.
INSERT IGNORE INTO RISK_CATEGORY (Risk_Category_Code, Risk_Level, Description) VALUES
('RC01', 'Low',      'No overdue. Regular payments. No intervention needed.'),
('RC02', 'Medium',   'Minor delays observed. Monitoring advised.'),
('RC03', 'High',     'Multiple missed EMIs. Active follow-up required.'),
('RC04', 'Critical', 'Severe overdue. NPA classification likely.');

-- ============================================================
-- CUSTOMER  (Customer_ID VARCHAR(10))
-- ============================================================
INSERT INTO CUSTOMER (Customer_ID, Name, Phone, Email, Address, PAN_Number) VALUES
('CUST01', 'Arjun Sharma',   '9876543210', 'arjun@email.com',  'Flat 12, Pune',      'ABCDE1234F'),
('CUST02', 'Priya Mehta',    '9123456780', 'priya@email.com',  'MG Road, Mumbai',    'FGHIJ5678K'),
('CUST03', 'Ravi Kulkarni',  '9988776655', 'ravi@email.com',   'FC Road, Pune',      'LMNOP9012Q'),
('CUST04', 'Sneha Patil',    '9871234560', 'sneha@email.com',  'Koregaon, Pune',     'RSTUV3456W'),
('CUST05', 'Amit Desai',     '9765432100', 'amit@email.com',   'Baner, Pune',        'XYZAB7890C');

-- ============================================================
-- ACCOUNT  (Account_ID VARCHAR(10), Customer_ID FK VARCHAR(10))
-- Account_Type is VARCHAR(30) - no ENUM restriction
-- ============================================================
INSERT INTO ACCOUNT (Account_ID, Account_Type, Balance, Open_Date, Customer_ID) VALUES
('ACC01', 'Savings',  150000.00, '2021-03-15', 'CUST01'),
('ACC02', 'Current',   75000.00, '2020-07-20', 'CUST02'),
('ACC03', 'Savings',  200000.00, '2022-01-10', 'CUST03'),
('ACC04', 'Salary',    50000.00, '2019-11-05', 'CUST04'),
('ACC05', 'Savings',  300000.00, '2023-06-01', 'CUST05');

-- ============================================================
-- LOAN  (Loan_ID VARCHAR(10), Account_ID FK VARCHAR(10))
-- Loan_Type VARCHAR(50), Loan_Tenure INT
-- ============================================================
INSERT INTO LOAN (Loan_ID, Loan_Type, Principal_Amount, Interest_Rate, Loan_Tenure, Start_Date, Account_ID) VALUES
('LOAN01', 'Home',      2500000.00, 8.50, 240, '2021-04-01', 'ACC01'),
('LOAN02', 'Car',        600000.00, 9.00,  60, '2020-09-01', 'ACC02'),
('LOAN03', 'Personal',   200000.00,12.50,  36, '2022-02-01', 'ACC03'),
('LOAN04', 'Education',  500000.00, 7.00,  84, '2020-01-15', 'ACC04'),
('LOAN05', 'Business',  1000000.00,11.00,  60, '2023-07-01', 'ACC05');

-- ============================================================
-- EMI  (EMI_ID VARCHAR(10), Loan_ID FK VARCHAR(10))
-- Paid_Status ENUM: 'Paid','Unpaid','Overdue'
-- ============================================================
INSERT INTO EMI (EMI_ID, EMI_Amount, Due_Date, Paid_Status, Loan_ID) VALUES
('EMI001', 21650.00, '2024-01-01', 'Paid',    'LOAN01'),
('EMI002', 21650.00, '2024-02-01', 'Paid',    'LOAN01'),
('EMI003', 21650.00, '2024-03-01', 'Overdue', 'LOAN01'),
('EMI004', 12450.00, '2024-01-05', 'Paid',    'LOAN02'),
('EMI005', 12450.00, '2024-02-05', 'Unpaid',  'LOAN02'),
('EMI006',  6600.00, '2024-01-10', 'Paid',    'LOAN03'),
('EMI007',  6600.00, '2024-02-10', 'Overdue', 'LOAN03'),
('EMI008',  6600.00, '2024-03-10', 'Overdue', 'LOAN03'),
('EMI009',  7350.00, '2024-01-15', 'Paid',    'LOAN04'),
('EMI010', 21750.00, '2024-01-20', 'Paid',    'LOAN05');

-- ============================================================
-- PAYMENT  (Payment_ID VARCHAR(10), EMI_ID FK UNIQUE VARCHAR(10))
-- Payment_Mode VARCHAR(30) - no ENUM restriction
-- ============================================================
INSERT INTO PAYMENT (Payment_ID, Payment_Date, Amount_Paid, Payment_Mode, Late_Fee, EMI_ID) VALUES
('PAY01', '2024-01-01', 21650.00, 'Online', 0.00,  'EMI001'),
('PAY02', '2024-02-01', 21650.00, 'UPI',    0.00,  'EMI002'),
('PAY03', '2024-01-05', 12450.00, 'Online', 0.00,  'EMI004'),
('PAY04', '2024-01-10',  6600.00, 'Cash',   0.00,  'EMI006'),
('PAY05', '2024-01-15',  7350.00, 'Cheque', 0.00,  'EMI009'),
('PAY06', '2024-01-20', 21750.00, 'UPI',    0.00,  'EMI010');

-- ============================================================
-- EMI_SCHEDULE  (Schedule_ID VARCHAR(10), Loan_ID FK VARCHAR(10))
-- ============================================================
INSERT INTO EMI_SCHEDULE (Schedule_ID, Installment_No, Expected_Date, Expected_Amount, Loan_ID) VALUES
('SCH01', 1, '2021-05-01', 21650.00, 'LOAN01'),
('SCH02', 2, '2021-06-01', 21650.00, 'LOAN01'),
('SCH03', 3, '2021-07-01', 21650.00, 'LOAN01'),
('SCH04', 1, '2022-03-01',  6600.00, 'LOAN03'),
('SCH05', 2, '2022-04-01',  6600.00, 'LOAN03');

-- ============================================================
-- TRANSACTION  (Transaction_ID VARCHAR(10), Reference_Number UNIQUE)
-- Transaction_Status ENUM: 'Success','Failed','Pending'
-- ============================================================
INSERT INTO TRANSACTION (Transaction_ID, Transaction_Date, Transaction_Amount, Transaction_Type,
    Transaction_Mode, Transaction_Status, Reference_Number, Account_ID, EMI_ID) VALUES
('TXN01', '2024-01-01', 21650.00, 'EMI Payment', 'Online', 'Success', 'REF001', 'ACC01', 'EMI001'),
('TXN02', '2024-02-01', 21650.00, 'EMI Payment', 'UPI',    'Success', 'REF002', 'ACC01', 'EMI002'),
('TXN03', '2024-01-05', 12450.00, 'EMI Payment', 'Online', 'Success', 'REF003', 'ACC02', 'EMI004'),
('TXN04', '2024-01-10',  6600.00, 'EMI Payment', 'Cash',   'Success', 'REF004', 'ACC03', 'EMI006');

-- ============================================================
-- EMI_INTEREST_BREAKUP  (Breakup_ID VARCHAR(10), EMI_ID UNIQUE FK)
-- ============================================================
INSERT INTO EMI_INTEREST_BREAKUP (Breakup_ID, Principal_Component, Interest_Component, Outstanding_Balance, EMI_ID) VALUES
('BRK01', 3900.00, 17750.00, 2496100.00, 'EMI001'),
('BRK02', 3928.00, 17722.00, 2492172.00, 'EMI002'),
('BRK03', 3956.00, 17694.00, 2488216.00, 'EMI003');

-- ============================================================
-- EMI_PREPAYMENT  (Prepayment_ID VARCHAR(10))
-- Prepayment_Type ENUM: 'Partial','Full'
-- ============================================================
INSERT INTO EMI_PREPAYMENT (Prepayment_ID, Prepayment_Date, Prepayment_Amount, Prepayment_Type, Prepayment_Fee, EMI_ID) VALUES
('PRE01', '2024-02-15', 50000.00, 'Partial', 500.00, 'EMI002');

-- ============================================================
-- EMI_RISK_PROFILE  (Stress_Level_ID VARCHAR(10), EMI_ID UNIQUE FK)
-- Monitoring_Status ENUM: 'Active','Resolved','Under Review'  ← NOT 'Escalated'
-- Risk_Category_Code FK: 'RC01','RC02','RC03','RC04'          ← NOT 'RC001'
-- ============================================================
INSERT INTO EMI_RISK_PROFILE
    (Stress_Level_ID, Risk_Flag_Date, Missed_EMI_Count, Overdue_Amount, Monitoring_Status, Last_Reviewed_Date, EMI_ID, Risk_Category_Code)
VALUES
('SL01', '2024-03-05', 1,  21650.00, 'Active',       '2024-03-10', 'EMI003', 'RC02'),
('SL02', '2024-02-10', 1,  12450.00, 'Active',       '2024-02-15', 'EMI005', 'RC02'),
('SL03', '2024-02-15', 2,  13200.00, 'Under Review', '2024-03-01', 'EMI007', 'RC03'),
('SL04', '2024-03-15', 3,  19800.00, 'Under Review', '2024-03-20', 'EMI008', 'RC03');

-- ============================================================
-- VERIFY
-- ============================================================
SELECT 'CUSTOMER'         AS TableName, COUNT(*) AS Rows FROM CUSTOMER        UNION ALL
SELECT 'ACCOUNT',                       COUNT(*)         FROM ACCOUNT         UNION ALL
SELECT 'LOAN',                          COUNT(*)         FROM LOAN            UNION ALL
SELECT 'EMI',                           COUNT(*)         FROM EMI             UNION ALL
SELECT 'PAYMENT',                       COUNT(*)         FROM PAYMENT         UNION ALL
SELECT 'EMI_SCHEDULE',                  COUNT(*)         FROM EMI_SCHEDULE    UNION ALL
SELECT 'TRANSACTION',                   COUNT(*)         FROM TRANSACTION     UNION ALL
SELECT 'EMI_INTEREST_BREAKUP',          COUNT(*)         FROM EMI_INTEREST_BREAKUP UNION ALL
SELECT 'EMI_PREPAYMENT',                COUNT(*)         FROM EMI_PREPAYMENT  UNION ALL
SELECT 'RISK_CATEGORY',                 COUNT(*)         FROM RISK_CATEGORY   UNION ALL
SELECT 'EMI_RISK_PROFILE',              COUNT(*)         FROM EMI_RISK_PROFILE;
