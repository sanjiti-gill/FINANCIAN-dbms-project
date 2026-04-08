package util;

/**
 * Custom Exception class for the FINANCIAN application.
 * Demonstrates custom exception handling (required for marks).
 */
public class FinancianException extends Exception {

    private int errorCode;

    // Constructor with just message
    public FinancianException(String message) {
        super(message);
        this.errorCode = 0;
    }

    // Constructor with message and error code
    public FinancianException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // Constructor with message and cause
    public FinancianException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 0;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "FinancianException [Code " + errorCode + "]: " + getMessage();
    }
}
