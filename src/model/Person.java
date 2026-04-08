package model;

/**
 * Abstract base class demonstrating INHERITANCE.
 * Customer will extend this class.
 */
public abstract class Person {

    // Encapsulated fields (private)
    private String name;
    private String phone;
    private String email;

    // Constructor
    public Person(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // Default constructor
    public Person() {}

    // Getters and Setters (ENCAPSULATION)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Abstract method - POLYMORPHISM (subclass must override)
    public abstract String getDisplayInfo();

    // toString override - POLYMORPHISM
    @Override
    public String toString() {
        return "Name: " + name + " | Phone: " + phone + " | Email: " + email;
    }
}
