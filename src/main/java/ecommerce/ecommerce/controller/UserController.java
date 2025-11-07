package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.RoleDTO;
import ecommerce.ecommerce.DTO.TransactionDTO;
import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.Users;
import ecommerce.ecommerce.service.UserService;
import org.springframework.web.bind.annotation.*;

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
        List<Users> usersList = userService.findAllUsers();

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

    //buy
}
