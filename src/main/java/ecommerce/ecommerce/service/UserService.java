package ecommerce.ecommerce.service;

import ecommerce.ecommerce.entity.Users;
import java.util.List;

public interface UserService {
    public Users findUserById(int id);
    public List<Users> findAllUsers();
    public void addOrUpdateUser(Users user);
    public void deleteUserById(int id);
}
