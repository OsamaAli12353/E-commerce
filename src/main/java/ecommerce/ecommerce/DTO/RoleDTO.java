package ecommerce.ecommerce.DTO;

/**
 * Data Transfer Object for role information.
 * Represents user roles (e.g., ADMIN, CUSTOMER) in the system.
 */
public class RoleDTO {
    // Unique identifier for the role
    private int roleId;

    // Name of the role (e.g., "ADMIN", "CUSTOMER")
    private String roleName;

    // Constructor
    public RoleDTO(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    // Getters and Setters

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}