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
import ecommerce.ecommerce.security.SecurityConfig;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository,RolesRepository rolesRepository,PasswordEncoder passwordEncoder) {
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
    public List<UserWithDetailsDTO> getAllUsersWithDetails() {
        List<Users> usersList = usersRepository.findAll();

        return usersList.stream().map(user -> {
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
        }).toList();
    }

    @Transactional
    @Override
    public void addOrUpdateUser(Users user) {
        usersRepository.save(user);
        System.out.println("User saved successfully");
    }

    @Transactional
    @Override
    public void deleteUserById(int id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        usersRepository.delete(user);
        System.out.println("User deleted successfully");
    }

    @Override
    public Users login(String email, String password) {

        Users user = usersRepository.findUsersByEmail(email);

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid email or password");
    }

    @Transactional
    @Override
    public Users register(String name, String email, String password) {
        //check if email is exist
        Users existingUser = usersRepository.findUsersByEmail(email);
        if (existingUser != null)
            throw new RuntimeException("Email is already registered");

        // check password is secure
        if (!PasswordValidator.isValid(password)) {
            throw new RuntimeException(
                    "Password must be at least 8 characters long, contain a capital letter, a number, and a special character."
            );
        }
        String hashedPassword = passwordEncoder.encode(password);

        Users newUser = new Users(name, email, hashedPassword);
        //give it default role as costumer
        Roles customerRole = rolesRepository.findByRoleName("CUSTOMER");

        if (customerRole == null) {
            throw new RuntimeException("Customer role not found in database");
        }
        newUser.setRole(customerRole);

        return usersRepository.save(newUser);
    }

}
