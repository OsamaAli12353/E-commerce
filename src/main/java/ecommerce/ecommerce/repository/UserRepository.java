package ecommerce.ecommerce.repository;

import ecommerce.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUsersByEmailAndPassword(String userName, String password);

    Optional<User> findUsersByEmail(String email);
}
