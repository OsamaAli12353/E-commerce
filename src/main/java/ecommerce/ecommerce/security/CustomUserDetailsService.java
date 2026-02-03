package ecommerce.ecommerce.security;

import ecommerce.ecommerce.entity.User;
import ecommerce.ecommerce.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * Loads user-specific data during authentication process.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Repository for accessing user data from the database
    private final UserRepository userRepo;

    // Constructor
    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Loads user by email (username) for authentication.
     * Called by Spring Security during the authentication process.
     *
     * @param email The user's email address (used as username)
     * @return UserDetails object containing user information
     * @throws UsernameNotFoundException if user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Find user by email in the database
        User user = userRepo.findUsersByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        // Wrap the User entity in CustomUserDetails and return
        return new CustomUserDetails(user);
    }
}