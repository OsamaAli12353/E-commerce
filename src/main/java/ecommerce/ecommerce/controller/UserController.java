package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.RoleDTO;
import ecommerce.ecommerce.DTO.TransactionDTO;
import ecommerce.ecommerce.DTO.UserDTO;
import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.Users;
import ecommerce.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<Users> usersList =new ArrayList<>();

        return usersList.stream().map(user -> {
            // تحويل Role
            RoleDTO roleDTO = null;
            if (user.getRole() != null) {
                roleDTO = new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName());
            }

            // تحويل Transactions
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
        }).toList();
    }

    // GET single user by ID
    @GetMapping("/{id}")
    public UserWithDetailsDTO getUserById(@PathVariable int id) {
        Users user = userService.findUserById(id);

        RoleDTO roleDTO = null;
        if (user.getRole() != null) {
            roleDTO = new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName());
        }

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

    // CREATE new user
    @PostMapping
    public String addUser(@RequestBody Users user) {
        userService.addOrUpdateUser(user);
        return "User added successfully";
    }

    // UPDATE existing user
    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id, @RequestBody Users updatedUser) {
        Users existingUser = userService.findUserById(id);

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setTransactions(updatedUser.getTransactions());

        userService.addOrUpdateUser(existingUser);
        return "User updated successfully";
    }

    // DELETE user by ID
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return "User deleted successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        Users user = userService.login(email, password);
        if (user != null) {
            return ResponseEntity.ok("Login successful for user: " + user.getName());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO request) {
        try {
            Users user = userService.register(
                    request.getName(),
                    request.getEmail(),
                    request.getPassword()
            );
            //register success
            return ResponseEntity.ok("Register successful for user: " + user.getName());

        } catch (RuntimeException e) {
            // to get more info about the problem
            String msg = e.getMessage();
            //if the email exist
            if (msg.contains("Email is already registered")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("This email is already registered. Please use another email.");
            }
            //if the password doesn't meet requirment
            else if (msg.contains("Password must")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(msg);
            }
            //if there is server problem
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + msg);
        }
    }
}


