package ecommerce.ecommerce.security;

import ecommerce.ecommerce.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of Spring Security's UserDetails interface.
 * Wraps the User entity and provides authentication and authorization details.
 */
public class CustomUserDetails implements UserDetails {

    // The wrapped User entity
    private final User user;

    /**
     * Gets the underlying User entity.
     *
     * @return The User entity
     */
    public User getUser() {
        return user;
    }

    // Constructor
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities (roles) granted to the user.
     * Prefixes role name with "ROLE_" as required by Spring Security.
     *
     * @return Collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName())
        );
    }

    // Returns the user's password for authentication
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Returns the user's email as the username for authentication
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Account status methods - all return true (no expiration/locking logic implemented)

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}