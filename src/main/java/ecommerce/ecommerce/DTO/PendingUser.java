package ecommerce.ecommerce.DTO;

import java.io.Serializable;

/**
 * Represents a user awaiting email verification.
 * Stored temporarily in Redis until OTP is verified.
 * Implements Serializable to allow Redis caching.
 */
public class PendingUser implements Serializable {
    // User's full name
    private String name;

    // User's email address (used as identifier)
    private String email;

    // User's hashed password
    private String password;

    // Default constructor
    public PendingUser() {
    }

    // Constructor with all fields
    public PendingUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}