package ecommerce.ecommerce.service;

import ecommerce.ecommerce.entity.Users;
import ecommerce.ecommerce.repository.UsersRepository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public Users findUserById(int id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return user;
    }

    @Override
    public List<Users> findAllUsers() {
        List<Users> result = usersRepository.findAll();
        return result;
    }

    @Transactional
    @Override
    public void addOrUpdateUser(Users user) {
        usersRepository.save(user);
        System.out.println("user saved Successfully");
    }

    @Transactional
    @Override
    public void deleteUserById(int id) {
        //find user first
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        usersRepository.delete(user);
        System.out.println("Deleted Successfully");
    }
}
