package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.OtpVerifyRequest;
import ecommerce.ecommerce.DTO.UserDTO;
import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.User;
import ecommerce.ecommerce.security.CustomUserDetails;
import ecommerce.ecommerce.service.AuthService;
import ecommerce.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static ecommerce.ecommerce.service.PasswordValidator.isValid;

/**
 * Controller for managing user-related operations.
 * Handles user registration, OTP verification, CRUD operations with role-based access control.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    /**
     * Constructor injection of required services.
     *
     * @param userService Service for user management operations
     * @param authService Service for authentication and registration
     */
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * Retrieves all users with their complete details.
     * ADMIN ONLY - Only administrators can view all users.
     * GET /api/users
     *
     * @return List of all users with detailed information as DTOs
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserWithDetailsDTO> getAllUsers() {
        return userService.getAllUsersWithDetails();
    }

    /**
     * Updates user information.
     * ADMIN or SAME USER - Users can update their own profile, admins can update any user.
     * PUT /api/users/{id}
     *
     * @param id The ID of the user to update
     * @param updatedUser The new user data
     * @param currentUser The currently authenticated user from JWT token
     * @return Success message
     * @throws AccessDeniedException if user tries to update another user's profile without admin rights
     */
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable int id,
            @RequestBody User updatedUser,
            @AuthenticationPrincipal CustomUserDetails currentUser)
            throws AccessDeniedException {

        User user = currentUser.getUser();

        // Authorization check: Verify the user is either updating their own account or is an admin
        // This prevents customers from modifying other users' profiles
        if (user.getUserId() != id && !userService.isAdmin(user)) {
            throw new AccessDeniedException("Access denied");
        }

        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok("User updated successfully");
    }

    /**
     * Deletes a user account.
     * ADMIN or SAME USER - Users can delete their own account, admins can delete any user.
     * DELETE /api/users/{id}
     *
     * @param id The ID of the user to delete
     * @param currentUser The currently authenticated user from JWT token
     * @return Success message
     * @throws AccessDeniedException if user tries to delete another user's account without admin rights
     */
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable int id,
            @AuthenticationPrincipal CustomUserDetails currentUser)
            throws AccessDeniedException {

        User user = currentUser.getUser();

        // Authorization check: Verify the user is either deleting their own account or is an admin
        // This prevents customers from deleting other users' accounts
        if (user.getUserId() != id && !userService.isAdmin(user)) {
            throw new AccessDeniedException("Access denied");
        }

        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Registers a new user account with OTP verification.
     * PUBLIC ENDPOINT - No authentication required for registration.
     * POST /api/users/register
     *
     * Registration process:
     * 1. Validates password strength
     * 2. Creates a pending user record
     * 3. Generates and sends OTP to user's email
     * 4. User must verify OTP to activate account
     *
     * @param req UserDTO containing registration details (name, email, password, etc.)
     * @return Success message indicating OTP has been sent
     * @throws RuntimeException if password validation fails
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO req) {
        String password = req.getPassword();

        // Validate password is provided
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Password cannot be null or empty");
        }

        // Validate password meets security requirements:
        // - 8-32 characters long
        // - At least one uppercase letter
        // - At least one number
        // - At least one special character
        if (!isValid(password)) {
            throw new RuntimeException("Password is too weak. Must be 8-32 chars, include uppercase, number, special char");
        }

        // Save user as pending and generate OTP for email verification
        authService.register(req);
        return ResponseEntity.ok("OTP sent");
    }

    /**
     * Verifies the OTP code and activates the user account.
     * PUBLIC ENDPOINT - No authentication required for OTP verification.
     * POST /api/users/verify-otp
     *
     * Verification process:
     * 1. Validates OTP code against stored value
     * 2. Moves user from pending to active status
     * 3. Deletes the pending user record
     * 4. User can now login with their credentials
     *
     * @param req OtpVerifyRequest containing email and OTP code
     * @return Success message indicating account is activated
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequest req) {
        // Log for debugging purposes
        System.out.println("verifyOtp method called for email: " + req.getEmail());

        // Verify OTP and activate user account
        authService.verifyOtp(req);

        // Log successful verification
        System.out.println("User saved successfully for email: " + req.getEmail());
        return ResponseEntity.ok("User saved successfully");
    }
}