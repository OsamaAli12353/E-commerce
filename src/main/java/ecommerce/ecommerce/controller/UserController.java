package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.UserDTO;
import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.Users;
import ecommerce.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET all users with details
    @GetMapping
    public List<UserWithDetailsDTO> getAllUsers() {
        return userService.getAllUsersWithDetails();
    }

    // GET single user by ID
    @GetMapping("/{id}")
    public UserWithDetailsDTO getUserById(@PathVariable int id) {
        return userService.getUserWithDetailsById(id);
    }

    // CREATE new user
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody Users user) {
        userService.addOrUpdateUser(user);
        return ResponseEntity.ok("User added successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody Users updatedUser,
                                             @AuthenticationPrincipal Users currentUser) throws AccessDeniedException {
        // تحقق الصلاحية
        if (currentUser.getUserId() != id && !userService.isAdmin(currentUser)) {
            throw new AccessDeniedException("You can only modify your own account");
        }

        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id,
                                             @AuthenticationPrincipal Users currentUser) throws AccessDeniedException {
        // تحقق الصلاحية
        if (currentUser.getUserId() != id && !userService.isAdmin(currentUser)) {
            throw new AccessDeniedException("You can only modify your own account");
        }

        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        try {
            Users user = userService.login(email, password);
            return ResponseEntity.ok("Login successful for user: " + user.getName());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO request) {
        try {
            Users user = userService.register(
                    request.getName(),
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.ok("Register successful for user: " + user.getName());

        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.contains("Email is already registered")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("This email is already registered. Please use another email.");
            } else if (msg.contains("Password must")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(msg);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An unexpected error occurred: " + msg);
            }
        }
    }
}
