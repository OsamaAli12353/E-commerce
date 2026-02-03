package ecommerce.ecommerce.service;

import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.User;
import java.util.List;

public interface UserService {

    User findUserById(int id);

    List<UserWithDetailsDTO> getAllUsersWithDetails();

    UserWithDetailsDTO getUserWithDetailsById(int id); // أُضيفت هنا

    User addOrUpdateUser(User user);

    void updateUser(int id, User updatedUser); // أُضيفت هنا

    void deleteUserById(int id);

    User login(String email, String password);

    boolean isAdmin(User user);
}
