package ecommerce.ecommerce.DTO;

import java.util.List;

public class UserWithDetailsDTO {
    private int userId;
    private String name;
    private String email;
    private RoleDTO role;
    private List<TransactionDTO> transactions;

    public UserWithDetailsDTO(int userId, String name, String email, RoleDTO role, List<TransactionDTO> transactions) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.transactions = transactions;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public RoleDTO getRole() { return role; }
    public void setRole(RoleDTO role) { this.role = role; }

    public List<TransactionDTO> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionDTO> transactions) { this.transactions = transactions; }
}
