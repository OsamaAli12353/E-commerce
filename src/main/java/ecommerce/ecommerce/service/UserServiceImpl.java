package ecommerce.ecommerce.service;

import ecommerce.ecommerce.DTO.RoleDTO;
import ecommerce.ecommerce.DTO.TransactionDTO;
import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.Roles;
import ecommerce.ecommerce.entity.Users;
import ecommerce.ecommerce.repository.RolesRepository;
import ecommerce.ecommerce.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository,
                           RolesRepository rolesRepository,
                           PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users findUserById(int id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public UserWithDetailsDTO getUserWithDetailsById(int id) {
        Users user = findUserById(id);

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
        return usersRepository.findAll().stream()
                .map(this::mapToUserWithDetailsDTO)
                .toList();
    }

    private UserWithDetailsDTO mapToUserWithDetailsDTO(Users user) {
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
    public Users addOrUpdateUser(Users user) {
       return usersRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(int id, Users updatedUser) {
        Users existingUser = findUserById(id);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        usersRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteUserById(int id) {
        Users user = findUserById(id);
        usersRepository.delete(user);
    }

    @Override
    public Users login(String email, String password) {
        Users user = usersRepository.findUsersByEmail(email);
        if (user == null) throw new RuntimeException("Invalid email or password");
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new RuntimeException("Invalid email or password");
        return user;
    }

    @Transactional
    @Override
    public Users register(String name, String email, String password) {
        if (usersRepository.findUsersByEmail(email) != null)
            throw new RuntimeException("Email is already registered");

        if (!PasswordValidator.isValid(password))
            throw new RuntimeException(
                    "Password must be at least 8 characters long, contain a capital letter, a number, and a special character."
            );

        String hashedPassword = passwordEncoder.encode(password);
        Users newUser = new Users(name, email, hashedPassword);

        Roles customerRole = rolesRepository.findByRoleName("CUSTOMER");
        if (customerRole == null)
            throw new RuntimeException("Customer role not found in database");

        newUser.setRole(customerRole);
        return usersRepository.save(newUser);
    }
    public boolean isAdmin(Users user) {
        return user.getRole() != null && "ADMIN".equals(user.getRole().getRoleName());
    }

}