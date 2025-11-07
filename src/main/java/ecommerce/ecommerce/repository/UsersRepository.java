package ecommerce.ecommerce.repository;

import ecommerce.ecommerce.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Integer> {
}
