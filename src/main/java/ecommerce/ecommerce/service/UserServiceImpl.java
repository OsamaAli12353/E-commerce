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

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RolesRepository rolesRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public UserWithDetailsDTO getUserWithDetailsById(int id) {
        User user = findUserById(id);

        RoleDTO roleDTO = user.getRole() != null
                ? new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName())
                : null;

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

    @Override
    public List<UserWithDetailsDTO> getAllUsersWithDetails() {
        return userRepository.findAll().stream()
                .map(this::mapToUserWithDetailsDTO)
                .toList();
    }

    private UserWithDetailsDTO mapToUserWithDetailsDTO(User user) {
        RoleDTO roleDTO = user.getRole() != null
                ? new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName())
                : null;

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

    @Transactional
    @Override
    public User addOrUpdateUser(User user) {
       return userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(int id, User updatedUser) {
        User existingUser = findUserById(id);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteUserById(int id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    @Override
    public User login(String email, String password) {
        // جلب المستخدم أو رمي استثناء لو مش موجود
        User user = userRepository.findUsersByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // التحقق من الباسورد
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    @Transactional
    @Override
    public User register(String name, String email, String password) {
        Optional<User> existingUser = userRepository.findUsersByEmail(email);
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        if (!PasswordValidator.isValid(password))
            throw new RuntimeException(
                    "Password must be at least 8 characters long, contain a capital letter, a number, and a special character."
            );

        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(name, email, hashedPassword);

        Roles customerRole = rolesRepository.findByRoleName("CUSTOMER");
        if (customerRole == null)
            throw new RuntimeException("Customer role not found in database");

        newUser.setRole(customerRole);
        return userRepository.save(newUser);
    }

    public boolean isAdmin(User user) {
        return user.getRole() != null && "ADMIN".equals(user.getRole().getRoleName());
    }

}