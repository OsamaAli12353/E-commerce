package ecommerce.ecommerce.service;

import ecommerce.ecommerce.DTO.RoleDTO;
import ecommerce.ecommerce.DTO.TransactionDTO;
import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.Roles;
import ecommerce.ecommerce.entity.User;
import ecommerce.ecommerce.repository.RolesRepository;
import ecommerce.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for user operations.
 * Handles user CRUD operations, authentication, and DTO conversions.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RolesRepository rolesRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Finds a user by ID.
     *
     * @param id The user ID
     * @return The user entity
     * @throws RuntimeException if user not found
     */
    @Override
    public User findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    /**
     * Retrieves complete user details including role and transactions.
     *
     * @param id The user ID
     * @return UserWithDetailsDTO containing user info, role, and transaction history
     */
    @Override
    public UserWithDetailsDTO getUserWithDetailsById(int id) {
        User user = findUserById(id);

        // Convert role entity to DTO
        RoleDTO roleDTO = user.getRole() != null
                ? new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName())
                : null;

        // Convert transaction entities to DTOs
        List<TransactionDTO> transactionsDTO = user.getTransactions().stream()
                .map(tx -> new TransactionDTO(
                        tx.getTransactionId(),
                        tx.getTransactionDate(),
                        tx.getTransactionInfo(),
                        tx.getUser() != null ? tx.getUser().getName() : null,
                        tx.getUser() != null ? tx.getUser().getEmail() : null
                ))
                .toList();

        return new UserWithDetailsDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                roleDTO,
                transactionsDTO
        );
    }

    /**
     * Retrieves all users with their complete details.
     *
     * @return List of UserWithDetailsDTO for all users
     */
    @Override
    public List<UserWithDetailsDTO> getAllUsersWithDetails() {
        return userRepository.findAll().stream()
                .map(this::mapToUserWithDetailsDTO)
                .toList();
    }

    /**
     * Helper method to convert User entity to UserWithDetailsDTO.
     *
     * @param user The user entity
     * @return UserWithDetailsDTO with role and transactions
     */
    private UserWithDetailsDTO mapToUserWithDetailsDTO(User user) {
        // Convert role to DTO
        RoleDTO roleDTO = user.getRole() != null
                ? new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName())
                : null;

        // Convert transactions to DTOs
        List<TransactionDTO> transactionsDTO = user.getTransactions().stream()
                .map(tx -> new TransactionDTO(
                        tx.getTransactionId(),
                        tx.getTransactionDate(),
                        tx.getTransactionInfo(),
                        tx.getUser() != null ? tx.getUser().getName() : null,
                        tx.getUser() != null ? tx.getUser().getEmail() : null
                ))
                .toList();

        return new UserWithDetailsDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                roleDTO,
                transactionsDTO
        );
    }

    /**
     * Adds a new user or updates an existing one.
     *
     * @param user The user to save or update
     * @return The saved user entity
     */
    @Transactional
    @Override
    public User addOrUpdateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates specific user fields.
     * Only updates password if provided and not blank.
     *
     * @param id The user ID to update
     * @param updatedUser User object containing new values
     */
    @Transactional
    @Override
    public void updateUser(int id, User updatedUser) {
        User existingUser = findUserById(id);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        // Only update password if provided and not empty
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUser);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The user ID to delete
     */
    @Transactional
    @Override
    public void deleteUserById(int id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    /**
     * Authenticates a user with email and password.
     * Note: This method is typically not used directly in JWT-based authentication.
     *
     * @param email User's email
     * @param password User's plain text password
     * @return The authenticated user
     * @throws RuntimeException if credentials are invalid
     */
    @Override
    public User login(String email, String password) {
        // Retrieve user or throw exception if not found
        User user = userRepository.findUsersByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    /**
     * Checks if a user has ADMIN role.
     *
     * @param user The user to check
     * @return true if user is an admin, false otherwise
     */
    public boolean isAdmin(User user) {
        return user.getRole() != null && "ADMIN".equals(user.getRole().getRoleName());
    }
}