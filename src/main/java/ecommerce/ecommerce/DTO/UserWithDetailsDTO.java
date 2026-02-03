package ecommerce.ecommerce.DTO;

import java.util.List;

/**
 * Data Transfer Object for comprehensive user information.
 * Includes user details, role, and transaction history.
 * Used when returning complete user information (e.g., for admin view).
 */
public class UserWithDetailsDTO {
    // Unique identifier for the user
    private int userId;

    // User's full name
    private String name;

    // User's email address
    private String email;

    // User's role (ADMIN or CUSTOMER)
    private RoleDTO role;

    // List of all transactions made by the user
    private List<TransactionDTO> transactions;

    // Constructor
    public UserWithDetailsDTO(int userId, String name, String email, RoleDTO role, List<TransactionDTO> transactions) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.transactions = transactions;
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}