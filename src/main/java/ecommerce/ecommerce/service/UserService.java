package ecommerce.ecommerce.service;

import ecommerce.ecommerce.DTO.UserWithDetailsDTO;
import ecommerce.ecommerce.entity.Users;
import java.util.List;

public interface UserService {
    public Users findUserById(int id);
    List<UserWithDetailsDTO> getAllUsersWithDetails();
    public void addOrUpdateUser(Users user);
    public void deleteUserById(int id);
    public Users login(String email,String Password);
    public Users register(String name,String email,String Password );
}
