package model;

/**
 * Customer model class.
 * Maps to: Customer(Customer_ID VARCHAR, Name, Phone, Email, Address, PAN_Number)
 * FIX: Customer_ID is VARCHAR in your schema (e.g. "CUST01"), not INT.
 */
public class Customer extends Person {

    private String customerId;   // VARCHAR e.g. "CUST01"
    private String address;
    private String panNumber;

    public Customer() { super(); }

    public Customer(String customerId, String name, String phone, String email,
                    String address, String panNumber) {
        super(name, phone, email);
        this.customerId = customerId;
        this.address    = address;
        this.panNumber  = panNumber;
    }

    public Customer(String name, String phone, String email,
                    String address, String panNumber) {
        super(name, phone, email);
        this.address   = address;
        this.panNumber = panNumber;
    }

    public String getCustomerId()           { return customerId; }
    public void   setCustomerId(String id)  { this.customerId = id; }
    public String getAddress()              { return address; }
    public void   setAddress(String a)      { this.address = a; }
    public String getPanNumber()            { return panNumber; }
    public void   setPanNumber(String p)    { this.panNumber = p; }

    @Override
    public String getDisplayInfo() {
        return String.format(
            "Customer ID: %-8s | Name: %-20s | Phone: %-12s | PAN: %-12s | Address: %s",
            customerId, getName(), getPhone(), panNumber, address
        );
    }

    @Override
    public String toString() { return getDisplayInfo(); }
}
