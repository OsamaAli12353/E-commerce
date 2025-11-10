package ecommerce.ecommerce.repository;

import ecommerce.ecommerce.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles,Integer> {
    Roles findByRoleName(String roleName);

}
