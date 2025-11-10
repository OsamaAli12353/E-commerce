package ecommerce.ecommerce.repository;

import ecommerce.ecommerce.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Integer> {
    Users findUsersByEmailAndPassword(String userName, String password);

    Users findUsersByEmail(String email);
}
