package ecommerce.ecommerce.service;

import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.Users;
import java.util.List;

public interface UserService {

    Users findUserById(int id);

    List<UserWithDetailsDTO> getAllUsersWithDetails();

    UserWithDetailsDTO getUserWithDetailsById(int id); // أُضيفت هنا

    Users addOrUpdateUser(Users user);

    void updateUser(int id, Users updatedUser); // أُضيفت هنا

    void deleteUserById(int id);

    Users login(String email, String password);

    Users register(String name, String email, String password);
    boolean isAdmin(Users user);
}
