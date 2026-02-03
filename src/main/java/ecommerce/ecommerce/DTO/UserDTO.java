package ecommerce.ecommerce.DTO;

/**
 * Data Transfer Object for user registration and basic user information.
 * Used to transfer user data without exposing the full User entity.
 */
public class UserDTO {
    // User's full name
    private String name;

    // User's email address
    private String email;

    // User's password (plain text in request, will be hashed before storage)
    private String password;

    // Constructor
    public UserDTO(String name, String email, String password) {
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