-- ============================================================
-- FINANCIAN - Stored Procedure
-- FIX: Loan_ID is VARCHAR to match your schema.
-- Run this in MySQL Workbench AFTER setting up schema tables.
-- ============================================================

USE financian;

DROP PROCEDURE IF EXISTS GetOverdueEMIsByLoan;

DELIMITER $$

CREATE PROCEDURE GetOverdueEMIsByLoan(
    IN  p_loan_id       VARCHAR(10),
    OUT p_missed_count  INT,
    OUT p_total_overdue DECIMAL(15,2)
)
BEGIN
    SELECT COUNT(*)
    INTO   p_missed_count
    FROM   EMI
    WHERE  Loan_ID    = p_loan_id
      AND  Paid_Status IN ('Unpaid', 'Overdue');

    SELECT COALESCE(SUM(EMI_Amount), 0.00)
    INTO   p_total_overdue
    FROM   EMI
    WHERE  Loan_ID    = p_loan_id
      AND  Paid_Status IN ('Unpaid', 'Overdue');
END$$

DELIMITER ;

-- Quick test:
-- CALL GetOverdueEMIsByLoan('LOAN01', @missed, @overdue);
-- SELECT @missed AS Missed_EMI_Count, @overdue AS Total_Overdue;
