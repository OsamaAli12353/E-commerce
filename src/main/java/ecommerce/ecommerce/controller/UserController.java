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

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserWithDetailsDTO> getAllUsers() {
        return userService.getAllUsersWithDetails();
    }

    // ADMIN or same user
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable int id,
            @RequestBody User updatedUser,
            @AuthenticationPrincipal CustomUserDetails currentUser)
            throws AccessDeniedException {

        User user = currentUser.getUser();

        if (user.getUserId() != id && !userService.isAdmin(user)) {
            throw new AccessDeniedException("Access denied");
        }

        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok("User updated successfully");
    }

    // ADMIN or same user
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable int id,
            @AuthenticationPrincipal CustomUserDetails currentUser)
            throws AccessDeniedException {

        User user = currentUser.getUser();

        if (user.getUserId() != id && !userService.isAdmin(user)) {
            throw new AccessDeniedException("Access denied");
        }

        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO req) {
        authService.register(req);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequest req) {
        authService.verifyOtp(req);
        return ResponseEntity.ok("User saved successfully");
    }

}
